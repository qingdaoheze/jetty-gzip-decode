package org.sample.jetty.client.http2;

import org.eclipse.jetty.http.HostPortHttpField;
import org.eclipse.jetty.http.HttpFields;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.http.HttpScheme;
import org.eclipse.jetty.http.HttpVersion;
import org.eclipse.jetty.http.MetaData;
import org.eclipse.jetty.http2.FlowControlStrategy;
import org.eclipse.jetty.http2.api.Session;
import org.eclipse.jetty.http2.api.Stream;
import org.eclipse.jetty.http2.client.HTTP2Client;
import org.eclipse.jetty.http2.frames.DataFrame;
import org.eclipse.jetty.http2.frames.HeadersFrame;
import org.eclipse.jetty.util.Callback;
import org.eclipse.jetty.util.FuturePromise;
import org.eclipse.jetty.util.Promise;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Date: 2021/4/1
 */
public class HttpClientWithHttp2TransportWithLowLevelApi {
    private static final Logger log = LoggerFactory.getLogger(HttpClientWithHttp2TransportWithLowLevelApi.class);

    public static void main(String[] args) throws Exception {
        lowLevelHttpClient();
    }

    public static void lowLevelHttpClient() throws Exception {
        HTTP2Client http2Client = new HTTP2Client();

        // start
        QueuedThreadPool clientExecutor = new QueuedThreadPool();
        clientExecutor.setName("client");
        http2Client.setExecutor(clientExecutor);
        http2Client.setInitialSessionRecvWindow(FlowControlStrategy.DEFAULT_WINDOW_SIZE);
        http2Client.setInitialStreamRecvWindow(FlowControlStrategy.DEFAULT_WINDOW_SIZE);
        http2Client.start();

        // 连接
        String hostName = "localhost";
        int port = 8081;
        InetSocketAddress address = new InetSocketAddress(hostName, port);
        FuturePromise<Session> promise = new FuturePromise<>();
        Session.Listener.Adapter sessionListener = new Session.Listener.Adapter();
        http2Client.connect(address, sessionListener, promise);
        Session session = promise.get(5, TimeUnit.SECONDS);

        // headers
        HttpFields headers = new HttpFields();
        headers.put(HttpHeader.CONTENT_TYPE, "application/json");

        // request
        String uri = "/tomcat/post";
        String authority = hostName + ":" + port;
        MetaData.Request request = new MetaData.Request("POST", HttpScheme.HTTP, new HostPortHttpField(authority), uri, HttpVersion.HTTP_2, headers);

        HeadersFrame frame = new HeadersFrame(request, null, true);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final CountDownLatch latch = new CountDownLatch(1);

//        Stream.Listener.Adapter listener = new Stream.Listener.Adapter()
//        {
//            @Override
//            public void onHeaders(Stream stream, HeadersFrame frame)
//            {
//                try {
//                    log.info("expect true: {} - stream.getId() > 0", stream.getId() > 0);
//                    log.info("expect equals: {} - stream.getId(), frame.getStreamId()", stream.getId() == frame.getStreamId());
//                    log.info("expect true: {} - frame.getMetaData().isResponse()", frame.getMetaData().isResponse());
//                    MetaData.Response response = (MetaData.Response)frame.getMetaData();
//                    log.info("expect 200: {} - response.getStatus()", response.getStatus());
//                } catch (Exception e) {
//                    log.error(e.getMessage(), e);
//                } finally {
//                    latch.countDown();
//                }
//            }
//
//            @Override
//            public void onData(Stream stream, DataFrame frame, Callback callback)
//            {
//                try {
//                    ByteBuffer byteBuffer = frame.getData();
//
////                    byteBuffer.flip();
//                    int length = byteBuffer.limit() - byteBuffer.position();
//                    for (int i = 0; i < length; i++) {
//                        outputStream.write(byteBuffer.get());
//                    }
//                    boolean isEnd = frame.isEndStream();
//                    if (isEnd) {
//                        String content = new String(outputStream.toByteArray(), StandardCharsets.UTF_8);
//                        log.info("content: {}", content);
//                    } else {
//                        log.info("received {} bytes response, but not end.", length);
//                    }
//                } catch (Exception e) {
//                    log.error(e.getMessage(), e);
//                } finally {
//                    callback.succeeded();
//                }
//            }
//        };
//
//        session.newStream(frame, new Promise.Adapter<>(), listener);

        Stream.Listener.Adapter listener = new Stream.Listener.Adapter() {
            @Override
            public void onHeaders(Stream stream, HeadersFrame frame) {
                try {
                    log.info("expect true: {} - stream.getId() > 0", stream.getId() > 0);
                    log.info("expect equals: {} - stream.getId(), frame.getStreamId()", stream.getId() == frame.getStreamId());
                    log.info("expect true: {} - frame.getMetaData().isResponse()", frame.getMetaData().isResponse());
                    MetaData.Response response = (MetaData.Response) frame.getMetaData();
                    log.info("expect 200: {} - response.getStatus()", response.getStatus());
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }

            @Override
            public void onData(Stream stream, DataFrame frame, Callback callback) {
                try {
                    ByteBuffer byteBuffer = frame.getData();

                    int length = byteBuffer.limit() - byteBuffer.position();
                    for (int i = 0; i < length; i++) {
                        outputStream.write(byteBuffer.get());
                    }
                    boolean isEnd = frame.isEndStream();
                    if (isEnd) {
                        byte[] bytes = outputStream.toByteArray();
                        String content = new String(bytes, StandardCharsets.UTF_8);
                        log.info("content: {}", content);
                        latch.countDown();
                    } else {
                        log.info("received {} bytes response, but not end.", length);
                    }
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                } finally {
                    callback.succeeded();
                }
            }
        };
        session.newStream(frame, new Promise.Adapter<>(), listener);
        latch.await(10, TimeUnit.SECONDS);
        log.info("end");
        http2Client.stop();
    }

}

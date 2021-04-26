//package org.sample.jetty.client.http2.feign;
//
//import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
//import org.eclipse.jetty.http.MetaData;
//import org.eclipse.jetty.http2.api.Stream;
//import org.eclipse.jetty.http2.frames.DataFrame;
//import org.eclipse.jetty.http2.frames.HeadersFrame;
//import org.eclipse.jetty.util.Callback;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.io.ByteArrayOutputStream;
//import java.nio.ByteBuffer;
//import java.nio.charset.StandardCharsets;
//
///**
// * jetty-gzip-decode
// *
// * @author Thomas Li
// * Date: 2021/4/26
// */
//public class ResponseListener extends Stream.Listener.Adapter {
//    private static final Logger log = LoggerFactory.getLogger(ResponseListener.class);
//    private int connectionId = 0;
//    private ByteArrayOutputStream outputStream;
//
//    @Override
//    public void onHeaders(Stream stream, HeadersFrame frame) {
//        try {
//            log.debug("Connection[{}] Stream[{}]", stream.getId());
//            log.info("expect true: {} - frame.getMetaData().isResponse()", frame.getMetaData().isResponse());
//            MetaData.Response response = (MetaData.Response) frame.getMetaData();
//            log.info("expect 200: {} - response.getStatus()", response.getStatus());
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//        }
//    }
//
//    @Override
//    public void onData(Stream stream, DataFrame frame, Callback callback) {
//        try {
//            ByteBuffer byteBuffer = frame.getData();
//
//            int length = byteBuffer.limit() - byteBuffer.position();
//            for (int i = 0; i < length; i++) {
//                outputStream.write(byteBuffer.get());
//            }
//            boolean isEnd = frame.isEndStream();
//            if (isEnd) {
//                byte[] bytes = outputStream.toByteArray();
//                String content = new String(bytes, StandardCharsets.UTF_8);
//                log.info("content: {}", content);
//                latch.countDown();
//            } else {
//                log.info("received {} bytes response, but not end.", length);
//            }
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//        } finally {
//            callback.succeeded();
//        }
//    }
//}

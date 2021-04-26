package org.sample.jetty.client.http2.feign;

import org.eclipse.jetty.http2.FlowControlStrategy;
import org.eclipse.jetty.http2.client.HTTP2Client;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * jetty-gzip-decode
 *
 * @author Thomas Li
 * Date: 2021/4/26
 */
public class JettyClient {
    private HTTP2Client http2Client = new HTTP2Client();
    private ConcurrentMap<Origin, Destination> destinations = new ConcurrentHashMap<>();


    public void start() {
        QueuedThreadPool clientExecutor = new QueuedThreadPool();
        clientExecutor.setName("client");
        http2Client.setExecutor(clientExecutor);
        http2Client.setInitialSessionRecvWindow(FlowControlStrategy.DEFAULT_WINDOW_SIZE);
        http2Client.setInitialStreamRecvWindow(FlowControlStrategy.DEFAULT_WINDOW_SIZE);
        try {
            http2Client.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void stop() {
        try {
            http2Client.stop();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

//    public void
}

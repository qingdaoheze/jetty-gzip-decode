package org.sample.jetty.client.http2;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.HttpClientTransport;
import org.eclipse.jetty.client.api.ContentProvider;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.util.StringContentProvider;
import org.eclipse.jetty.http.HttpFields;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.http.HttpVersion;
import org.eclipse.jetty.http2.client.HTTP2Client;
import org.eclipse.jetty.http2.client.http.HttpClientTransportOverHTTP2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * Date: 2021/4/1
 */
public class HttpClientWithHttp2Transport {
    private static final Logger log = LoggerFactory.getLogger(HttpClientWithHttp2Transport.class);
    public static void main(String[] args) throws Exception {
        postHttp2("http://127.0.0.1:8081/tomcat/post", "{ \"age\": 0, \"name\": \"string\"}");
//        postHttp2("http://127.0.0.1:8081/tomcat/post1", "");

    }

    private static void postHttp2(String url, String content) throws Exception {

        HTTP2Client http2Client = new HTTP2Client();
        http2Client.setSelectors(1);
        HttpClientTransport transport = new HttpClientTransportOverHTTP2(http2Client);

        HttpClient httpClient = new HttpClient(transport, null);
        httpClient.setMaxConnectionsPerDestination(2);
        httpClient.setMaxRequestsQueuedPerDestination(2);
        httpClient.setConnectTimeout(1000);
        httpClient.setIdleTimeout(30000);
        httpClient.setFollowRedirects(true);
        httpClient.start();

        try {
            ContentProvider contentProvider = new StringContentProvider("application/json", content, StandardCharsets.UTF_8);
            Request request = httpClient.POST(url).content(contentProvider, "application/json");
            request.timeout(6, TimeUnit.SECONDS);
            request.idleTimeout(3, TimeUnit.SECONDS);
            ContentResponse contentResponse = request.send();
            int status = contentResponse.getStatus();
            log.info("status:{}", status);
            HttpVersion version = contentResponse.getVersion();
            log.info("version: {}", version);
            System.out.println(version);
            String contentAsString = contentResponse.getContentAsString();
            System.out.println(contentAsString);
            log.info("response:{}", contentAsString);
            String contentEncoding = contentResponse.getHeaders().get(HttpHeader.CONTENT_ENCODING);
            log.info("contentEncoding:{}", contentEncoding);
        } finally {
            httpClient.stop();
        }

    }

    private static void postHttp1(String url, String content) throws Exception {

        HttpClient httpClient = new HttpClient();
        httpClient.setFollowRedirects(true);
        httpClient.start();

        ContentProvider contentProvider = new StringContentProvider("application/json", content, StandardCharsets.UTF_8);
        Request request = httpClient.POST(url).content(contentProvider, "application/json");

        // 用于与服务端协商，如果支持协议升级，会返回响应头101，然后再使用h2c的协议发起请求
        request.header(HttpHeader.UPGRADE, "h2c");
        request.header(HttpHeader.HTTP2_SETTINGS, "");
        request.header(HttpHeader.CONNECTION, "Upgrade, HTTP2-Settings");

        ContentResponse contentResponse = request.send();
        int status = contentResponse.getStatus();
        HttpFields headers = contentResponse.getHeaders();
        String connection = headers.get(HttpHeader.CONNECTION);
        String upgrade = headers.get(HttpHeader.UPGRADE);
        // https://blog.csdn.net/u011904605/article/details/53033192
        // 协议升级协商结果：status:101, connection:Upgrade, upgrade:h2c
        log.info("协议升级协商结果：status:{}, connection:{}, upgrade:{}", status, connection, upgrade);
        HttpVersion version = contentResponse.getVersion();
        log.info("version: {}", version);
        System.out.println(version);
        String contentAsString = contentResponse.getContentAsString();
        System.out.println(contentAsString);
        log.info("response:{}", contentAsString);

        httpClient.stop();
    }

//    private static void get() throws Exception {
//        HttpClientTransport transport = new HttpClientTransportDynamic();
//        HttpClient httpClient = new HttpClient(transport);
//        httpClient.setFollowRedirects(true);
//        httpClient.start();
//
//        ContentResponse contentResponse = httpClient.GET("http://127.0.0.1:8082/tomcat/get");
//        HttpVersion version = contentResponse.getVersion();
//        System.out.println(version);
//        String contentAsString = contentResponse.getContentAsString();
//        System.out.println(contentAsString);
//
//        httpClient.stop();
//    }
}

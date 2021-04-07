package sample.okhttp.h2c;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 */
public class Http2ClearTextSample {
    private static final Logger log = LoggerFactory.getLogger(Http2ClearTextSample.class);
    public static void main(String[] args) throws IOException {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        OkHttpClient okHttpClient = builder.protocols(Arrays.asList(Protocol.H2_PRIOR_KNOWLEDGE))
                .connectTimeout(1, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .pingInterval(200, TimeUnit.MILLISECONDS)
                .build();
        Request request = new Request.Builder()
                .url("http://127.0.0.1:8081/tomcat/post")
                // okhttp3.internal.http.BridgeInterceptor.intercept
                // don't add this header, otherwise this interceptor will not auto process the gzip response.
//                .header("Accept-Encoding", "gzip")
                .post(RequestBody.create(MediaType.parse("application/json"), "{ \"age\": 0, \"name\": \"string\"}"))
                .build();
        Response response = okHttpClient.newCall(request).execute();
        Protocol protocol = response.protocol();
        log.info("protocol: {}", protocol);
        String body = response.body().string();
        log.info("body: {}", body);
    }
}

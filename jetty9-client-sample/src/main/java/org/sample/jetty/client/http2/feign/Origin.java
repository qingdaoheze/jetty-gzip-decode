package org.sample.jetty.client.http2.feign;

import java.util.Objects;

/**
 * jetty-gzip-decode
 *
 * @author Thomas Li
 * Date: 2021/4/26
 */
public class Origin {
    private String host;
    private int port;

    public Origin(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Origin origin = (Origin) object;
        return port == origin.port &&
                host.equals(origin.host);
    }

    @Override
    public int hashCode() {
        return Objects.hash(host, port);
    }
}

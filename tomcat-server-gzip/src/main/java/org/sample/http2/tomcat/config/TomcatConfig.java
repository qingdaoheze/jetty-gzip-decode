package org.sample.http2.tomcat.config;//package org.sample.http2.tomcat.config;

import org.apache.catalina.connector.Connector;
import org.apache.coyote.http2.Http2Protocol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * springboot-tomcat
 *
 * Date: 2021/4/1
 */
@Configuration
public class TomcatConfig {
    @Autowired
    private ServerProperties serverProperties;

    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> tomcatCustomizer() {
        return tomcatServletWebServerFactory -> {
            TomcatConnectorCustomizer http2Customizer = connector -> {
                Http2Protocol http2Protocol = new Http2Protocol();
                boolean gzipEnable = false;
                if (serverProperties != null && serverProperties.getCompression() != null && serverProperties.getCompression().getEnabled()) {
                    gzipEnable = true;
                }
                http2Protocol.setCompression(gzipEnable ? "on" : "off");
                http2Protocol.setKeepAliveTimeout(60000);
                connector.addUpgradeProtocol(http2Protocol);
            };
            tomcatServletWebServerFactory.addConnectorCustomizers(http2Customizer);
        };
    }
}

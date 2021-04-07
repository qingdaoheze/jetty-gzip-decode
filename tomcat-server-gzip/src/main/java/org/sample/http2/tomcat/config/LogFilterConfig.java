package org.sample.http2.tomcat.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

/**
 * jetty-gzip-decode
 *
 * Date: 2021/4/7
 */
@Configuration
public class LogFilterConfig {
    private static final Logger log = LoggerFactory.getLogger(LogFilterConfig.class);
    @Bean
    public FilterRegistrationBean<LogFilter> logFilter() {
        FilterRegistrationBean<LogFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new LogFilter());
        filterRegistrationBean.setUrlPatterns(Arrays.asList("/*"));
        return filterRegistrationBean;
    }

    public static class LogFilter implements Filter {

        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            String acceptEncoding = httpRequest.getHeader(HttpHeaders.ACCEPT_ENCODING);
            String requestURI = httpRequest.getRequestURI();
            String protocol = httpRequest.getProtocol();
            chain.doFilter(request, response);
            String contentEncoding = ((HttpServletResponse) response).getHeader(HttpHeaders.CONTENT_ENCODING);
            log.info("requestURI:{}, protocol:{}, acceptEncoding: {}, contentEncoding:{}", requestURI, protocol, acceptEncoding, contentEncoding);
        }
    }
}

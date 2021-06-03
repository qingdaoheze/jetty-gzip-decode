# HTTP/2 max local stream count exceeded
单个TCP连接会被多个HTTP2的连接复用，但为了收益最大化，会限制单个TCP连接同时服用的HTTP2连接数量，这个bug是jetty在计算这个状态的时候，出现了错误。

https://github.com/eclipse/jetty.project/issues/6208


# Response timeout when an h2c request was sent and the response's content-encoding is gzip
使用jetty发送http2请求到tomcat，如果响应结果为gzip，则结果服务正常返回，直至超时。将tomcat版本从9.0.21升级到了9.0.45后，问题解决。


https://github.com/eclipse/jetty.project/issues/6141

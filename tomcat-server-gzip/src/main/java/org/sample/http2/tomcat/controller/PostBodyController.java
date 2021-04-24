package org.sample.http2.tomcat.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * springboot-tomcat
 *
 * Date: 2021/4/1
 */
@RestController
public class PostBodyController {
    @PostMapping("/tomcat/post")
    public String post(@RequestBody PostBody body) {
        StringBuilder stringBuilder = new StringBuilder();
        int size = body.getSize();
        size = size > 0 ? size : 20;
        for (int i = 0; i < size; i++) {
            stringBuilder.append("name=").append(body.getName() + i).append(",");
            stringBuilder.append("age=").append(body.getAge() + i).append(",");
        }
        return stringBuilder.toString();
    }
}

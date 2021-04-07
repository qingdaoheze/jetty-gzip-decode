package org.sample.http2.tomcat.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

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
        for (int i = 0; i < 200; i++) {
            stringBuilder.append("name=").append(body.getName() + i).append(",");
            stringBuilder.append("age=").append(body.getAge() + i).append(",");
        }
        return stringBuilder.toString();
    }

    @PostMapping("/tomcat/post1")
    public Map<String, String> post1() {
        Map<String, String> result = new HashMap<>();
        for (int i = 0; i < 200; i++) {
            result.put("name" + i, "value" + i);
        }
        return result;
    }
}

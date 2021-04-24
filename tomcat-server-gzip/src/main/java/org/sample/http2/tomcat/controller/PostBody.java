package org.sample.http2.tomcat.controller;

/**
 * springboot-tomcat
 *
 * Date: 2021/4/1
 */
public class PostBody {
    private String name;
    private Integer age;
    private int size = 20;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}

package com.example.administrator.myapplication;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2016/12/13.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
//泛类
public class Page <T>{
    List<T> content;
    Integer number;

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }
}

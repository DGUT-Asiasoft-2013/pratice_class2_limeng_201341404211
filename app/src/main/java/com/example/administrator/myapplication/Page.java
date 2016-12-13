package com.example.administrator.myapplication;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2016/12/13.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Page <Article>{
    List<Article> content;
    Integer number;

    public List<Article> getContent() {
        return content;
    }

    public void setContent(List<Article> content) {
        this.content = content;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }
}

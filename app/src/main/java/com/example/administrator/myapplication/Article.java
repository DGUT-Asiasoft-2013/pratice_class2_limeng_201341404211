package com.example.administrator.myapplication;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2016/12/13.
 */

public class Article implements Serializable {
    Integer id;
    Date creataDate;
    Date editDate;
    String title;
    String text;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getCreateDate() {
        return creataDate;
    }

    public void setCreateDate(Date createDate) {
        this.creataDate = createDate;
    }

    public Date getEditDate() {
        return editDate;
    }

    public void setEditDate(Date editDate) {
        this.editDate = editDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorAvater() {
        return authorAvater;
    }

    public void setAuthorAvater(String authorAvater) {
        this.authorAvater = authorAvater;
    }

    String authorName;
    String authorAvater;
}

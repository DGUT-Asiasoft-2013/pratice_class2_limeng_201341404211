package com.example.administrator.myapplication;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2016/12/14.
 */

public class Review implements Serializable {
    int id;
    Article article;
    User author;
    String textReview;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    Date creataDate;
    Date editDate;

    public String getAuthorName() {
        //数据库中的数据是User user；所以在获取人名的时候用return author.getName();而不是直接返回return AuthorName;
        return author.getName();
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    String authorName;


    public Date getCreataDate() {
        return creataDate;
    }

    public void setCreataDate(Date creataDate) {
        this.creataDate = creataDate;
    }

    public Date getEditDate() {
        return editDate;
    }

    public void setEditDate(Date editDate) {
        this.editDate = editDate;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getTextReview() {
        return textReview;
    }

    public void setTextReview(String textReview) {
        this.textReview = textReview;
    }
}

package com.example.a15632.poetrydemo.Entity;

import java.io.Serializable;

public class Poetry implements Serializable {
    public Poetry(String name,String author,String content){
        this.name=name;
        this.author=author;
        this.content=content;
    }

<<<<<<< HEAD
    private int id;
    private int authorId;
=======
>>>>>>> f5f9d82d1c020d387ffc50b24bcf78cc4f0d78e0
    private String name;
    private String author;
    private String content;
    private String dynasty;//朝代
    private String translate;//译文






<<<<<<< HEAD
    public Poetry(){

    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public Poetry(int id, int authorId, String name, String author, String content, String dynasty) {
        this.id = id;
        this.authorId = authorId;

        this.name = name;
        this.author = author;
        this.content = content;
        this.dynasty = dynasty;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public Poetry(String name, String author, String content, String dynasty) {
=======
    public Poetry(String name, String author, String content, String translate) {
>>>>>>> f5f9d82d1c020d387ffc50b24bcf78cc4f0d78e0
        this.name = name;
        this.author = author;
        this.content = content;
        this.translate = translate;
    }

    public String getDynasty() {
        return dynasty;
    }

    public void setDynasty(String dynasty) {
        this.dynasty = dynasty;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTranslate() {
        return translate;
    }

    public void setTranslate(String translate) {
        this.translate = translate;
    }
}

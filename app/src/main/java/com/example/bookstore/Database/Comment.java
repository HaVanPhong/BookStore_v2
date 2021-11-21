package com.example.bookstore.Database;

public class Comment {
    private String username;
    private String time;
    private String content;
    private String imageLink;

    public Comment(String username, String time, String content, String imageLink) {
        this.username = username;
        this.time = time;
        this.content = content;
        this.imageLink = imageLink;
    }

    public Comment() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }
}

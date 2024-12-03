package com.ndurance.mobileapp.model.dto;

public class Comment {
    private Long id;
    private String comment;
    private String userId;

    public Comment(String comment, String pic, String email) {
        this.comment = comment;
        this.pic = pic;
        this.email = email;
    }

    private String email;
    private String pic;

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", comment='" + comment + '\'' +
                ", userId='" + userId + '\'' +
                ", email='" + email + '\'' +
                ", pic='" + pic + '\'' +
                '}';
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}

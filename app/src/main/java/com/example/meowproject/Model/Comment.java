package com.example.meowproject.Model;

public class Comment {

    private String ID, Comment, UserName, CommentDate;

    public Comment() {
    }

    public Comment(String ID, String comment, String userName, String commentDate) {
        this.ID = ID;
        Comment = comment;
        UserName = userName;
        CommentDate = commentDate;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getCommentDate() {
        return CommentDate;
    }

    public void setCommentDate(String commentDate) {
        CommentDate = commentDate;
    }
}

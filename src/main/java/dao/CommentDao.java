package dao;

import model.Comment;

import java.util.List;

public interface CommentDao {
    void insert(Comment comment);
    List<Comment> getCommentList();
}

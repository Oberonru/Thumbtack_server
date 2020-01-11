package dao;

import database.DataBase;
import model.Comment;

import java.util.List;

public class CommentDaoImpl implements CommentDao {
    private DataBase db = DataBase.getInstance();

    public void insert(Comment comment) {
        db.addComment(comment);
    }

    public List<Comment> getCommentsBySongId(int songId) {
        return db.getCommentsBySongId(songId);
    }

    public List<Comment> getCommentList() {
        return db.getCommentList();
    }
}

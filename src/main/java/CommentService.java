import com.fasterxml.jackson.databind.ObjectMapper;
import dao.CommentDaoImpl;
import database.DataBase;
import model.Comment;
import model.Song;
import model.User;
import request.CommentDtoRequest;
import response.ErrorDtoResponse;

public class CommentService {

    private ObjectMapper mapper = new ObjectMapper();
    private DataBase db = DataBase.getInstance();
    private CommentDaoImpl commentDao = new CommentDaoImpl();
    private UserService userService = new UserService();

    public String addComment(CommentDtoRequest request) throws Exception {
        if (tooLongContent(request.getContent())) {
            throw new Exception("content is too Long");
        }

        Comment comment = createComment(request.getToken(), request.getContent(),
                request.getSongId(), request.getReplyCommentId());
        if (comment == null) {
            throw new Exception("Comment is not added");
        }

        commentDao.insert(comment);
        return "{}";
    }

    private Comment createComment(String token, String content, int songId, int replyCommentId) {
        User user = userService.getUserByToken(token);
        Song song = db.findSongById(songId);
        if (user != null && song != null) {
            Comment comment = new Comment();
            comment.setId(generateCommentId());
            comment.setLogin(user.getLogin());
            comment.setContent(content);
            comment.setSongId(songId);
            comment.setReplyCommentId(replyCommentId);
            return comment;
        }
        return null;
    }

    private int generateCommentId() {
        if (db.getCommentList().size() != 0) {
            return commentDao.getCommentList().get(commentDao.getCommentList().size() - 1).getId() + 1;
        } else return 1;
    }

    private boolean tooLongContent(String content) {
        return content.length() > 30;
    }
}

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

    public String addComment(String requestJsonString) throws Exception {
        CommentDtoRequest commentRequest = mapper.readValue(requestJsonString, CommentDtoRequest.class);
        if (tooLongContent(commentRequest.getContent())) {
            ErrorDtoResponse response = new ErrorDtoResponse("content is too Long");
            return mapper.writeValueAsString(response);
        }
        Comment comment = createComment(commentRequest.getToken(), commentRequest.getContent(),
                commentRequest.getSongId(), commentRequest.getReplyCommentId());
        ErrorDtoResponse response = new ErrorDtoResponse("Comment is not added");
        if (comment != null) {
            commentDao.insert(comment);
            return "{}";
        }
        else return mapper.writeValueAsString(response);
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
        }
        else return 1;
    }
    private boolean tooLongContent(String content) {
        return content.length() > 30;
    }
}

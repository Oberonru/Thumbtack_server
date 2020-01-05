import com.fasterxml.jackson.databind.ObjectMapper;
import database.DataBase;
import model.Comment;
import model.User;
import request.CommentDtoRequest;

public class CommentService {

    private ObjectMapper mapper = new ObjectMapper();
    private DataBase db = DataBase.getInstance();

    public String addComment(String requestJsonString) throws Exception {
        CommentDtoRequest commentRequest = mapper.readValue(requestJsonString, CommentDtoRequest.class);

        Comment comment = createComment(commentRequest.getToken(), commentRequest.getContent(),
                commentRequest.getSongId());
        if (comment != null) {
            db.addComment(comment);
            return "{}";
        } else return "\"error\" : \"Comment is not added\"";
    }

    private Comment createComment(String token, String content, int songId) {
        User user = db.getUserByToken(token);
        if (user != null) {
            Comment comment = new Comment();
            comment.setLogin(user.getLogin());
            comment.setContent(content);
            comment.setSongId(songId);
            return comment;
        }
        return null;
    }
}

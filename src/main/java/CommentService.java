import dao.CommentDaoImpl;
import dao.SongDaoImpl;
import model.Comment;
import model.Song;
import model.User;
import request.CommentDtoRequest;

public class CommentService {

    private CommentDaoImpl commentDao = new CommentDaoImpl();
    private SongDaoImpl songDao = new SongDaoImpl();
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
        Song song = songDao.findSongById(songId);
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
        if (commentDao.getCommentList().size() != 0) {
            return commentDao.getCommentList().get(commentDao.getCommentList().size() - 1).getId() + 1;
        } else return 1;
    }

    private boolean tooLongContent(String content) {
        return content.length() > 30;
    }
}

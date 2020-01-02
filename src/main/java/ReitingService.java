import com.fasterxml.jackson.databind.ObjectMapper;
import database.DataBase;
import model.Raiting;
import model.Song;
import model.User;
import request.DeleteSongRaitingRequest;
import request.RatingDtoRequest;

public class ReitingService {
    private UserService userService = new UserService();
    private ObjectMapper mapper = new ObjectMapper();
    private DataBase db = DataBase.getInstance();
    private SongService songService = new SongService();

    public String deleteRaiting(String requestJsonString) throws Exception {

        DeleteSongRaitingRequest deleteRequest = mapper.readValue(requestJsonString, DeleteSongRaitingRequest.class);
        User user = userService.getUserByToken(deleteRequest.getToken());

        if (user != null) {
            int rate = 0;
            for (Raiting r : db.getRaitingList()) {
                if (user.getLogin().equals(r.getLogin()) && deleteRequest.getSongId() == r.getSongId()) {
                    rate = r.getSongRaiting();
                    break;
                }
            }
            db.deleteRaiting(new Raiting(user.getLogin(), deleteRequest.getSongId(), rate));
            return "{}";
        }
        return "\"error\" : \"the operato\"";
    }
    /**
     * Радиослушатель, предложивший песню в состав концерта, считается автором этого предложения.
     * Радиослушатели могут ставить свои оценки предлагаемым в программу песням по шкале 1..5. Радиослушатели вправе
     * изменить свою оценку или вообще удалить ее в любое время. Автор предложения автоматически оценивает свое
     * предложение оценкой “5” и не вправе ни изменить, ни удалить свою оценку.
     * Радиослушатели, сделавшие свое предложение, могут отменить его. Если на момент отмены предложение не получило
     * никаких оценок от других радиослушателей, оно удаляется.
     */

    public String addRaiting(String requestJsonString) throws Exception {
        RatingDtoRequest ratingRequest = mapper.readValue(requestJsonString, RatingDtoRequest.class);
        if (!verifyRating(ratingRequest.getSongRaiting())) {
            return "{\"error\" : \"raiting is not valid\"}";
        }
        User user = userService.getUserByLogin(ratingRequest.getLogin());
        if (user == null) {
            return "{\"error\" : \"User not found\"}";
        }
        Song song = songService.findSongById(ratingRequest.getSongId());
        if (song != null) {
            db.updateRaiting(new Raiting(user.getLogin(), song.getSongId(), ratingRequest.getSongRaiting()));
            return "{}";
        }
        return "{\"error\" : \"the operation cannot be performed\"}";
    }

    public boolean verifyRating(int raiting) {
        return raiting > 0 && raiting < 6;
    }






}

import com.fasterxml.jackson.databind.ObjectMapper;
import database.DataBase;
import model.Rating;
import model.Song;
import model.User;
import request.DeleteSongDtoRatingRequest;
import request.RatingDtoRequest;
import response.ErrorDtoResponse;

public class RaitingService {
    private UserService userService = new UserService();
    private ObjectMapper mapper = new ObjectMapper();
    private DataBase db = DataBase.getInstance();

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
        if (!verifyRating(ratingRequest.getSongRating())) {
            ErrorDtoResponse response = new ErrorDtoResponse("Rating not valid");
            return mapper.writeValueAsString(response);
        }
        User user = userService.getUserByLogin(ratingRequest.getLogin());
        if (user == null) {
            ErrorDtoResponse response = new ErrorDtoResponse("User not found");
            return mapper.writeValueAsString(response);
        }
        Song song = db.findSongById(ratingRequest.getSongId());
        if (song == null) {
            ErrorDtoResponse response = new ErrorDtoResponse("Song not found");
            return mapper.writeValueAsString(response);
        }
        db.updateRaiting(new Rating(user.getLogin(), song.getSongId(), ratingRequest.getSongRating()));
        return "{}";
    }

    public boolean verifyRating(int raiting) {
        return raiting > 0 && raiting < 6;
    }

    public String deleteRating(String requestJsonString) throws Exception {
        DeleteSongDtoRatingRequest deleteRequest = mapper.readValue(requestJsonString, DeleteSongDtoRatingRequest.class);

        User user = userService.getUserByToken(deleteRequest.getToken());
        Song song = db.findSongById(deleteRequest.getSongId());
        if (song == null) {
            return "\"error\" : \"Song not found\"";
        }
        //todo: найти по songId песню
        if (user != null) {
            db.deleteRaiting(new Rating(user.getLogin(), deleteRequest.getSongId()));
            return "{}";
        }
        return "\"error\" : \"user not found\"";
    }
}

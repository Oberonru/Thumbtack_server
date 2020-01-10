import com.fasterxml.jackson.databind.ObjectMapper;
import database.DataBase;
import request.*;
import response.ErrorDtoResponse;
import response.RegisterUserDtoResponse;

public class Server {

    private UserService userService = new UserService();
    private SongService songService = new SongService();
    private RaitingService raitingService = new RaitingService();
    private CommentService commentService = new CommentService();
    private DataBase db = DataBase.getInstance();
    private boolean isStarted;
    private ObjectMapper mapper = new ObjectMapper();

    /**
     * Производит всю необходимую инициализацию и запускает сервер.
     * savedDataFileName - имя файла, в котором было сохранено состояние сервера.  Если savedDataFileName == null,
     * восстановление состояния не производится, сервер стартует “с нуля”.
     */

    public void startServer(String savedDataFileName) {
        db.loadDataToCache(savedDataFileName);
        isStarted = true;
    }

    /**
     * Останавливает сервер и записывает все его содержимое в файл сохранения с именем savedDataFileName.
     * Если savedDataFileName == null, запись содержимого не производится.
     */
    public void stopServer(String savedDataFileName) throws Exception {
        isStarted = false;
        db.saveData(savedDataFileName);
    }

    public String registerUser(String registerUserJson) throws Exception {
        RegisterUserDtoRequest request;

        if (isStarted) {
            try {
                request = mapper.readValue(registerUserJson, RegisterUserDtoRequest.class);
            } catch (Exception e) {
                return mapper.writeValueAsString(new ErrorDtoResponse("Request is not valid"));
            }

            try {
                RegisterUserDtoResponse response = new RegisterUserDtoResponse();
                String token = userService.registerUser(request);
                response.setToken(token);
                return mapper.writeValueAsString(response);
            } catch (Exception e) {
                return mapper.writeValueAsString(new ErrorDtoResponse(e.getMessage()));
            }
        }

        return mapper.writeValueAsString(new ErrorDtoResponse("Server is not started!"));
    }

    public String logIn(String requestJsonString) throws Exception {
        LogInDtoRequest request;

        try {
            request = mapper.readValue(requestJsonString, LogInDtoRequest.class);
        } catch (Exception e) {
            return mapper.writeValueAsString(new ErrorDtoResponse(e.getMessage()));
        }

        try {
            //todo: по идее здесь всегда валидное значение, как проверить блок catch, как в него передать невалидный токен?
            String token = userService.logIn(request);
            return mapper.writeValueAsString(token);
        } catch (Exception e) {
            return mapper.writeValueAsString(new ErrorDtoResponse(e.getMessage()));
        }
    }

    public String logOut(String requestJsonString) throws Exception {
        LogOutDtoRequest request;

        try {
            request = mapper.readValue(requestJsonString, LogOutDtoRequest.class);
        } catch (Exception e) {
            return mapper.writeValueAsString(new ErrorDtoResponse(e.getMessage()));
        }

        try {
            String response = userService.logOut(request);
            return mapper.writeValueAsString(response);
        } catch (Exception e) {
            return mapper.writeValueAsString(new ErrorDtoResponse(e.getMessage()));
        }
    }

    public String addSong(String requestJsonString) throws Exception {
        RegisterSongDtoRequest request;

        try {
            request = mapper.readValue(requestJsonString, RegisterSongDtoRequest.class);
        } catch (Exception e) {
            return mapper.writeValueAsString(new ErrorDtoResponse(e.getMessage()));
        }

        try {
            String response = songService.addSong(request);
            return mapper.writeValueAsString(response);
        } catch (Exception e) {
            return mapper.writeValueAsString(new ErrorDtoResponse(e.getMessage()));
        }

    }

    public String deleteSong(String requestJsonString) throws Exception {
        DeleteSongDtoRequest request;

        try {
            request = mapper.readValue(requestJsonString, DeleteSongDtoRequest.class);
        } catch (Exception e) {
            return mapper.writeValueAsString(new ErrorDtoResponse(e.getMessage()));
        }

        try {
            String response = songService.deleteSong(request);
            return mapper.writeValueAsString(response);
        } catch (Exception e) {
            return mapper.writeValueAsString(new ErrorDtoResponse(e.getMessage()));
        }
    }

    public String addRating(String requestJsonString) throws Exception {
        RatingDtoRequest request;

        try {
            request = mapper.readValue(requestJsonString, RatingDtoRequest.class);
        } catch (Exception e) {
            return mapper.writeValueAsString(new ErrorDtoResponse(e.getMessage()));
        }

        try {
            String response = raitingService.addRaiting(request);
            return mapper.writeValueAsString(response);
        } catch (Exception e) {
            return mapper.writeValueAsString(new ErrorDtoResponse(e.getMessage()));
        }


    }

    public String deleteRating(String requestJsonString) throws Exception {
        return raitingService.deleteRating(requestJsonString);
    }

    public String addComment(String requestJsonString) throws Exception {
        return commentService.addComment(requestJsonString);
    }

    public String getSongs(String requestJsonString) throws Exception {
        return songService.getSongs(requestJsonString);
    }

    public String getSongByComposers(String requestJsonString) throws Exception {
        return songService.findSongByComposer(requestJsonString);
    }
}

import com.fasterxml.jackson.databind.ObjectMapper;
import database.DataBase;
import request.LogInDtoRequest;
import request.RegisterUserDtoRequest;
import response.ErrorDtoResponse;

import java.io.IOException;

/**
 * * Будет лучше, если класс Server сам никакие операции выполнять не будет, а будет делегировать их выполнение
 * * соответствующему классу, который мы назовем классом сервиса (например, UserService, SongService и т.д), вызывая
 * * соответствующий метод из этого класса. Экземпляры этих классов должны быть полями класса Server.
 * * <p>
 * * Итак, класс Server передает полученный json соответствующему классу сервиса. Этот класс должен распарсить json,
 * * проанализировать и проверить находящийся в нем запрос, выполнить его (конечно, если в запросе нет ошибок) и вернуть
 * * ответ. Если в запросе имеются ошибки, класс сервиса должен тут же вернуть ответ с полем “error”, не производя
 * * дальнейшей обработки.
 */
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
     *
     * @param savedDataFileName
     */

    public void startServer(String savedDataFileName) {
        db.loadDataToCache(savedDataFileName);
        isStarted = true;
    }

    /**
     * Останавливает сервер и записывает все его содержимое в файл сохранения с именем savedDataFileName.
     * Если savedDataFileName == null, запись содержимого не производится.
     *
     * @param savedDataFileName
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
                String token = userService.registerUser(request);
                return mapper.writeValueAsString(token);
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

    public String logOut(String requestJsonString) throws IOException {
        return userService.logOut(requestJsonString);
    }

    public String addSong(String requestJsonString) throws Exception {
        return songService.addSong(requestJsonString);
    }

    public String deleteSong(String requestJsonString) throws Exception {
        return songService.deleteSong(requestJsonString);
    }

    public String addRating(String requestJsonString) throws Exception {
        return raitingService.addRaiting(requestJsonString);
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

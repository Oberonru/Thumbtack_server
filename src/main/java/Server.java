import com.fasterxml.jackson.databind.ObjectMapper;
import database.DataBase;
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
    private DataBase db = DataBase.getInstance();
    private ErrorDtoResponse errorDtoResponse = new ErrorDtoResponse();
    private boolean isStarted;
    private boolean isLogined;

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
        db.saveData();
    }

    public String registerUser(String registerUserJson) throws Exception {
        if (isStarted) {
            String response = userService.registerUser(registerUserJson);
            return response;
        }
        ObjectMapper mapper = new ObjectMapper();
        errorDtoResponse.error = "Server is not started!";
        return mapper.writeValueAsString(errorDtoResponse);
    }

    public String logIn(String requestJsonString) throws IOException {
        return userService.logIn(requestJsonString);
    }

    public String logOut(String requestJsonString) throws IOException {
        isLogined = false;
        return  userService.logOut(requestJsonString);
    }

    public String addSong(String requestJsonString) throws Exception {
        SongService songService = new SongService();
        return songService.addSong(requestJsonString);
    }

    public static void main(String[] args) throws Exception {
    }
}

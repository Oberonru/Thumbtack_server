import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Song;
import model.User;
import request.RegisterSongDtoRequest;
import request.RegisterUserDtoRequest;
import response.RegisterSongDtoResponse;

import java.io.File;
import java.io.IOException;

/**
 * * Будет лучше, если класс Server сам никакие операции выполнять не будет, а будет делегировать их выполнение
 *  * соответствующему классу, который мы назовем классом сервиса (например, UserService, SongService и т.д), вызывая
 *  * соответствующий метод из этого класса. Экземпляры этих классов должны быть полями класса Server.
 *  * <p>
 *  * Итак, класс Server передает полученный json соответствующему классу сервиса. Этот класс должен распарсить json,
 *  * проанализировать и проверить находящийся в нем запрос, выполнить его (конечно, если в запросе нет ошибок) и вернуть
 *  * ответ. Если в запросе имеются ошибки, класс сервиса должен тут же вернуть ответ с полем “error”, не производя
 *  * дальнейшей обработки.
 */
public class Server {

    private static UserService userService = new UserService();
    private static SongService songService = new SongService();
    private static boolean isStarted;
    private ObjectMapper mapper = new ObjectMapper();
    private String token;

    public static void main(String[] args) throws Exception {

    }

    /**
     * Производит всю необходимую инициализацию и запускает сервер.
     * savedDataFileName - имя файла, в котором было сохранено состояние сервера.  Если savedDataFileName == null,
     * восстановление состояния не производится, сервер стартует “с нуля”.
     *
     * @param savedDataFileName
     */
    public void startServer(String savedDataFileName) throws Exception {

    }

    /**
     * Останавливает сервер и записывает все его содержимое в файл сохранения с именем savedDataFileName.
     * Если savedDataFileName == null, запись содержимого не производится.
     *
     * @param savedDataFileName
     */
    public void stopServer(String savedDataFileName) throws Exception {
        mapper.writeValue(new File(savedDataFileName), Server.class);
    }

    /**
     * Регистрирует радиослушателя на сервере. requestJsonString содержит данные о радиослушателе, необходимые для
     * регистрации.  Метод при успешном выполнении возвращает json с единственным элементом “token”. Если же команду по
     * какой-то причине выполнить нельзя, возвращает json с элементом “error”
     * При регистрации (метод registerUser) радиослушателя возвращаемая строка (при успешном выполнении)
     * должна обязательно содержать поле “token” - уникальный номер, присвоенный этому радиослушателю
     * в результате регистрации.
     *
     * @param requestJsonString
     * @return
     * @throws Exception
     */

    public String registerUser(String requestJsonString) throws Exception {
        /**
         *..... Итак, мы создали экземпляр класса модели. В этом экземпляре данные корректные в соответствии с
         * нашими требованиям, так как мы создавали его только в том случае, если экземпляр класса запроса прошел
         * проверку.
         * Те перед созданием экземпляра класса "модели" должна быть проверка на валидность данных?
         */
        RegisterUserDtoRequest request = mapper.readValue(requestJsonString, RegisterUserDtoRequest.class);

        //todo: в условии говорится, что класс сервиса может проверять на валидность данных для регистрации
        if (!request.verifyName(request.getFirstName())) {
            return "{error}";
        }
        User registredUser = userService.
                createUser(request.getFirstName(), request.getLastName(), request.getLogin(), request.getPassword());
        //todo:токен уже должен быть в request? и  у него нужно вызывать метод genToken?
        token = "{\"token\":" + "\"" + registredUser.getToken() + "\"}";
        return token;
    }

    /**
     * Радиослушатель добавляет новую песню на сервер. requestJsonString содержит описание песни и token, полученный
     * как результат выполнения команды регистрации радиослушателя. Метод при успешном выполнении возвращает пустой json
     * Если же команду почему-то выполнить нельзя, возвращает json с элементом “error”
     *
     * @param requestJsonString
     * @return
     */
    public String addSong(String requestJsonString) throws Exception {
        RegisterSongDtoRequest songRequest = mapper.readValue(requestJsonString, RegisterSongDtoRequest.class);
        //создаю экземпляр ответа...в нём уже должна быть кра жсон строка с нужными данными?
        RegisterSongDtoResponse songResponse = new RegisterSongDtoResponse();
        if (userService.validateToken(songRequest.getToken())) {
            Song createdSong = songService.createSong(songRequest.getSongName(), songRequest.getComposer(),
                    songRequest.getAuthor(), songRequest.getMusician(), songRequest.getSongDuration());
        }
        songResponse.setEmptyResponse("{}");
        return songResponse.getEmptyResponse();
    }

    //это мой метод, для того чтобы записать тестовую жсон строку в файл
    private static void saveToFile(Object obj, File fileName) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(fileName, obj);
        } catch (JsonGenerationException ge) {
            ge.printStackTrace();
        } catch (JsonMappingException jme) {
            jme.printStackTrace();
        } catch (IOException e) {
        }
    }



}

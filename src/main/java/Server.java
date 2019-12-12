import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import database.DataBase;
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

    private String token;

    public static void main(String[] args) throws Exception {
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

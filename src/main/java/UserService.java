import com.fasterxml.jackson.databind.ObjectMapper;
import dao.UserDaoImpl;
import database.DataBase;
import model.User;
import request.RegisterUserDtoRequest;

import java.util.List;
import java.util.UUID;

/**
 * Будет лучше, если сам сервер ниекаких операций производить не будет, а будет делегировать из
 * соответствующему сервису
 */
public class UserService {

    private UserDaoImpl userDao = new UserDaoImpl();
    private DataBase db = new DataBase();
    private ObjectMapper mapper = new ObjectMapper();

    /**
     * Производит всю необходимую инициализацию и запускает сервер.
     * savedDataFileName - имя файла, в котором было сохранено состояние сервера.  Если savedDataFileName == null,
     * восстановление состояния не производится, сервер стартует “с нуля”.
     *
     * @param savedDataFileName
     */
    public void startServer(String savedDataFileName) throws Exception {
        if (savedDataFileName == null) {
            //todo:каким макаром он так должен стартовать, что тут реализовывать?
            System.out.println("Сервер стартует с нуля");
        }

    }

    /**
     * Останавливает сервер и записывает все его содержимое в файл сохранения с именем savedDataFileName.
     * Если savedDataFileName == null, запись содержимого не производится.
     *
     * @param savedDataFileName
     */
    public void stopServer(String savedDataFileName) throws Exception {

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
        //todo:тут должна быть проверка на валидность данных для регистрации
        if (!verifyName(request.getFirstName()) || !verifyName(request.getLastName()) ||
                !verifyName(request.getLogin())) {
            return "{error}";
        }
        User newUser = createUser(request.getFirstName(), request.getLastName(), request.getLogin(),
                request.getPassword());
        String token = "{\"token\":" + "\"" + newUser.getToken() + "\"}";
        return token;
    }

    public User createUser(String firstName, String lastName, String login, String password) {
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setLogin(login);
        user.setPassword(password);
        user.setToken(tokenGenerate());
        return user;
    }

    public void addToDataBase(User user) {
        userDao.insert(user);
    }
    public boolean verifyAll(String firtName, String lastName, String login, String password) {
        //todo:методы  verify нужно для каждого варианта сделать по своему
        return verifyName(firtName) && verifyName(lastName) && verifyName(login) && verifyName(password);
    }

    private boolean verifyName(String name) {
        return name != null && name.length() > 2 && name.length() < 90;
    }

    public List<User> getUserList() {
        return db.getUserList();
    }

    public boolean validateToken(String token) {
        for (User item : db.getUserList()) {
            if (item.getToken().equals(token)) {
                return true;
            }
        }
        return false;
    }

    private String tokenGenerate() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

}

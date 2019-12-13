import com.fasterxml.jackson.databind.ObjectMapper;
import dao.UserDaoImpl;
import database.DataBase;
import model.User;
import request.RegisterUserDtoRequest;

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
        RegisterUserDtoRequest request = mapper.readValue(requestJsonString, RegisterUserDtoRequest.class);
        if (!request.validate(request.getFirstName(), request.getLastName(), request.getLogin(), request.getPassword())) {
            //todo: некорректная строка, ключ значение - формат json
            return "{error}";
        }
        /**
         *..... Итак, мы создали экземпляр класса модели. В этом экземпляре данные корректные в соответствии с
         * нашими требованиям, так как мы создавали его только в том случае, если экземпляр класса запроса прошел
         * проверку.
         * Те перед созданием экземпляра класса "модели" должна быть проверка на валидность данных?
         */
        User newUser = createUserWithToken(request.getFirstName(), request.getLastName(), request.getLogin(),
                request.getPassword());
        /**
         * Итак мы создали экземпляр класса модели. В этом экземпляре данные корректные в соответствии с нашими
         * требованиями. Экземпляр класса модели мы теперь должны добавить в нашу базу данных.....
         */
        addUserToDataBase(newUser);
        String token = "{\"token\":" + "\"" + newUser.getToken() + "\"}";
        return token;
    }

    public User createUserWithToken(String firstName, String lastName, String login, String password) {
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setLogin(login);
        user.setPassword(password);
        user.setToken(tokenGenerate());
        return user;
    }

    public void addUserToDataBase(User user) {
        userDao.insert(user);
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

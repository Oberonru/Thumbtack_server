import com.fasterxml.jackson.databind.ObjectMapper;
import dao.UserDaoImpl;
import database.DataBase;
import model.User;
import request.RegisterUserDtoRequest;
import response.ErrorDtoResponse;
import response.RegisterUserDtoResponse;

import java.util.UUID;

/**
 * Будет лучше, если сам сервер ниекаких операций производить не будет, а будет делегировать из
 * соответствующему сервису
 */
public class UserService {

    private UserDaoImpl userDao = new UserDaoImpl();
    private DataBase db = DataBase.getInstance();
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
        if (!validate(request.getFirstName(), request.getLastName(), request.getLogin(), request.getPassword())) {
            ErrorDtoResponse errorDtoResponse = new ErrorDtoResponse();
            errorDtoResponse.error = "Params isn't valid";
            return mapper.writeValueAsString(errorDtoResponse);
        }

        User newUser = createUserWithToken(request.getFirstName(), request.getLastName(), request.getLogin(),
                request.getPassword());
        /**
         * Итак мы создали экземпляр класса модели. В этом экземпляре данные корректные в соответствии с нашими
         * требованиями. Экземпляр класса модели мы теперь должны добавить в нашу базу данных.....
         */
        addUserToDataBase(newUser);
        RegisterUserDtoResponse response = new RegisterUserDtoResponse();
        response.setToken(newUser.getToken());
        String token = mapper.writeValueAsString(response);
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

    private boolean validate(String firstName, String lastName, String login, String password) {
        return validateName(firstName) && validateName(lastName) && validateLogin(login) &&
                validateName(password);
    }

    private boolean validateName(String name) {
        return name != null && name.length() > 2 && name.length() < 60;
    }

    private boolean validateLogin(String login) {
        for (User user : db.getUserList()) {
            if (user.getLogin().equals(login)) {
                return false;
            }
        }
        return true;
    }

}

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.UserDaoImpl;
import database.DataBase;
import model.User;
import request.LogInDtoRequest;
import request.LogOutDtoRequest;
import request.RegisterUserDtoRequest;
import response.ErrorDtoResponse;
import response.RegisterUserDtoResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Будет лучше, если сам сервер ниекаких операций производить не будет, а будет делегировать из
 * соответствующему сервису
 */
public class UserService {
    private UserDaoImpl userDao = new UserDaoImpl();
    private ObjectMapper mapper = new ObjectMapper();

    /**
     * Регистрирует радиослушателя на сервере. requestJsonString содержит данные о радиослушателе, необходимые для
     * регистрации.  Метод при успешном выполнении возвращает json с единственным элементом “token”. Если же команду по
     * какой-то причине выполнить нельзя, возвращает json с элементом “error”
     * При регистрации (метод registerUser) радиослушателя возвращаемая строка (при успешном выполнении)
     * должна обязательно содержать поле “token” - уникальный номер, присвоенный этому радиослушателю
     * в результате регистрации.
     */

    public String registerUser(String requestJsonString) throws Exception {
        RegisterUserDtoRequest request = mapper.readValue(requestJsonString, RegisterUserDtoRequest.class);
        if (validateParams(
                request.getFirstName(), request.getLastName(), request.getLogin(), request.getPassword()
        ).size() > 0) {
            ErrorDtoResponse errorResponse = new ErrorDtoResponse();
            errorResponse.error = "Params is not valid";
            return mapper.writeValueAsString(errorResponse);
        }

        User newUser = createUserWithToken(request.getFirstName(), request.getLastName(), request.getLogin(),
                request.getPassword());

        userDao.insert(newUser);
        RegisterUserDtoResponse response = new RegisterUserDtoResponse();
        response.setToken(newUser.getToken());
        return mapper.writeValueAsString(response);
    }

    /**
     * и радиослушатель, вышедший с сервера, входит на него снова (метод login), он получает новый токен, который может
     * использовать во всех операциях вплоть до нового выхода.
     */
    public String logIn(String requestJsonString) throws IOException {
        LogInDtoRequest logInRequest = mapper.readValue(requestJsonString, LogInDtoRequest.class);
        User user = getUserByLogin(logInRequest.getLogin());
        if (user != null) {
            if (user.getToken() == null) {
                user.setToken(generateToken());
                userDao.updateUser(user);
            }
            return mapper.writeValueAsString(user.getToken());
        } else return "{\"error\" : \"login not found\"}";
    }

    /**
     * Зарегистрированный на сервере радиослушатель может выйти с сервера. Вышедший с сервера радиослушатель может
     * войти на сервер снова. При этом ему достаточно ввести свои логин и пароль.
     * Зарегистрированный на сервере радиослушатель может покинуть сервер, в этом случае вся информация о нем удаляется
     * <p>
     */

    public String logOut(String requestJsonString) throws IOException {
        LogOutDtoRequest logOutRequest = mapper.readValue(requestJsonString, LogOutDtoRequest.class);
        User user = userDao.getUserByToken(logOutRequest.getToken());
        if (user != null) {
            user.setToken(null);
            userDao.updateUser(user);
            return "{}";
        }
        return "{\"error\" : \"user not found\"}";
    }

    /**
     * Зарегистрированный на сервере радиослушатель может покинуть сервер, в этом случае вся информация о нем
     * удаляется, а список сделанных им предложений обрабатывается как указано ниже.
     *
     * @param requestJsonString
     * @return user
     */
    public String exitToServer(String requestJsonString) {
        return "fig";
    }

    public User createUserWithToken(String firstName, String lastName, String login, String password) {
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setLogin(login);
        user.setPassword(password);
        user.setToken(generateToken());
        return user;
    }

    public User getUserByLogin(String login) {
        for (User user : userDao.getUserList()) {
            if (user.getLogin().equals(login)) {
                return user;
            }
        }
        return null;
    }

    public User getUserByToken(String token) {
        for (User user : userDao.getUserList()) {
            if (user.getToken().equals(token)) {
                return user;
            }
        }
        return null;
    }

    private String generateToken() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    private List<String> validateParams(String firstName, String lastName, String login, String password) {
        List<String> errors = new ArrayList<String>();
        if (!validateName(firstName)) {
            errors.add("firstName is not valid");
        }
        if (!validateName(lastName)) {
            errors.add("last name is not valid");
        }
        if (getUserByLogin(login) != null) {
            errors.add("login is not valid");
        }
        return errors;
    }

    private boolean validateName(String name) {
        return name != null && name.length() > 2 && name.length() < 60;
    }
}

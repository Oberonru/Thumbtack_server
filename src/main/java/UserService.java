import com.fasterxml.jackson.databind.ObjectMapper;
import dao.UserDaoImpl;
import model.User;
import request.LogInDtoRequest;
import request.LogOutDtoRequest;
import request.RegisterUserDtoRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    public String registerUser(RegisterUserDtoRequest request) throws Exception {
        if (validateParams(
                request.getFirstName(), request.getLastName(), request.getLogin(), request.getPassword()).size() > 0) {
            throw new Exception("Params is not valid");
        }

        User user = getUserByLogin(request.getLogin());
        if (user != null) {
            if (request.getLogin().equals(user.getLogin())) {
                throw new Exception("Login is already used");
            }
        }

        User newUser = createUserWithToken(request.getFirstName(), request.getLastName(), request.getLogin(),
                request.getPassword());
        userDao.insert(newUser);

        return newUser.getToken();
    }

    /**
     * и радиослушатель, вышедший с сервера, входит на него снова (метод login), он получает новый токен, который может
     * использовать во всех операциях вплоть до нового выхода.
     */
    public String logIn(LogInDtoRequest logInRequest) throws Exception {
        User user = getUserByLogin(logInRequest.getLogin(), logInRequest.getPassword());

        if (user == null) {
//            ErrorDtoResponse response = new ErrorDtoResponse("User not found");
//            return response.error;
            throw new Exception("User not found");
        }

        if (user.getToken() == null) {
            user.setToken(generateToken());
            userDao.updateUser(user);
        }

        return "{}";
    }

    /**
     * Зарегистрированный на сервере радиослушатель может выйти с сервера. Вышедший с сервера радиослушатель может
     * войти на сервер снова. При этом ему достаточно ввести свои логин и пароль.
     * Зарегистрированный на сервере радиослушатель может покинуть сервер, в этом случае вся информация о нем удаляется
     * <p>
     */

    public String logOut(String requestJsonString) throws IOException {
        LogOutDtoRequest logOutRequest = mapper.readValue(requestJsonString, LogOutDtoRequest.class);
        User user = getUserByToken(logOutRequest.getToken());
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

    public User getUserByLogin(String login, String password) {
        for (User user : userDao.getUserList()) {
            if (user.getLogin().equals(login) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
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
            if (user.getToken() != null && user.getToken().equals(token)) {
                return user;
            }
        }
        return null;
    }

    public boolean verifyToken(String token) throws Exception {
        User user = getUserByToken(token);
        return user != null;
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
        if (getUserByLogin(login, password) != null) {
            errors.add("login is not valid");
        }
        return errors;
    }

    private boolean validateName(String name) {
        return name != null && name.length() > 2 && name.length() < 60;
    }
}

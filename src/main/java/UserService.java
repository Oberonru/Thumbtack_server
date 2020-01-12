import dao.UserDaoImpl;
import model.User;
import request.ExitToServerDtoRequest;
import request.LogInDtoRequest;
import request.LogOutDtoRequest;
import request.RegisterUserDtoRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserService {
    private UserDaoImpl userDao = new UserDaoImpl();

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

    public String logIn(LogInDtoRequest request) throws Exception {
        User user = getUserByLogin(request.getLogin(), request.getPassword());

        if (user == null) {
            throw new Exception("User not found");
        }

        if (user.getToken() == null) {
            user.setToken(generateToken());
            userDao.updateUser(user);
        }

        return "{}";
    }

    public String logOut(LogOutDtoRequest logOutRequest) throws Exception {
        User user = getUserByToken(logOutRequest.getToken());
        if (user == null) {
            throw new Exception("User not found");
        }

        user.setToken(null);
        userDao.updateUser(user);
        return "{}";
    }

    public String exitToServer(ExitToServerDtoRequest request) throws Exception {
        User user = getUserByToken(request.getToken());

        if (user == null) {
            throw new Exception("User not found");
        }

        for (User person : userDao.getUserList()) {
            if (person.getToken().equals(request.getToken())) {
                userDao.getUserList().remove(person);
                break;
            }
        }
        return "{}";
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

    private String generateToken() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    private List<String> validateParams(String firstName, String lastName, String login, String password) {
        List<String> errors = new ArrayList<String>();
        if (!isValidName(firstName)) {
            errors.add("firstName is not valid");
        }
        if (!isValidName(lastName)) {
            errors.add("last name is not valid");
        }
        if (getUserByLogin(login, password) != null) {
            errors.add("login is not valid");
        }
        return errors;
    }

    private boolean isValidName(String name) {
        return name != null && name.length() > 2 && name.length() < 60;
    }
}

import dao.UserDaoImpl;
import model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**

 */
public class UserService {

    //todo: перенести хранение списков отдельно, под вид базы данных
    private List<User> userList = new ArrayList<User>();
    private UserDaoImpl userDao = new UserDaoImpl();

    public User createUser(String firstName, String lastName, String login, String password) {
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setLogin(login);
        user.setPassword(password);
        user.setToken(tokenGenerate());
        userDao.insert(user);
        return user;
    }

    public boolean verifyAll(String firtName, String lastName, String login, String password) {
        //todo:методы  verify нужно для каждого варианта сделать по своему
        return verifyName(firtName) && verifyName(lastName) && verifyName(login) && verifyName(password);
    }

    private boolean verifyName(String name) {
        return name != null && name.length() > 2 && name.length() < 90;
    }

    public List<User> getUserList() {
        return userList;
    }

    public boolean validateToken(String token) {
        for (User item : userList) {
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

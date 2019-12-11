package model;

import java.io.Serializable;
import java.util.UUID;

public class User implements Serializable {
    private String firstName;
    private String lastName;
    private String login;
    private String password;
    private String token;

    public User() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        //логика в модулях не должна присутствовать (в условии прописано)
//        if (firstName != null && firstName.length() > 2 && firstName.length() < 60) {
//            this.firstName = firstName;
//        }
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

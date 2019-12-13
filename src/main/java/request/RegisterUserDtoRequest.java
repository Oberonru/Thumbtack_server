package request;/*
json:
{
 "firstName": "Vasya",
 "lastName": "Pupkin",
 "login": "vasya_tye_best",
 "password": "238nsdf"
}
 */

import database.DataBase;
import model.User;

public class RegisterUserDtoRequest {
    private String firstName;
    private String lastName;
    private String login;
    private String password;

    public RegisterUserDtoRequest(String firstName, String lastName, String login, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.login = login;
        this.password = password;
    }

    public RegisterUserDtoRequest(){}

    /**
     * ...Правильно ли заданы firstName и lastName, не являются ли они пустыми, не является ли пустым пароль, и если
     * нет - удовлетворяет ли пароль каким-то требованиям  и т.д. В случае обнаружения ошибки при анализе класс сервиса
     * прекращает дальнейшую обработку и возвращает json с полем “error”. Такую проверку может провести сам сервис,
     * проверяя каждое поле. Альтернативный вариант -  сделать в классе RegisterUserDtoRequest метод validate и
     * перенести все проверки в него, а сервис пусть вызывает этот метод.
     */
    public boolean validate(String firstName, String lastName, String login, String password) {
        return validateName(firstName) && validateName(lastName) && validateName(login) && validateLogin(login) &&
                validateName(password);
    }

    private boolean validateName(String name) {
      return name != null && name.length() > 2 && name.length() < 60;
    }

    //должен искать в базе данных пользователя с таким логином
    private boolean validateLogin(String login) {
        DataBase db = new DataBase();
        for (User element : db.getUserList()) {
            if (element.getLogin().equals(login)) {
                return true;
            }
        }
        return false;
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
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

    public boolean verifyName(String name) {
        return name != null && name.length() > 2 && name.length() < 90;
    }


}

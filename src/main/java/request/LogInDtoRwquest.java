package request;

public class LogInDtoRwquest {
    private String login;
    private String password;

    public String getLogin() {
        return login;
    }

    public boolean getPassword(String password) {
        return this.password.equals(password);
    }
}

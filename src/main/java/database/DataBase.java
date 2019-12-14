package database;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.Song;
import model.User;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataBase {

    private List<User> userList = new ArrayList<User>();
    private List<Song> songList = new ArrayList<Song>();
    private ObjectMapper mapper = new ObjectMapper();

    /**
     * После того как метод класса DataBase произвел операцию с базой данных, он возвращает какой то результат
     * классу UserDataImpl или иному DAO классу.....а то уже в свою очередь передаёт результат классу сервиса, а класс
     * сервиса но основе его создает какой то запрос (RegisterUserDtoRequest, RegisterSongDtoRequest и тд)
     * todo:метод нужно переделать, должен возвращать
     */
    public boolean addUser(User user) {
        userList.add(user);
        try {
            mapper.writeValue(new File("test.txt"), userList);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean addSong(Song song) {
        songList.add(song);
        try {
            mapper.writeValue(new File("test.txt"), songList);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<User> getUserList() {
        return userList;
    }

    /**
     * Читает данные из файла (у меня test.txt) и инициализирует список userList
     */
    public void loadDataToCache() {
        try {
            User[] users = mapper.readValue(new File("test.txt"), User[].class);
            for (User u : users) {
                userList.add(u);
            }
        } catch (IOException e) {
        }
    }
}

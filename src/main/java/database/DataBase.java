package database;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import model.Song;
import model.User;
import response.ErrorDtoResponse;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataBase {

    @JsonDeserialize(as = ArrayList.class, contentAs = User.class)
    private List<User> userList = new ArrayList<User>();
    private List<Song> songList = new ArrayList<Song>();
    private ObjectMapper mapper = new ObjectMapper();
    private static DataBase instance;
    private DataBase() {}

    /**
     * После того как метод класса DataBase произвел операцию с базой данных, он возвращает какой то результат
     * классу UserDataImpl или иному DAO классу.....а то уже в свою очередь передаёт результат классу сервиса, а класс
     * сервиса но основе его создает какой то запрос (RegisterUserDtoRequest, RegisterSongDtoRequest и тд)
     * На вход подаётся корректные данные с пользователем, но если пользователь залогинен, то метод должен вернуть
     * false, а в классе сервиса эта ошибка переводится в Json error
     */
    public void addUser(User user) {
        userList.add(user);
    }

    public void addSong(Song song) {
        //generateSongId
        songList.add(song);
    }

    public void saveData() {
        try {
            FileWriter fileWriter = new FileWriter(new File("saveData.json") ,false);
            mapper.writerWithDefaultPrettyPrinter().writeValue(fileWriter, userList);
            mapper.writerWithDefaultPrettyPrinter().writeValue(fileWriter, songList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Загружает данные из файла
     */
    public void loadDataToCache(String savedDataFileName) {
        if (savedDataFileName == null || new File(savedDataFileName).length() == 0) {
            System.out.println("Список пользователей пуст, сервер стартует с нуля");
            return;
        }
        try {
            User[] users = mapper.readValue(new File(savedDataFileName), User[].class);
            for (User user : users) {
                userList.add(user);
            }
            Song[] songs = mapper.readValue(new File(savedDataFileName), Song[].class);
            for (Song song : songs) {
                songList.add(song);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static DataBase getInstance() {
        if (instance == null) {
            instance = new DataBase();
        }
        return instance;
    }

    public List<User> getUserList() {
        return userList;
    }
    public List<Song> getSongList() {
        return songList;
    }

}

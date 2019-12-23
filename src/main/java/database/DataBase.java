package database;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import model.DataBaseModel;
import model.Song;
import model.User;

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

    private DataBase() {
    }

    /**
     * После того как метод класса DataBaseModel произвел операцию с базой данных, он возвращает какой то результат
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
            DataBaseModel dbm = new DataBaseModel();
            dbm.users.addAll(userList);
            dbm.songs.addAll(songList);
            FileWriter fileWriter = new FileWriter(new File("saveData.json"), false);
            mapper.writerWithDefaultPrettyPrinter().writeValue(fileWriter, dbm);
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
            DataBaseModel db = mapper.readValue(new File(savedDataFileName), DataBaseModel.class);
            userList.clear();
            songList.clear();
            userList.addAll(db.users);
            songList.addAll(db.songs);
        } catch (Exception e) {
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

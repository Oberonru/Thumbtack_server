package database;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import model.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataBase {

    @JsonDeserialize(as = ArrayList.class, contentAs = User.class)
    private List<User> userList = new ArrayList<User>();
    private List<Song> songList = new ArrayList<Song>();
    private List<Raiting> raitingList = new ArrayList<Raiting>();
    private List<Comment> commentList = new ArrayList<Comment>();
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

    //todo: при обновлении пользователя проверка только на логин, пароль нужно?
    public void updateUser(User user) {
        for (User u : userList) {
            if (u.getLogin().equals(user.getLogin())) {
                u.setFirstName(user.getFirstName());
                u.setLastName(user.getLastName());
                u.setPassword(user.getPassword());
                u.setToken(user.getToken());
            }
        }
    }

    public void addSong(Song song) {
        songList.add(song);
    }

    public String deleteSong(Song song) {
        //todo: если нет никаких других рейтингов или комментариев, песня может быть удалена - добавить проверку
        for (User user : userList) {
            if (user.getLogin().equals(song.getLogin())) {
                if (frequencyRaitings(song.getSongId()) == 1) {
                    songList.remove(song);
                    return "{}";
                }
            }
        }
        return "{\"error\" : \"The user can't delete song\"}";
    }

    public void updateRaiting(Raiting raiting) {
        boolean isExsist = false;
        for (Raiting r : raitingList) {
            if (r.getLogin().equals(raiting.getLogin()) && r.getSongId() == raiting.getSongId()) {
                boolean isAutor = raiting.getLogin().equals(findSongById(raiting.getSongId()).getLogin());
                if (isAutor) {
                    return;
                }
                r.setSongRating(raiting.getSongRating());
                isExsist = true;
                break;
            }
        }
        if (!isExsist) {
            raitingList.add(raiting);
        }
    }

    /**
     * Радиослушатели могут добавлять свои комментарии к предложениям. Комментарий представляет собой одну текстовую
     * строку.  Радиослушатель, сделавший комментарий, считается его автором. Радиослушатели могут присоединяться к комментариям
     * , сделанным ранее другими радиослушателями. Автор комментария вправе изменить его в любой момент. Если на момент
     * изменения к этому комментарию еще никто не присоединился, старый текст комментария просто заменяется на новый.
     * Если же к  этому комментарию кто-то успел к моменту его изменения автором комментария присоединиться, старый
     * вариант комментария остается без изменений, новый вариант добавляется к списку комментариев для этой песни, а
     * автором старого комментария считается сообщество радиослушателей. Если радиослушатель покидает сервер, то этот
     * механизм применяется ко всем его комментариям, в том числе и тем, к которым никто не присоединился.
     * Радиослушатели, присоединившиеся к комментарию, вправе отказаться от своего присоединения, но не могут
     * изменять текст комментария.
     *
     * @param comment
     */
    public void addComment(Comment comment) {
        List<Comment> currentComments = getCommentsBySongId(comment.getSongId());
        if (currentComments.size() != 0 || currentComments.get(commentList.size() - 1).getLogin().equals(comment.getLogin())) {
            currentComments.set(currentComments.size() - 1, comment);
        } else {
            currentComments.add(comment);
        }
    }

    private List<Comment> getCommentsBySongId(int songId) {
        List<Comment> comments = new ArrayList<Comment>();
        for (Comment comment : commentList) {
            if (comment.getSongId() == songId) {
                comments.add(comment);
            }
        }
        return comments;
    }

    public Song findSongById(int songId) {
        for (Song song : songList) {
            if (song.getSongId() == songId) {
                return song;
            }
        }
        return null;
    }

    public int frequencyRaitings(int songId) {
        int count = 0;
        for (Raiting raiting : raitingList) {
            if (raiting.getSongId() == songId) {
                count++;
            }
        }
        return count;
    }

    public User getUserByToken(String token) {
        for (User user : userList) {
            if (user.getToken() != null && user.getToken().equals(token)) {
                return user;
            }
        }
        return null;
    }

    public void deleteRaiting(Raiting raiting) throws Exception {
        for (int i = 0; i < raitingList.size(); i++) {
            Raiting r = raitingList.get(i);
            if (r.getLogin().equals(raiting.getLogin()) && r.getSongId() == raiting.getSongId()) {
                raitingList.remove(r);
                break;
            }
        }
    }

    public void saveData(String savedDataFileName) {
        try {
            DataBaseModel dbm = new DataBaseModel();
            dbm.users.addAll(userList);
            dbm.songs.addAll(songList);
            dbm.ratings.addAll(raitingList);
            dbm.comments.addAll(commentList);
            FileWriter fileWriter = new FileWriter(new File(savedDataFileName), false);
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
            DataBaseModel dbm = mapper.readValue(new File(savedDataFileName), DataBaseModel.class);
            userList.clear();
            songList.clear();
            raitingList.clear();
            commentList.clear();
            userList.addAll(dbm.users);
            songList.addAll(dbm.songs);
            raitingList.addAll(dbm.ratings);
            commentList.addAll(dbm.comments);
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

    public List<Raiting> getRaitingList() {
        return raitingList;
    }

    public List<Comment> getCommentList() {
        return commentList;
    }
}

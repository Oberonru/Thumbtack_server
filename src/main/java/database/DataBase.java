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
    private List<Rating> ratingList = new ArrayList<Rating>();
    private List<Comment> commentList = new ArrayList<Comment>();
    private ObjectMapper mapper = new ObjectMapper();
    private static DataBase instance;

    private DataBase() {
    }
    
    public void addUser(User user) {
        userList.add(user);
    }

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

    public String deleteSong(Song song) throws Exception {
        List<Comment> songComments = getCommentsBySongId(song.getSongId());

        for (User user : userList) {
            if (user.getLogin().equals(song.getLogin())) {
                if (getRatingsCount(song.getSongId()) == 1 && songComments.size() == 0) {
                    songList.remove(song);
                    return "{}";
                }

                else {
                    for (Rating rating : ratingList) {
                        if (rating.getLogin().equals(song.getLogin())) {
                            //я прссто удаляю рейтинг, можно засетить автором рейтинга login : progerCommunity, не понятно условие
                            ratingList.remove(rating);
                            throw new Exception("Song rating is deleted, the user can't delete song");
                        }
                    }
                }
            }
        }
        throw new Exception("The user can't delete song");
    }

    public void updateRaiting(Rating rating) throws Exception {
        boolean isExsist = false;

        for (Rating r : ratingList) {
            if (r.getLogin().equals(rating.getLogin()) && r.getSongId() == rating.getSongId()) {
                boolean isAutor = rating.getLogin().equals(findSongById(rating.getSongId()).getLogin());
                if (isAutor) {
                    throw new Exception("the author of the song can't change the rating");
                }
                r.setSongRating(rating.getSongRating());
                isExsist = true;
                break;
            }
        }
        if (!isExsist) {
            ratingList.add(rating);
        }
    }

    public void addComment(Comment comment) {
        List<Comment> currentComments = getCommentsBySongId(comment.getSongId());

        if (currentComments.size() != 0 && currentComments.get(commentList.size() - 1).getLogin().equals(comment.getLogin())) {
            currentComments.set(currentComments.size() - 1, comment);
            commentList.add(currentComments.get(currentComments.size() - 1));
        } else {
            commentList.add(comment);
        }
    }

    public List<Comment> getCommentsBySongId(int songId) {
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
            if (song != null && song.getSongId() == songId) {
                return song;
            }
        }
        return null;
    }

    public int getRatingsCount(int songId) {
        int count = 0;
        for (Rating rating : ratingList) {
            if (rating.getSongId() == songId) {
                count++;
            }
        }
        return count;
    }

    public void deleteRating(Rating rating) throws Exception {
        if (isSongCreator(rating)) {
            throw new Exception("The Creator of the song can't delete the rating");
        }

        for (int i = 0; i < ratingList.size(); i++) {
            Rating r = ratingList.get(i);
            if (r.getLogin().equals(rating.getLogin()) && r.getSongId() == rating.getSongId()) {
                ratingList.remove(r);
                return;
            }
        }
        throw new Exception("Rating cannot be deleted");
    }

    public void saveData(String savedDataFileName) {
        try {
            DataBaseModel dbm = new DataBaseModel();
            dbm.users.addAll(userList);
            dbm.songs.addAll(songList);
            dbm.ratings.addAll(ratingList);
            dbm.comments.addAll(commentList);
            FileWriter fileWriter = new FileWriter(new File(savedDataFileName), false);
            mapper.writerWithDefaultPrettyPrinter().writeValue(fileWriter, dbm);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadDataToCache(String savedDataFileName) {
        if (savedDataFileName == null || new File(savedDataFileName).length() == 0) {
//            System.out.println("Список пользователей пуст, сервер стартует с нуля");
            return;
        }
        try {
            DataBaseModel dbm = mapper.readValue(new File(savedDataFileName), DataBaseModel.class);
            userList.clear();
            songList.clear();
            ratingList.clear();
            commentList.clear();
            userList.addAll(dbm.users);
            songList.addAll(dbm.songs);
            ratingList.addAll(dbm.ratings);
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

    public List<Rating> getRatingList() {
        return ratingList;
    }

    public List<Comment> getCommentList() {
        return commentList;
    }

    private boolean isSongCreator(Rating rating) {
        for (Song song : songList) {
            if (rating.getLogin().equals(song.getLogin()) && rating.getSongId() == song.getSongId()) {
                return true;
            }
        }
        return false;
    }
}

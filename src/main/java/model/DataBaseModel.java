package model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class DataBaseModel {
    @JsonDeserialize(as = ArrayList.class, contentAs = User.class)
    public List<User> users = new ArrayList<User>();

    @JsonDeserialize(as = ArrayList.class, contentAs = Song.class)
    public List<Song> songs = new ArrayList<Song>();

    @JsonDeserialize(as = ArrayList.class, contentAs = Raiting.class)
    public List<Raiting> ratings = new ArrayList<Raiting>();

    @JsonDeserialize(as = ArrayList.class, contentAs = Comment.class)
    public List<Comment> comments = new ArrayList<Comment>();

}

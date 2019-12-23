package model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.*;

public class DataBaseModel {
    @JsonDeserialize(as = ArrayList.class, contentAs = User.class)
   public List<User> users = new ArrayList<User>();
    @JsonDeserialize(as = ArrayList.class, contentAs = Song.class)
   public List<Song> songs = new ArrayList<Song>();
}

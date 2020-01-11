package response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import model.Comment;

import java.util.ArrayList;
import java.util.List;

public class SongForConcertModel {
    private String songName;
    private String[] composers;
    private String[] author;
    private String musician;
    @JsonIgnore
    private double songDuration;
    private double averageRating;
    private String firstName;
    private String lastName;
    @JsonDeserialize(as = ArrayList.class, contentAs = Comment.class)
    private List<Comment> comments = new ArrayList<Comment>();

    public SongForConcertModel() {

    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String[] getComposers() {
        return composers;
    }

    public void setComposers(String[] composers) {
        this.composers = composers;
    }

    public String[] getAuthor() {
        return author;
    }

    public void setAuthor(String[] author) {
        this.author = author;
    }

    public String getMusician() {
        return musician;
    }

    public void setMusician(String musician) {
        this.musician = musician;
    }

    public double getSongDuration() {
        return songDuration;
    }

    public void setSongDuration(double songDuration) {
        this.songDuration = songDuration;
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

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public List<Comment> getComments() {
        return comments;
    }
}

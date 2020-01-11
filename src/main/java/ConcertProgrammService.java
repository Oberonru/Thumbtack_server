import dao.CommentDaoImpl;
import dao.RatingDaoImpl;
import dao.SongDaoImpl;
import model.Rating;
import model.Song;
import model.User;
import request.ConcertProgramDtoRequest;
import response.SongForConcertModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ConcertProgrammService {

    private UserService userService = new UserService();
    private RatingDaoImpl ratingDao = new RatingDaoImpl();
    private SongDaoImpl songDao = new SongDaoImpl();
    private CommentDaoImpl commentDao = new CommentDaoImpl();

    public List<SongForConcertModel> getConcertProgram(ConcertProgramDtoRequest request) throws Exception {

        List<SongForConcertModel> concertProgram = new ArrayList<SongForConcertModel>();
        User user = userService.getUserByToken(request.getToken());

        if (user == null) {
            throw new Exception("User not found");
        }

        for (Song song : songDao.getSongList()) {
            SongForConcertModel songForConcert = new SongForConcertModel();
            songForConcert.setSongName(song.getSongName());
            songForConcert.setComposers(song.getComposers());
            songForConcert.setAuthor(song.getAuthor());
            songForConcert.setMusician(song.getMusician());
            songForConcert.setSongDuration(song.getSongDuration());

            double averageRating = getAverageRating(song);
            songForConcert.setAverageRating(averageRating);

            songForConcert.setFirstName(user.getFirstName());
            songForConcert.setLastName(user.getLastName());
            songForConcert.getComments().addAll(commentDao.getCommentsBySongId(song.getSongId()));

            concertProgram.add(songForConcert);
        }

        Collections.sort(concertProgram, COMPARE_BY_AVERAGE_RATING);

        //todo: далее

        return concertProgram;
    }

    private static final Comparator<SongForConcertModel> COMPARE_BY_AVERAGE_RATING = new Comparator<SongForConcertModel>() {
        public int compare(SongForConcertModel o1, SongForConcertModel o2) {
            return (int) (o2.getAverageRating() - o1.getAverageRating());
        }
    };

    private double getAverageRating(Song song) {
        double sum = 0;
        int count = 0;
        for (Rating rating : ratingDao.getRatingList()) {
            if (song.getSongId() == rating.getSongId()) {
                sum += rating.getSongRating();
                count++;
            }

        }

        return sum / count;
    }
}

import com.fasterxml.jackson.databind.ObjectMapper;
import database.DataBase;
import request.*;
import response.ErrorDtoResponse;
import response.RegisterUserDtoResponse;
import response.SongForConcertModel;

import java.util.List;

public class Server {

    private UserService userService = new UserService();
    private SongService songService = new SongService();
    private RatingService ratingService = new RatingService();
    private CommentService commentService = new CommentService();
    private ConcertProgramService concertProgramm = new ConcertProgramService();
    private DataBase db = DataBase.getInstance();
    private boolean isStarted;
    private ObjectMapper mapper = new ObjectMapper();

    public void startServer(String savedDataFileName) {
        db.loadDataToCache(savedDataFileName);
        isStarted = true;
    }

    public void stopServer(String savedDataFileName) {
        isStarted = false;
        db.saveData(savedDataFileName);
    }

    public String registerUser(String registerUserJson) throws Exception {
        RegisterUserDtoRequest request;

        if (isStarted) {
            try {
                request = mapper.readValue(registerUserJson, RegisterUserDtoRequest.class);
            } catch (Exception e) {
                return mapper.writeValueAsString(new ErrorDtoResponse("Request is not valid"));
            }

            try {
                RegisterUserDtoResponse response = new RegisterUserDtoResponse();
                String token = userService.registerUser(request);
                response.setToken(token);
                return mapper.writeValueAsString(response);
            } catch (Exception e) {
                return mapper.writeValueAsString(new ErrorDtoResponse(e.getMessage()));
            }
        }

        return mapper.writeValueAsString(new ErrorDtoResponse("Server is not started!"));
    }

    public String logIn(String requestJsonString) throws Exception {
        LogInDtoRequest request;

        try {
            request = mapper.readValue(requestJsonString, LogInDtoRequest.class);
        } catch (Exception e) {
            return mapper.writeValueAsString(new ErrorDtoResponse("Data is not valid"));
        }

        try {
            return mapper.writeValueAsString(userService.logIn(request));
        } catch (Exception e) {
            return mapper.writeValueAsString(new ErrorDtoResponse(e.getMessage()));
        }
    }

    public String logOut(String requestJsonString) throws Exception {
        LogOutDtoRequest request;

        try {
            request = mapper.readValue(requestJsonString, LogOutDtoRequest.class);
        } catch (Exception e) {
            return mapper.writeValueAsString(new ErrorDtoResponse("Data is not valid"));
        }

        try {
            String response = userService.logOut(request);
            return mapper.writeValueAsString(response);
        } catch (Exception e) {
            return mapper.writeValueAsString(new ErrorDtoResponse(e.getMessage()));
        }
    }

    public String addSong(String requestJsonString) throws Exception {
        RegisterSongDtoRequest request;

        try {
            request = mapper.readValue(requestJsonString, RegisterSongDtoRequest.class);
        } catch (Exception e) {
            return mapper.writeValueAsString(new ErrorDtoResponse("Data is not valid"));
        }

        try {
            String response = songService.addSong(request);
            return mapper.writeValueAsString(response);
        } catch (Exception e) {
            return mapper.writeValueAsString(new ErrorDtoResponse(e.getMessage()));
        }

    }

    public String deleteSong(String requestJsonString) throws Exception {
        DeleteSongDtoRequest request;

        try {
            request = mapper.readValue(requestJsonString, DeleteSongDtoRequest.class);
        } catch (Exception e) {
            return mapper.writeValueAsString(new ErrorDtoResponse("Data is not valid"));
        }

        try {
            String response = songService.deleteSong(request);
            return mapper.writeValueAsString(response);
        } catch (Exception e) {
            return mapper.writeValueAsString(new ErrorDtoResponse(e.getMessage()));
        }
    }

    public String addRating(String requestJsonString) throws Exception {
        RatingDtoRequest request;

        try {
            request = mapper.readValue(requestJsonString, RatingDtoRequest.class);
        } catch (Exception e) {
            return mapper.writeValueAsString(new ErrorDtoResponse("Data is not valid"));
        }

        try {
            String response = ratingService.addRaiting(request);
            return mapper.writeValueAsString(response);
        } catch (Exception e) {
            return mapper.writeValueAsString(new ErrorDtoResponse(e.getMessage()));
        }
    }

    public String deleteRating(String requestJsonString) throws Exception {
        DeleteSongRatingDtoRequest request;

        try {
            request = mapper.readValue(requestJsonString, DeleteSongRatingDtoRequest.class);
        } catch (Exception e) {
            return mapper.writeValueAsString(new ErrorDtoResponse("Data is not valid"));
        }

        try {
            String response = ratingService.deleteRating(request);
            return mapper.writeValueAsString(response);
        } catch (Exception e) {
            return mapper.writeValueAsString(new ErrorDtoResponse(e.getMessage()));
        }
    }

    public String addComment(String requestJsonString) throws Exception {
        CommentDtoRequest request;

        try {
            request = mapper.readValue(requestJsonString, CommentDtoRequest.class);
        } catch (Exception e) {
            return mapper.writeValueAsString(new ErrorDtoResponse("Data is not valid"));
        }
        try {

            String response = commentService.addComment(request);
            return mapper.writeValueAsString(response);
        } catch (Exception e) {
            return mapper.writeValueAsString(new ErrorDtoResponse(e.getMessage()));
        }
    }

    public String getSongs(String requestJsonString) throws Exception {
        GetSongsDtoRequest request;
        try {
            request = mapper.readValue(requestJsonString, GetSongsDtoRequest.class);
        } catch (Exception e) {
            return mapper.writeValueAsString(new ErrorDtoResponse("Data is not valid"));
        }

        try {
            String response = songService.getSongs(request);
            return mapper.writeValueAsString(response);
        } catch (Exception e) {
            return mapper.writeValueAsString(new ErrorDtoResponse(e.getMessage()));
        }
    }

    public String getSongByComposers(String requestJsonString) throws Exception {
        FindSongByComposersDtoRequest request;
        try {
            request = mapper.readValue(requestJsonString, FindSongByComposersDtoRequest.class);
        } catch (Exception e) {
            return mapper.writeValueAsString(new ErrorDtoResponse("Data is not valid"));
        }
        try {
            String response = songService.findSongByComposer(request);
            return mapper.writeValueAsString(response);
        } catch (Exception e) {
            return mapper.writeValueAsString(new ErrorDtoResponse(e.getMessage()));
        }
    }

    public String getSongByAuthors(String requestJsonString) throws Exception {
        FindSongByAutorDtoRequest request;

        try {
            request = mapper.readValue(requestJsonString, FindSongByAutorDtoRequest.class);
        } catch (Exception e) {
            return mapper.writeValueAsString(new ErrorDtoResponse("Data is not valid"));
        }

        try {
            String response = songService.findSongByAuthor(request);
            return mapper.writeValueAsString(response);
        } catch (Exception e) {
            return mapper.writeValueAsString(new ErrorDtoResponse(e.getMessage()));
        }
    }

    public String getSongByMusician(String requestJsonString) throws Exception {
        FindSongByMisicianDtoRequest request;
        try {
            request = mapper.readValue(requestJsonString, FindSongByMisicianDtoRequest.class);
        } catch (Exception e) {
            return mapper.writeValueAsString(new ErrorDtoResponse("Data is not valid"));
        }

        try {
            String response = songService.findSongByMusician(request);
            return mapper.writeValueAsString(response);
        } catch (Exception e) {
            return mapper.writeValueAsString(new ErrorDtoResponse(e.getMessage()));
        }
    }

    public String getConcertProgram(String requestJsonString) throws Exception {
        ConcertProgramDtoRequest request;

        try {
            request = mapper.readValue(requestJsonString, ConcertProgramDtoRequest.class);
        } catch (Exception e) {
            return mapper.writeValueAsString(new ErrorDtoResponse("Data is not valid"));
        }

        try {
            List<SongForConcertModel> modelList = concertProgramm.getConcertProgram(request);
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(modelList);
        } catch (Exception e) {
            return mapper.writeValueAsString(new ErrorDtoResponse(e.getMessage()));
        }
    }

    public String exitToServer(String requestJsonString) throws Exception {
        ExitToServerDtoRequest request;
        try {
            request = mapper.readValue(requestJsonString, ExitToServerDtoRequest.class);
        } catch (Exception e) {
            return mapper.writeValueAsString(new ErrorDtoResponse("Data is not valid"));
        }

        try {
            String response = userService.exitToServer(request);
            return mapper.writeValueAsString(response);
        } catch (Exception e) {
            return mapper.writeValueAsString(new ErrorDtoResponse(e.getMessage()));
        }
    }
}

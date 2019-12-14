import dao.SongDataImpl;
import model.Song;

import java.util.ArrayList;
import java.util.List;

public class SongService {

    private SongDataImpl songData = new SongDataImpl();

    public SongService() {}

    public Song createSong(String songName, String[] composer, String[] author, String musician, int songDuration) {
        Song song = new Song();
        song.setSongName(songName);
        song.setComposer(composer);
        song.setAuthor(author);
        song.setMusician(musician);
        song.setSongDuration(songDuration);
        return song;
    }

    //Добавление песни в базу данных
    public void addSongToDataBase(Song song) {
        songData.insert(song);
    }
    /**
     * Радиослушатель добавляет новую песню на сервер. requestJsonString содержит описание песни и token, полученный
     * как результат выполнения команды регистрации радиослушателя. Метод при успешном выполнении возвращает пустой json
     * Если же команду почему-то выполнить нельзя, возвращает json с элементом “error”
     *
     * @param requestJsonString
     * @return
     */
    public String addSong(String requestJsonString) throws Exception {
//        RegisterSongDtoRequest songRequest = mapper.readValue(requestJsonString, RegisterSongDtoRequest.class);
//        //создаю экземпляр ответа...в нём уже должна быть кра жсон строка с нужными данными?
//        RegisterSongDtoResponse songResponse = new RegisterSongDtoResponse();
//        if (userService.validateToken(songRequest.getToken())) {
//            Song createdSong = songService.createSong(songRequest.getSongName(), songRequest.getComposer(),
//                    songRequest.getAuthor(), songRequest.getMusician(), songRequest.getSongDuration());
//        }
//        songResponse.setEmptyResponse("{}");
//        return songResponse.getEmptyResponse();
        return null;
    }
}

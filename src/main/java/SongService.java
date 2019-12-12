import model.Song;
import request.RegisterSongDtoRequest;
import response.RegisterSongDtoResponse;

import java.util.ArrayList;
import java.util.List;

public class SongService {
    private List<Song> songList = new ArrayList<Song>();

    public SongService(List<Song> songList) {
        this.songList = songList;
    }
    public SongService() {}

    public Song createSong(String songName, String[] composer, String[] author, String musician, int songDuration) {
        Song song = new Song();
        song.setSongName(songName);
        song.setComposer(composer);
        song.setAuthor(author);
        song.setMusician(musician);
        song.setSongDuration(songDuration);
        songList.add(song);
        return song;
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

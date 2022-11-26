package voicerecipeserver.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import voicerecipeserver.api.MediaApi;
import voicerecipeserver.model.dto.IdDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import voicerecipeserver.model.entities.Media;
import voicerecipeserver.model.entities.MediaType;
import voicerecipeserver.model.exceptions.NotFoundException;
import voicerecipeserver.respository.MediaRepository;
import voicerecipeserver.respository.MediaTypeRepository;

import java.util.Optional;

@RestController
public class MediaApiController implements MediaApi {

    private final MediaRepository mediaRepository;

    @Autowired
    public MediaApiController(MediaRepository mediaRepository, MediaTypeRepository mediaTypeRepository){
        this.mediaTypeRepository = mediaTypeRepository;
        this.mediaRepository = mediaRepository;
    }

    //TODO тип медиа не проверяется
    @Override
    public ResponseEntity<byte[]> mediaGet(@javax.validation.constraints.PositiveOrZero Long id) throws NotFoundException {

        Optional<Media> media = mediaRepository.findById(id);
        if(media.isEmpty()) {
            throw new NotFoundException("media");
        }

        byte[] data = media.get().getFileData();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.IMAGE_JPEG);
        headers.setContentLength(data.length);
        return new ResponseEntity<byte[]>(data, headers, HttpStatus.OK);
    }

    private final MediaTypeRepository mediaTypeRepository;



    //    todo кешнуть поддерживаемые типы при инициализации и потом их юзать. Эксепшены кидать при отсутствии типа.
    @Override
    public ResponseEntity<IdDto> mediaPost(String mimeType, byte[] data){

        MediaType mediaType = mediaTypeRepository.findByMimeType(mimeType).get();

        Media media = new Media();

        media.setFileData(data);
        media.setMediaType(mediaType);

        mediaRepository.save(media);
        return new ResponseEntity<>(new IdDto().id(media.getId()), HttpStatus.OK);
    }


}

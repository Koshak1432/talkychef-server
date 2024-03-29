package voicerecipeserver.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.CrossOrigin;
import voicerecipeserver.api.MediaApi;
import voicerecipeserver.model.dto.IdDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import voicerecipeserver.model.entities.Media;
import voicerecipeserver.model.entities.MediaType;
import voicerecipeserver.model.exceptions.InvalidMediaTypeException;
import voicerecipeserver.model.exceptions.NotFoundException;
import voicerecipeserver.respository.MediaRepository;
import voicerecipeserver.respository.MediaTypeRepository;
import voicerecipeserver.services.MediaService;

import java.util.Optional;

@RestController
@CrossOrigin(maxAge = 1440)
public class MediaApiController implements MediaApi {

    private final MediaService service;

    @Autowired
    public MediaApiController(MediaService service){
        this.service = service;
    }


    @Override
    public ResponseEntity<byte[]> mediaGet(Long id) throws NotFoundException {
        return service.getMediaById(id);
    }


    @Override
    public ResponseEntity<IdDto> mediaPost(String contentTypeHeader, byte[] data) throws InvalidMediaTypeException {
        return service.addMedia(contentTypeHeader,data);
    }


}

package talkychefserver.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import talkychefserver.api.MediaApi;
import talkychefserver.model.dto.IdDto;
import talkychefserver.services.interfaces.MediaService;

@RestController
@CrossOrigin(maxAge = 1440)
public class MediaApiController implements MediaApi {

    private final MediaService service;

    @Autowired
    public MediaApiController(MediaService service) {
        this.service = service;
    }


    @Override
    public ResponseEntity<byte[]> mediaGet(Long id) {
        return service.getMediaById(id);
    }


    @Override
    public ResponseEntity<IdDto> mediaPost(String contentTypeHeader, byte[] data) {
        return service.addMedia(contentTypeHeader, data);
    }


}

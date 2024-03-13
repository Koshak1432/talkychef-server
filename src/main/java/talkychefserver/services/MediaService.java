package talkychefserver.services;

import org.springframework.http.ResponseEntity;
import talkychefserver.model.dto.IdDto;
import talkychefserver.model.exceptions.InvalidMediaTypeException;
import talkychefserver.model.exceptions.NotFoundException;

public interface MediaService {
    ResponseEntity<byte[]> getMediaById(Long id) throws NotFoundException;

    ResponseEntity<IdDto> addMedia(String contentTypeHeader, byte[] data) throws InvalidMediaTypeException;
}

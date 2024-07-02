package talkychefserver.services.interfaces;

import org.springframework.http.ResponseEntity;
import talkychefserver.model.dto.IdDto;

public interface MediaService {
    ResponseEntity<byte[]> getMediaById(Long id);

    ResponseEntity<IdDto> addMedia(String contentTypeHeader, byte[] data);
}

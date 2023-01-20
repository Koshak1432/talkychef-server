package voicerecipeserver.services;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import voicerecipeserver.model.dto.IdDto;
import voicerecipeserver.model.exceptions.InvalidMediaTypeException;
import voicerecipeserver.model.exceptions.NotFoundException;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

public interface MediaService {
    ResponseEntity<byte[]> getMediaById(Long id) throws NotFoundException;

    ResponseEntity<IdDto> addMedia(String contentTypeHeader, byte[] data) throws InvalidMediaTypeException;
}

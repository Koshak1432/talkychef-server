package voicerecipeserver.services;

import org.springframework.http.ResponseEntity;
import voicerecipeserver.model.dto.IdDto;
import voicerecipeserver.model.dto.MarkDto;
import voicerecipeserver.model.exceptions.NotFoundException;

public interface MarkService {
    ResponseEntity<IdDto> addRecipeMark(MarkDto mark) throws NotFoundException;

    ResponseEntity<IdDto> updateRecipeMark(MarkDto mark) throws NotFoundException;

    ResponseEntity<Void> deleteRecipeMark(Long id);
}

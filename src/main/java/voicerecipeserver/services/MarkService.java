package voicerecipeserver.services;

import org.springframework.http.ResponseEntity;
import voicerecipeserver.model.dto.IdDto;
import voicerecipeserver.model.dto.MarkDto;
import voicerecipeserver.model.exceptions.AuthException;
import voicerecipeserver.model.exceptions.BadRequestException;
import voicerecipeserver.model.exceptions.NotFoundException;

public interface MarkService {
    ResponseEntity<IdDto> addRecipeMark(MarkDto mark) throws NotFoundException, AuthException, BadRequestException;

    ResponseEntity<IdDto> updateRecipeMark(MarkDto mark) throws NotFoundException, BadRequestException, AuthException;

    ResponseEntity<Void> deleteRecipeMark(Long recipeId) throws BadRequestException, AuthException;
}

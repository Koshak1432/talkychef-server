package talkychefserver.services;

import org.springframework.http.ResponseEntity;
import talkychefserver.model.dto.IdDto;
import talkychefserver.model.dto.MarkDto;
import talkychefserver.model.exceptions.AuthException;
import talkychefserver.model.exceptions.BadRequestException;
import talkychefserver.model.exceptions.NotFoundException;

public interface MarkService {

    ResponseEntity<Float> getAvgMark(Long recipeId) throws NotFoundException;

    ResponseEntity<MarkDto> getRecipeMark(String userUid, Long recipeId) throws NotFoundException;

    ResponseEntity<IdDto> addRecipeMark(MarkDto mark) throws NotFoundException, AuthException, BadRequestException;

    ResponseEntity<IdDto> updateRecipeMark(MarkDto mark) throws NotFoundException, BadRequestException, AuthException;

    ResponseEntity<Void> deleteRecipeMark(String userUid, Long recipeId) throws AuthException, NotFoundException,
            BadRequestException;
}

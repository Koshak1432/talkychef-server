package talkychefserver.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import talkychefserver.api.MarkApi;
import talkychefserver.model.dto.IdDto;
import talkychefserver.model.dto.MarkDto;
import talkychefserver.model.exceptions.AuthException;
import talkychefserver.model.exceptions.BadRequestException;
import talkychefserver.model.exceptions.NotFoundException;
import talkychefserver.services.MarkService;

@CrossOrigin(maxAge = 1440)
@RestController
public class MarkApiController implements MarkApi {
    private final MarkService markService;

    @Autowired
    public MarkApiController(MarkService markService) {
        this.markService = markService;
    }

    @Override
    public ResponseEntity<Float> getAvgMark(Long id) throws NotFoundException {
        return markService.getAvgMark(id);
    }

    @Override
    public ResponseEntity<MarkDto> getMark(String userUid, Long recipeId) throws NotFoundException {
        return markService.getRecipeMark(userUid, recipeId);
    }

    @Override
    public ResponseEntity<IdDto> markPost(MarkDto mark) throws BadRequestException, NotFoundException, AuthException {
        return markService.addRecipeMark(mark);
    }

    @Override
    public ResponseEntity<IdDto> markUpdate(MarkDto mark) throws BadRequestException, NotFoundException, AuthException {
        return markService.updateRecipeMark(mark);
    }

    @Override
    public ResponseEntity<Void> markDelete(String userUid, Long recipeId) throws NotFoundException, AuthException,
            BadRequestException {
        return markService.deleteRecipeMark(userUid, recipeId);
    }


}

package talkychefserver.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import talkychefserver.api.MarkApi;
import talkychefserver.model.dto.IdDto;
import talkychefserver.model.dto.MarkDto;
import talkychefserver.services.interfaces.MarkService;

@CrossOrigin(maxAge = 1440)
@RestController
public class MarkApiController implements MarkApi {
    private final MarkService markService;

    @Autowired
    public MarkApiController(MarkService markService) {
        this.markService = markService;
    }

    @Override
    public ResponseEntity<Float> getAvgMark(Long id) {
        return markService.getAvgMark(id);
    }

    @Override
    public ResponseEntity<MarkDto> getMark(String userUid, Long recipeId) {
        return markService.getRecipeMark(userUid, recipeId);
    }

    @Override
    public ResponseEntity<IdDto> markPost(MarkDto mark) {
        return markService.addRecipeMark(mark);
    }

    @Override
    public ResponseEntity<IdDto> markUpdate(MarkDto mark) {
        return markService.updateRecipeMark(mark);
    }

    @Override
    public ResponseEntity<Void> markDelete(String userUid, Long recipeId) {
        return markService.deleteRecipeMark(userUid, recipeId);
    }
}

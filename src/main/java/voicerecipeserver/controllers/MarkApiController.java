package voicerecipeserver.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import voicerecipeserver.api.MarkApi;
import voicerecipeserver.model.dto.IdDto;
import voicerecipeserver.model.dto.MarkDto;
import voicerecipeserver.model.exceptions.BadRequestException;
import voicerecipeserver.model.exceptions.NotFoundException;
import voicerecipeserver.services.MarkService;

@CrossOrigin(maxAge = 1440)
@RestController
public class MarkApiController implements MarkApi {
    private final MarkService markService;

    @Autowired
    public MarkApiController(MarkService markService) {
        this.markService = markService;
    }

    @Override
    public ResponseEntity<IdDto> markPost(MarkDto mark) throws BadRequestException, NotFoundException {
        return markService.addRecipeMark(mark);
    }

    @Override
    public ResponseEntity<IdDto> markUpdate(MarkDto mark) throws BadRequestException, NotFoundException {
        return markService.updateRecipeMark(mark);
    }

    @Override
    public ResponseEntity<Void> markDelete(Long id) throws NotFoundException {
        return markService.deleteRecipeMark(id);
    }
}

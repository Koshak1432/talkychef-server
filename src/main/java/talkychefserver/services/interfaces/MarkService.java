package talkychefserver.services.interfaces;

import org.springframework.http.ResponseEntity;
import talkychefserver.model.dto.IdDto;
import talkychefserver.model.dto.MarkDto;

public interface MarkService {

    ResponseEntity<Float> getAvgMark(Long recipeId);

    ResponseEntity<MarkDto> getRecipeMark(String userUid, Long recipeId);

    ResponseEntity<IdDto> addRecipeMark(MarkDto mark);

    ResponseEntity<IdDto> updateRecipeMark(MarkDto mark);

    ResponseEntity<Void> deleteRecipeMark(String userUid, Long recipeId);
}

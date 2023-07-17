package voicerecipeserver.api;

import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import voicerecipeserver.config.Constants;
import voicerecipeserver.model.dto.CommentDto;
import voicerecipeserver.model.dto.IdDto;
import voicerecipeserver.model.exceptions.BadRequestException;
import voicerecipeserver.model.exceptions.NotFoundException;

import java.util.List;

@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
@RequestMapping(Constants.BASE_API_PATH + "/comments")
@Validated
public interface CommentApi {
    @PostMapping
    ResponseEntity<IdDto> commentPost(@RequestBody CommentDto commentDto) throws NotFoundException;

    // TODO мб следует ещё один эксепшн с другим кодом создать, чтобы различать not found recipe и not found comment
    // или же просто говорить что всё ок(так плохо делать)

    @PutMapping
    ResponseEntity<IdDto> commentUpdate(@RequestBody CommentDto commentDto) throws NotFoundException,
            BadRequestException;

    @DeleteMapping("/{id}")
    ResponseEntity<Void> commentDelete(@PathVariable("id") @Positive(message = "comment id must be positive") Long id) throws NotFoundException;

    @GetMapping("/{id}")
    ResponseEntity<List<CommentDto>> getRecipeComments(
            @PathVariable("id") @Positive(message = "recipe id must be positive") Long id);
}

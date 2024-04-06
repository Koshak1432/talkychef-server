package talkychefserver.api;

import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import talkychefserver.config.Constants;
import talkychefserver.model.dto.CommentDto;
import talkychefserver.model.dto.IdDto;

import java.util.List;

@RequestMapping(Constants.BASE_API_PATH + "/comments")
@Validated
public interface CommentApi {
    @PostMapping
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    ResponseEntity<IdDto> commentPost(@RequestBody CommentDto commentDto);

    @PutMapping
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    ResponseEntity<IdDto> commentUpdate(@RequestBody CommentDto commentDto);

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    ResponseEntity<Void> commentDelete(@PathVariable("id") @Positive(message = "comment id must be positive") Long id);

    @GetMapping("/{id}")
    ResponseEntity<List<CommentDto>> getRecipeComments(
            @PathVariable("id") @Positive(message = "recipe id must be positive") Long id);
}

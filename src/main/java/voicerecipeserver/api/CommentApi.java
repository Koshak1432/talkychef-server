package voicerecipeserver.api;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import voicerecipeserver.config.Constants;
import voicerecipeserver.model.dto.CommentDto;
import voicerecipeserver.model.dto.IdDto;
import voicerecipeserver.model.exceptions.BadRequestException;
import voicerecipeserver.model.exceptions.NotFoundException;

import jakarta.validation.constraints.PositiveOrZero;

@PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
@RequestMapping(Constants.BASE_API_PATH + "/comments")
@Validated
public interface CommentApi {
    @PostMapping
    ResponseEntity<IdDto> commentPost(@RequestBody CommentDto commentDto) throws NotFoundException, BadRequestException;

    // TODO мб следует ещё один эксепшн с другим кодом создать, чтобы различать not found recipe и not found comment
    // или же просто говорить что всё ок(так плохо делать)

    @PutMapping
    ResponseEntity<IdDto> commentUpdate(@RequestBody CommentDto commentDto) throws NotFoundException,
            BadRequestException;

    @DeleteMapping(value = "/{id}")
    ResponseEntity<Void> commentDelete(
            @PathVariable("id") @PositiveOrZero(message = "comment id must be not negative") Long id) throws NotFoundException;
}

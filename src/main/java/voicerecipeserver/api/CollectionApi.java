package voicerecipeserver.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import voicerecipeserver.config.Constants;
import voicerecipeserver.model.dto.CollectionDto;
import voicerecipeserver.model.exceptions.NotFoundException;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@Valid
@RequestMapping(Constants.BASE_API_PATH + "/collections")
public interface CollectionApi {
    @PostMapping
    ResponseEntity<Void> collectionPost(@RequestParam @NotBlank String name);

    @PostMapping(value = "/content")
    ResponseEntity<Void> collectionContentPost(@RequestParam @PositiveOrZero Long recipe,
                                               @RequestParam @NotBlank String collection) throws NotFoundException;

    @GetMapping(value = "/search")
    ResponseEntity<CollectionDto> collectionNameGet(@Size(max = 128) @RequestParam("name") @NotBlank String name,
                                                    @RequestParam(value = "page", required = false) @PositiveOrZero Integer pageNum) throws
            NotFoundException;
}

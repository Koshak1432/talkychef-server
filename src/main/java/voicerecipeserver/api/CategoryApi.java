package voicerecipeserver.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import voicerecipeserver.config.Constants;
import voicerecipeserver.model.dto.CategoryDto;
import voicerecipeserver.model.dto.CollectionDto;
import voicerecipeserver.model.dto.IdDto;
import voicerecipeserver.model.dto.RecipeDto;
import voicerecipeserver.model.exceptions.AuthException;
import voicerecipeserver.model.exceptions.BadRequestException;
import voicerecipeserver.model.exceptions.NotFoundException;

import java.util.List;
@Valid
@RequestMapping(Constants.BASE_API_PATH + "/categories")
public interface CategoryApi {
    @GetMapping
    ResponseEntity<List<CategoryDto>> categoriesGet() throws NotFoundException;
    @GetMapping(value = "/{id}")
    ResponseEntity<List<RecipeDto>> categoriesGetByName(@PathVariable(value = "id") Long id,
                                                        @RequestParam(value = "limit", required = false) @PositiveOrZero Integer limit)
            throws NotFoundException, AuthException, BadRequestException;
}

package voicerecipeserver.services;

import org.springframework.http.ResponseEntity;
import voicerecipeserver.model.dto.IdDto;
import voicerecipeserver.model.dto.MarkDto;
import voicerecipeserver.model.dto.RecipeDto;
import voicerecipeserver.model.exceptions.BadRequestException;
import voicerecipeserver.model.exceptions.NotFoundException;

import java.util.List;

public interface RecipeService {
    ResponseEntity<RecipeDto> getRecipeById(Long id) throws NotFoundException;

    ResponseEntity<IdDto> addRecipe(RecipeDto recipeDto) throws NotFoundException, BadRequestException;

    ResponseEntity<IdDto> updateRecipe(RecipeDto recipeDto, Long id) throws NotFoundException, BadRequestException;

    ResponseEntity<List<RecipeDto>> searchRecipesByName(String name, Integer limit) throws NotFoundException;

    ResponseEntity<IdDto> addRecipeMark(MarkDto mark) throws NotFoundException;

    ResponseEntity<MarkDto> UpdateRecipeMark(MarkDto mark, Long id);
}

package voicerecipeserver.services;

import org.springframework.http.ResponseEntity;
import voicerecipeserver.model.dto.*;
import voicerecipeserver.model.exceptions.AuthException;
import voicerecipeserver.model.exceptions.BadRequestException;
import voicerecipeserver.model.exceptions.NotFoundException;

import java.util.List;

public interface RecipeService {
    ResponseEntity<RecipeDto> getRecipeById(Long id) throws NotFoundException, AuthException;

    ResponseEntity<IdDto> addRecipe(RecipeDto recipeDto) throws NotFoundException, BadRequestException, AuthException;

    ResponseEntity<IdDto> updateRecipe(RecipeDto recipeDto) throws NotFoundException, BadRequestException,
            AuthException;

    ResponseEntity<List<RecipeDto>> searchRecipesByName(String name, Integer limit) throws NotFoundException, AuthException;

    ResponseEntity<Void> deleteRecipe(Long id) throws NotFoundException;

    ResponseEntity<List<RecipeDto>> filterContent(Integer limit, Integer page) throws AuthException, NotFoundException;

    ResponseEntity<List<CategoryDto>>  getCategoriesById(Long id);
}

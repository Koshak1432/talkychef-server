package talkychefserver.services;

import org.springframework.http.ResponseEntity;
import talkychefserver.model.dto.CategoryDto;
import talkychefserver.model.dto.IdDto;
import talkychefserver.model.dto.RecipeDto;
import talkychefserver.model.exceptions.AuthException;
import talkychefserver.model.exceptions.BadRequestException;
import talkychefserver.model.exceptions.NotFoundException;

import java.util.List;

public interface RecipeService {
    ResponseEntity<RecipeDto> getRecipeById(Long id) throws NotFoundException, AuthException;

    ResponseEntity<IdDto> addRecipe(RecipeDto recipeDto) throws NotFoundException, BadRequestException, AuthException;

    ResponseEntity<IdDto> updateRecipe(RecipeDto recipeDto) throws NotFoundException, BadRequestException,
            AuthException;

    ResponseEntity<List<RecipeDto>> searchRecipesByName(String name, Integer limit, Integer page) throws NotFoundException,
            AuthException;

    ResponseEntity<Void> deleteRecipe(Long id) throws NotFoundException;

    ResponseEntity<List<RecipeDto>> getRecommendations(Integer limit, Integer page) throws AuthException, NotFoundException;

    ResponseEntity<List<CategoryDto>> getCategoriesByRecipeId(Long id);
}

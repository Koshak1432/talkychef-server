package voicerecipeserver.services;

import org.springframework.http.ResponseEntity;
import voicerecipeserver.model.dto.CommentDto;
import voicerecipeserver.model.dto.IdDto;
import voicerecipeserver.model.dto.MarkDto;
import voicerecipeserver.model.dto.RecipeDto;
import voicerecipeserver.model.exceptions.BadRequestException;
import voicerecipeserver.model.exceptions.NotFoundException;

import java.util.List;

public interface RecipeService {
    ResponseEntity<RecipeDto> getRecipeById(Long id) throws NotFoundException;

    ResponseEntity<IdDto> addRecipe(RecipeDto recipeDto) throws NotFoundException, BadRequestException;

    ResponseEntity<IdDto> updateRecipe(RecipeDto recipeDto) throws NotFoundException, BadRequestException;

    ResponseEntity<List<RecipeDto>> searchRecipesByName(String name, Integer limit) throws NotFoundException;

    ResponseEntity<IdDto> addRecipeMark(MarkDto mark) throws NotFoundException;

    ResponseEntity<IdDto> updateRecipeMark(MarkDto mark) throws NotFoundException;

    ResponseEntity<Void> deleteRecipeMark(Long id);

    ResponseEntity<IdDto> postComment(CommentDto commentDto) throws NotFoundException, BadRequestException;

    ResponseEntity<IdDto> updateComment(CommentDto commentDto) throws NotFoundException, BadRequestException;

    ResponseEntity<Void> deleteComment(Long commentId);

}

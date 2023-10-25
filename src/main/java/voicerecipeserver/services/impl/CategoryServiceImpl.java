package voicerecipeserver.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import voicerecipeserver.config.Constants;
import voicerecipeserver.model.dto.CategoryDto;
import voicerecipeserver.model.dto.RecipeDto;
import voicerecipeserver.model.entities.Category;
import voicerecipeserver.model.entities.Recipe;
import voicerecipeserver.respository.CategoryRepository;
import voicerecipeserver.respository.RecipeRepository;
import voicerecipeserver.services.CategoryService;
import voicerecipeserver.utils.GetUtil;

import java.util.List;


@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final RecipeRepository recipeRepository;
    private final ModelMapper modelMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, RecipeRepository recipeRepository,
                               ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.recipeRepository = recipeRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public ResponseEntity<List<CategoryDto>> getCategories() {
        List<Category> categories = categoryRepository.findAll();
        List<CategoryDto> categoryDtos = categories.stream().map(
                element -> modelMapper.map(element, CategoryDto.class)).toList();
        return ResponseEntity.ok(categoryDtos);
    }

    @Override
    public ResponseEntity<List<RecipeDto>> getRecipesFromCategory(Long id, Integer limit,
                                                                  Integer page) { //todo проверить на пустой категории
        List<Recipe> recipes = recipeRepository.findByCategoryId(id, GetUtil.getCurrentLimit(limit), GetUtil.getCurrentPage(page));
        List<RecipeDto> recipeDtos = recipes.stream().map(
                element -> modelMapper.map(element, RecipeDto.class)).toList();
        return ResponseEntity.ok(recipeDtos);
    }

    @Override
    @Transactional
    public ResponseEntity<Void> deleteRecipeFromCategory(Long id, Long recipeId) {
        categoryRepository.deleteByCategoryRecipeId(id, recipeId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity<Void> addCategoryToRecipe(Long id, Long categoryId) {
        categoryRepository.addRecipeToCategory(id, categoryId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

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
import voicerecipeserver.model.exceptions.AuthException;
import voicerecipeserver.model.exceptions.NotFoundException;
import voicerecipeserver.respository.CategoryRepository;
import voicerecipeserver.respository.RecipeRepository;
import voicerecipeserver.security.service.impl.AuthServiceCommon;
import voicerecipeserver.services.CategoryService;
import voicerecipeserver.utils.FindUtils;
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
    public ResponseEntity<Void> deleteRecipeFromCategory(Long categoryId, Long recipeId) throws NotFoundException,
            AuthException {
        Recipe recipe = FindUtils.findRecipe(recipeRepository, recipeId);
        if (!AuthServiceCommon.checkAuthorities(recipe.getAuthor().getUid())) {
            throw new AuthException("No rights");
        }

        categoryRepository.deleteByCategoryRecipeId(categoryId, recipeId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity<Void> addCategoryToRecipe(Long recipeId, Long categoryId) throws AuthException,
            NotFoundException {
        Recipe recipe = FindUtils.findRecipe(recipeRepository, recipeId);
        if (!AuthServiceCommon.checkAuthorities(recipe.getAuthor().getUid())) {
            throw new AuthException("No rights");
        }
        Category category = FindUtils.findCategory(categoryRepository, categoryId);
        if (!recipe.getCategories().contains(category)) {
            categoryRepository.addRecipeToCategory(recipeId, categoryId);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

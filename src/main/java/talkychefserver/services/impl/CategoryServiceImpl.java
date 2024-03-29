package talkychefserver.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import talkychefserver.model.dto.CategoryDto;
import talkychefserver.model.dto.RecipeDto;
import talkychefserver.model.entities.Category;
import talkychefserver.model.entities.Recipe;
import talkychefserver.model.exceptions.AuthException;
import talkychefserver.model.exceptions.NotFoundException;
import talkychefserver.respository.CategoryRepository;
import talkychefserver.respository.RecipeRepository;
import talkychefserver.security.service.impl.AuthServiceCommon;
import talkychefserver.services.CategoryService;
import talkychefserver.utils.FindUtils;
import talkychefserver.utils.GetUtil;

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
                                                                  Integer page) {
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

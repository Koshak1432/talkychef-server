package talkychefserver.services.impl;

import lombok.extern.slf4j.Slf4j;
import lombok.extern.slf4j.XSlf4j;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import talkychefserver.model.dto.CategoryDto;
import talkychefserver.model.dto.RecipeDto;
import talkychefserver.model.entities.Category;
import talkychefserver.model.entities.Recipe;
import talkychefserver.model.exceptions.AuthException;
import talkychefserver.respositories.CategoryRepository;
import talkychefserver.respositories.RecipeRepository;
import talkychefserver.security.service.impl.AuthServiceCommon;
import talkychefserver.services.interfaces.CategoryService;
import talkychefserver.utils.FindUtils;
import talkychefserver.utils.GetUtil;

import java.util.List;

@Slf4j
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
        log.info("Processing get categories request");
        List<Category> categories = categoryRepository.findAll();
        List<CategoryDto> categoryDtos = categories.stream().map(
                element -> modelMapper.map(element, CategoryDto.class)).toList();
        log.info("Response categories list size: {}", categoryDtos.size());
        return ResponseEntity.ok(categoryDtos);
    }

    @Override
    public ResponseEntity<List<RecipeDto>> getRecipesFromCategory(Long id, Integer limit,
                                                                  Integer page) { //todo проверить на пустой категории
        log.info("Processing get recipes from category [{}] request", id);
        List<Recipe> recipes = recipeRepository.findByCategoryId(id, GetUtil.getCurrentLimit(limit),
                                                                 GetUtil.getCurrentPage(page));
        List<RecipeDto> recipeDtos = recipes.stream().map(
                element -> modelMapper.map(element, RecipeDto.class)).toList();
        log.info("Processed get recipes from category [{}] request", id);
        return ResponseEntity.ok(recipeDtos);
    }

    @Override
    @Transactional
    public ResponseEntity<Void> deleteRecipeFromCategory(Long categoryId, Long recipeId) {
        log.info("Processing delete recipe [{}] from category [{}] request", recipeId, categoryId);
        Recipe recipe = FindUtils.findRecipe(recipeRepository, recipeId);
        if (!AuthServiceCommon.checkAuthorities(recipe.getAuthor().getUid())) {
            log.error("User have no rights to delete recipe [{}]: ", recipeId);
            throw new AuthException("No rights");
        }
        categoryRepository.deleteByCategoryRecipeId(categoryId, recipeId);
        log.info("Deleted recipe [{}] from category [{}]", recipeId, categoryId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity<Void> addCategoryToRecipe(Long recipeId, Long categoryId) {
        log.info("Processing add category [{}] to recipe [{}] request", categoryId, recipeId);
        Recipe recipe = FindUtils.findRecipe(recipeRepository, recipeId);
        if (!AuthServiceCommon.checkAuthorities(recipe.getAuthor().getUid())) {
            log.error("User have no rights to add recipe [{}] to category [{}]", recipeId, categoryId);
            throw new AuthException("No rights");
        }
        Category category = FindUtils.findCategory(categoryRepository, categoryId);
        if (!recipe.getCategories().contains(category)) {
            categoryRepository.addRecipeToCategory(recipeId, categoryId);
        }
        log.info("Added category [{}] to recipe [{}]", categoryId, recipeId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}

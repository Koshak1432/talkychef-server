package voicerecipeserver.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import voicerecipeserver.model.dto.CategoryDto;
import voicerecipeserver.model.dto.CollectionDto;
import voicerecipeserver.model.dto.RecipeDto;
import voicerecipeserver.model.entities.Category;
import voicerecipeserver.model.entities.Collection;
import voicerecipeserver.model.entities.Recipe;
import voicerecipeserver.model.exceptions.NotFoundException;
import voicerecipeserver.respository.CategoryRepository;
import voicerecipeserver.respository.RecipeRepository;
import voicerecipeserver.services.CategoryService;

import java.util.List;


@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository,
                               ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
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
    public ResponseEntity<List<RecipeDto>> getRecipesFromCategory(Long id, Integer limit)  { //todo проверить на пустой категории
        if (limit == null) limit = 100;
        List<Recipe> recipes = categoryRepository.findRecipesWithCategoryId(id, limit);
        List<RecipeDto> recipeDtos = recipes.stream().map(
                element -> modelMapper.map(element, RecipeDto.class)).toList();
        return ResponseEntity.ok(recipeDtos);
    }
}

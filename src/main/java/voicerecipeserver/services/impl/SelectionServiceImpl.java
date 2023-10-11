package voicerecipeserver.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import voicerecipeserver.model.dto.CategoryDto;
import voicerecipeserver.model.dto.SelectionDto;
import voicerecipeserver.model.entities.Category;
import voicerecipeserver.model.entities.Selection;
import voicerecipeserver.model.exceptions.NotFoundException;
import voicerecipeserver.respository.CategoryRepository;
import voicerecipeserver.respository.SelectionRepository;
import voicerecipeserver.services.SelectionService;

import java.util.List;

@Service
public class SelectionServiceImpl implements SelectionService {
    private final SelectionRepository selectionRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper mapper;

    public SelectionServiceImpl(SelectionRepository selectionRepository, CategoryRepository categoryRepository,
                                ModelMapper mapper) {
        this.selectionRepository = selectionRepository;
        this.categoryRepository = categoryRepository;
        this.mapper = mapper;
    }

    @Override
    public ResponseEntity<List<SelectionDto>> getAllSelections() {
        List<Selection> selections = selectionRepository.findAll();
        List<SelectionDto> selectionDtos = selections.stream().map(
                element -> mapper.map(element, SelectionDto.class)).toList();
        return ResponseEntity.ok(selectionDtos);
    }

    @Override
    public ResponseEntity<List<CategoryDto>> getCategoriesOfSelection(Long id) {
        List<Category> categories = categoryRepository.findBySelectionId(id);
        List<CategoryDto> categoryDtos = categories.stream().map(e -> mapper.map(e, CategoryDto.class)).toList();
        return ResponseEntity.ok(categoryDtos);
    }
}

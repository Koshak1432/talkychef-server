package talkychefserver.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import talkychefserver.model.dto.CategoryDto;
import talkychefserver.model.dto.SelectionDto;
import talkychefserver.model.entities.Category;
import talkychefserver.model.entities.Selection;
import talkychefserver.model.exceptions.NotFoundException;
import talkychefserver.respository.CategoryRepository;
import talkychefserver.respository.SelectionRepository;
import talkychefserver.services.SelectionService;
import talkychefserver.utils.FindUtils;

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
    public ResponseEntity<List<CategoryDto>> getCategoriesOfSelection(Long id) throws NotFoundException {
        FindUtils.findSelectionById(selectionRepository, id);
        List<Category> categories = categoryRepository.findBySelectionId(id);
        List<CategoryDto> categoryDtos = categories.stream().map(e -> mapper.map(e, CategoryDto.class)).toList();
        return ResponseEntity.ok(categoryDtos);
    }
}

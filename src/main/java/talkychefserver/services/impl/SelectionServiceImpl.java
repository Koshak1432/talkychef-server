package talkychefserver.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import talkychefserver.model.dto.CategoryDto;
import talkychefserver.model.dto.SelectionDto;
import talkychefserver.model.entities.Category;
import talkychefserver.model.entities.Selection;
import talkychefserver.respositories.CategoryRepository;
import talkychefserver.respositories.SelectionRepository;
import talkychefserver.services.interfaces.SelectionService;
import talkychefserver.utils.FindUtils;

import java.util.List;

@Slf4j
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
        log.info("Processing get all selections request");
        List<Selection> selections = selectionRepository.findAll();
        List<SelectionDto> selectionDtos = selections.stream().map(
                element -> mapper.map(element, SelectionDto.class)).toList();
        log.info("Response selection list size: {}", selectionDtos.size());
        return ResponseEntity.ok(selectionDtos);
    }

    @Override
    public ResponseEntity<List<CategoryDto>> getCategoriesOfSelection(Long id) {
        log.info("Processing get categories of selection [{}] request", id);
        FindUtils.findSelectionById(selectionRepository, id);
        List<Category> categories = categoryRepository.findBySelectionId(id);
        List<CategoryDto> categoryDtos = categories.stream().map(e -> mapper.map(e, CategoryDto.class)).toList();
        log.info("Response category list size: {}", categoryDtos.size());
        return ResponseEntity.ok(categoryDtos);
    }
}

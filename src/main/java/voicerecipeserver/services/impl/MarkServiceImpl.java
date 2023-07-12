package voicerecipeserver.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import voicerecipeserver.model.dto.IdDto;
import voicerecipeserver.model.dto.MarkDto;
import voicerecipeserver.model.entities.Mark;
import voicerecipeserver.model.exceptions.NotFoundException;
import voicerecipeserver.respository.MarkRepository;
import voicerecipeserver.respository.RecipeRepository;
import voicerecipeserver.respository.UserRepository;
import voicerecipeserver.services.MarkService;

@Service
public class MarkServiceImpl implements MarkService {
    private final ModelMapper mapper;
    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;

    private final MarkRepository markRepository;

    public MarkServiceImpl(ModelMapper mapper, RecipeRepository recipeRepository, UserRepository userRepository,
                           MarkRepository markRepository) {
        this.mapper = mapper;
        this.recipeRepository = recipeRepository;
        this.userRepository = userRepository;
        this.markRepository = markRepository;
    }

    @Override
    public ResponseEntity<IdDto> addRecipeMark(MarkDto markDto) throws NotFoundException {
        Mark mark = mapper.map(markDto, Mark.class);
        setRecipeTo(mark);
        setAuthorTo(mark);
        mark.setId(null);
        markRepository.save(mark);
        return new ResponseEntity<>(new IdDto().id(mark.getId()), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<IdDto> updateRecipeMark(MarkDto markDto) throws NotFoundException {
        Mark newMark = mapper.map(markDto, Mark.class);
        newMark.setId(markDto.getId());
        setAuthorTo(newMark);
        markRepository.save(newMark);
        return new ResponseEntity<>(new IdDto().id(newMark.getId()), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> deleteRecipeMark(Long id) {
        markRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

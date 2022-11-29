package voicerecipeserver.controllers;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import voicerecipeserver.api.MeasureUnitApi;
import voicerecipeserver.model.dto.IdDto;
import voicerecipeserver.model.dto.IngredientDto;
import voicerecipeserver.model.dto.MeasureUnitDto;
import voicerecipeserver.model.entities.Ingredient;
import voicerecipeserver.model.entities.MeasureUnit;
import voicerecipeserver.model.exceptions.NotFoundException;
import voicerecipeserver.respository.MeasureUnitRepository;


import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(maxAge = 1440)
public class MeasureUnitApiController implements MeasureUnitApi {

    private final MeasureUnitRepository measureUnitRepository;
    private final ModelMapper mapper;

    public MeasureUnitApiController(MeasureUnitRepository measureUnitRepository, ModelMapper mapper){
        this.measureUnitRepository = measureUnitRepository;
        this.mapper = mapper;
    }

    public ResponseEntity<IdDto> measureUnitPost(MeasureUnitDto body){
        MeasureUnit unit = mapper.map(body, MeasureUnit.class);

        measureUnitRepository.save(unit);

        return new ResponseEntity<>(new IdDto().id(unit.getId()), HttpStatus.OK);
    }

    public ResponseEntity<MeasureUnitDto> measureUnitIdGet(Long id) throws NotFoundException{
        Optional<MeasureUnit> measureUnitOptional = measureUnitRepository.findById(id);

        if(measureUnitOptional.isEmpty()){
            throw new NotFoundException("Measure unit with id " + id);
        }

        MeasureUnit measureUnit = measureUnitOptional.get();
        MeasureUnitDto measureUnitDto = mapper.map(measureUnit, MeasureUnitDto.class);

        return new ResponseEntity<MeasureUnitDto>(measureUnitDto, HttpStatus.OK);
    }

    public ResponseEntity<List<MeasureUnitDto>> measureUnitSearchNameGet(String name){
        List<MeasureUnit> units = measureUnitRepository.findFirst5ByNameContaining(name.toLowerCase());

        List<MeasureUnitDto> unitDtos = mapper.map(units, new TypeToken<List<MeasureUnitDto>>() {}.getType());

        return new ResponseEntity<>(unitDtos, HttpStatus.OK);
    }
}

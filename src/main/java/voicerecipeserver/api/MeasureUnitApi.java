package voicerecipeserver.api;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import voicerecipeserver.model.dto.IdDto;
import voicerecipeserver.model.dto.IngredientDto;
import voicerecipeserver.model.dto.MeasureUnitDto;
import voicerecipeserver.model.exceptions.NotFoundException;

import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.util.List;

@Validated
public interface MeasureUnitApi {
    @PostMapping(value = "/api/v1/muasureunit", consumes = "application/json")
    ResponseEntity<IdDto> measureUnitPost(@Valid @RequestBody MeasureUnitDto body) ;

    @GetMapping(value = "/api/v1/muasureunit/{id}")
    ResponseEntity<MeasureUnitDto> measureUnitIdGet(@PathVariable("id") @PositiveOrZero Long id) throws NotFoundException;

    @GetMapping(value = "/api/v1/muasureunit/search/{name}", produces = "application/json")
    ResponseEntity<List<MeasureUnitDto>> measureUnitSearchNameGet(@Size(max=32) @PathVariable("name") String name);
}

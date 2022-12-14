package voicerecipeserver.model.mappers;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import voicerecipeserver.model.dto.IngredientDto;
import voicerecipeserver.model.dto.IngredientsDistributionDto;
import voicerecipeserver.model.dto.IngredientsDistributionDto;
import voicerecipeserver.model.dto.RecipeDto;
import voicerecipeserver.model.entities.Ingredient;
import voicerecipeserver.model.entities.IngredientsDistribution;
import voicerecipeserver.model.entities.Recipe;

@Component
public class DefaultMapper extends ModelMapper {
    //TODO декомпозици для настроек
    public DefaultMapper(){
        super();
        this.emptyTypeMap(RecipeDto.class, Recipe.class).addMappings(mapper -> {
            mapper.skip(Recipe::setId);
        }).implicitMappings();

        //todo проверить игнорирование полей типа Categories при конвертировании из Recipe -> RecipeDto, вдруг заставляет их подтягивать за зря

        this.emptyTypeMap(IngredientsDistributionDto.class, IngredientsDistribution.class).addMappings(mapper -> {
            mapper.skip(IngredientsDistribution::setIngredient);
            mapper.map(IngredientsDistributionDto::getCount, IngredientsDistribution::setMeasureUnitCount);
            mapper.<String>map(src -> src.getName(), (dest, v) -> dest.getIngredient().setName(v));
        }).implicitMappings();

        this.emptyTypeMap(IngredientDto.class, Ingredient.class).addMappings(mapper -> {
            mapper.skip(Ingredient::setId);
        }).implicitMappings();

        this.typeMap(IngredientsDistribution.class, IngredientsDistributionDto.class).addMappings(mapper -> {
            mapper.map(IngredientsDistribution::getMeasureUnitCount, IngredientsDistributionDto::setCount );
            mapper.map(src -> src.getIngredient().getName(), IngredientsDistributionDto::setName);
        });


    }
}

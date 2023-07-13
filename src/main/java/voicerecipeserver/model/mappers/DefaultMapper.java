package voicerecipeserver.model.mappers;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.Provider;
import org.springframework.stereotype.Component;
import voicerecipeserver.model.dto.*;
import voicerecipeserver.model.dto.IngredientsDistributionDto;
import voicerecipeserver.model.entities.*;

@Component
public class DefaultMapper extends ModelMapper {
    //TODO декомпозици вообще можно декомпозицию сделать для конструктора
    public DefaultMapper(){
        super();
        this.emptyTypeMap(RecipeDto.class, Recipe.class).addMappings(mapper -> {
            mapper.skip(Recipe::setAuthor);
            mapper.map(RecipeDto::getAuthorUid, (d, v) -> d.getAuthor().setUid((String)v));
        }).implicitMappings();

        Converter<String, String> toLowercase =
                ctx -> ctx.getSource() == null ? null : ctx.getSource().toLowerCase();
        Converter<Long, IngredientsDistributionKey> clearKey = ctx -> new IngredientsDistributionKey();

        this.emptyTypeMap(IngredientsDistributionDto.class, IngredientsDistribution.class).addMappings(mapper -> {
            //ну как бы костыль да, но хз как иначе это было сделать внутри маппера
            mapper.using(clearKey).map(IngredientsDistributionDto::getIngredientId, IngredientsDistribution::setId);
            //id в ключе заполнять не надо, иначе hibernate будет искать существующую сущность
            mapper.<Long>map(src -> null, (dest, v) -> dest.getIngredient().setId(v));
            mapper.map(IngredientsDistributionDto::getCount, IngredientsDistribution::setMeasureUnitCount);
            //все ингредиенты в нижнем регистре живут
            mapper.using(toLowercase).<String>map(IngredientsDistributionDto::getName, (dest, v) -> dest.getIngredient().setName(v));
        }).implicitMappings();

        this.typeMap(IngredientsDistribution.class, IngredientsDistributionDto.class).addMappings(mapper -> {
            mapper.map(IngredientsDistribution::getMeasureUnitCount, IngredientsDistributionDto::setCount);
            mapper.map(src -> src.getIngredient().getName(), IngredientsDistributionDto::setName);
        });

    }
}

package voicerecipeserver.model.mappers;

import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import voicerecipeserver.model.dto.*;
import voicerecipeserver.model.dto.IngredientsDistributionDto;
import voicerecipeserver.model.entities.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Component
public class DefaultMapper extends ModelMapper {
    //TODO декомпозици вообще можно декомпозицию сделать для конструктора
    public DefaultMapper() {
        super();
        this.emptyTypeMap(RecipeDto.class, Recipe.class).addMappings(mapper -> {
            mapper.skip(Recipe::setAuthor);
            mapper.map(RecipeDto::getAuthorUid, (d, v) -> d.getAuthor().setUid((String) v));
        }).implicitMappings();

        Converter<String, String> toLowercase = ctx -> ctx.getSource() == null ? null : ctx.getSource().toLowerCase();
        Converter<Long, IngredientsDistributionKey> clearKey = ctx -> new IngredientsDistributionKey();

        this.emptyTypeMap(IngredientsDistributionDto.class, IngredientsDistribution.class).addMappings(mapper -> {
            //ну как бы костыль да, но хз как иначе это было сделать внутри маппера
            mapper.using(clearKey).map(IngredientsDistributionDto::getIngredientId, IngredientsDistribution::setId);
            //id в ключе заполнять не надо, иначе hibernate будет искать существующую сущность
            mapper.<Long>map(src -> null, (dest, v) -> dest.getIngredient().setId(v));
            mapper.map(IngredientsDistributionDto::getCount, IngredientsDistribution::setMeasureUnitCount);
            //все ингредиенты в нижнем регистре живут
            mapper.using(toLowercase).<String>map(IngredientsDistributionDto::getName,
                                                  (dest, v) -> dest.getIngredient().setName(v));
        }).implicitMappings();

        this.typeMap(IngredientsDistribution.class, IngredientsDistributionDto.class).addMappings(mapper -> {
            mapper.map(IngredientsDistribution::getMeasureUnitCount, IngredientsDistributionDto::setCount);
            mapper.map(src -> src.getIngredient().getName(), IngredientsDistributionDto::setName);
        });

        this.typeMap(UserInfo.class, UserProfileDto.class).addMappings(mapper -> {
            mapper.map(src -> src.getMedia().getId(), UserProfileDto::setMediaId);
            mapper.map(src -> src.getUser().getUid(), UserProfileDto::setUid);
        });

        this.typeMap(Recipe.class, RecipeDto.class).addMappings(m -> {
            m.map(src -> src.getAuthor().getUid(), RecipeDto::setAuthorUid);
        });
//        this.typeMap(Comment.class, CommentDto.class).addMappings(m -> {
//            m.using(new LocalDateTimeToOffsetDateTimeConverter())
//                .map(Comment::getPostTime, CommentDto::postTime);
//        });
//        this.typeMap(CommentDto.class, Comment.class).addMappings(
//                m -> {
//                    m.using(new OffsetDateTimeToLocalDateTimeConverter()).map(CommentDto::getPostTime, Comment::setPostTime);
//                }
//        );
    }

    private static class OffsetDateTimeToLocalDateTimeConverter extends
            AbstractConverter<OffsetDateTime, LocalDateTime> {
        @Override
        protected LocalDateTime convert(OffsetDateTime source) {
            if (source != null) {
                return source.toLocalDateTime();
            }
            return null;
        }
    }

    private static class LocalDateTimeToOffsetDateTimeConverter extends AbstractConverter<LocalDateTime, OffsetDateTime> {
        @Override
        protected OffsetDateTime convert(LocalDateTime source) {
            if (source != null) {
                return source.atOffset(ZoneOffset.UTC);
            }
            return null;
        }
    }

}

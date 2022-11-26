//package voicerecipeserver.model.mappers;
//
//import org.modelmapper.ModelMapper;
//import org.modelmapper.PropertyMap;
//import org.modelmapper.TypeMap;
//import org.springframework.stereotype.Component;
//import voicerecipeserver.model.dto.RecipeDto;
//import voicerecipeserver.model.entities.Media;
//import voicerecipeserver.model.entities.MediaType;
//import voicerecipeserver.model.entities.Recipe;
//
//@Component
//public class DtoToRecipeMapper extends ModelMapper {
//    public DtoToRecipeMapper(){
//        super();
//    }
//
//    private void setUpDtoToRecipe(){
//        TypeMap<RecipeDto, Recipe> propertyMapper = createTypeMap(RecipeDto.class, Recipe.class);
//        PropertyMap<RecipeDto, Recipe> propertyMap = new PropertyMap<RecipeDto, Recipe>() {
//            @Override
//            protected void configure() {
//                map().setMedia(Media.builder()
//                        .mediaType(MediaType.builder()
//                                .mimeType(source.getMedia().getMimeType()).build())
//                                .id(source.getMedia().getMediaId())
//                        .build()
//                );
//
//                map().setSteps();
//            }
//        };
//    }
//
//}

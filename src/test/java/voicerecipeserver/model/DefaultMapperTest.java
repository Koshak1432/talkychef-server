package voicerecipeserver.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import voicerecipeserver.model.dto.*;
import voicerecipeserver.model.entities.*;
import voicerecipeserver.model.mappers.DefaultMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


public class DefaultMapperTest {

    static ModelMapper mapper;

    @BeforeAll
    static public void setup() {
        mapper = new DefaultMapper();
    }

    @Test
    void map_ingredient_to_ingredientDto() {
        Ingredient ingredient = Ingredient.builder().id(3L).name("Картоха").ingredientsDistributionList(
                new ArrayList<>()).build();

        IngredientDto ingredientDto = mapper.map(ingredient, IngredientDto.class);

        assertEquals(ingredient.getId(), ingredientDto.getId());
        assertEquals(ingredient.getName(), ingredientDto.getName());
    }

    @Test
    void map_ingredientDto_to_ingredient() {
        IngredientDto ingredientDto = new IngredientDto().name("Картоха").id(5L);

        Ingredient ingredient = mapper.map(ingredientDto, Ingredient.class);

        assertEquals(ingredientDto.getId(), ingredient.getId());
        assertEquals(ingredientDto.getName(), ingredient.getName());
    }

    @Test
    void map_ingredientsDistribution_to_ingredientDistributionDto() {
        Ingredient ingredient = Ingredient.builder().id(1L).name("Картоха").build();
        Recipe recipe = new Recipe();
        recipe.setId(2L);
        recipe.setAuthor(User.builder().uid("Biba").id(3L).build());
        recipe.setName("Super dish");
        recipe.setCookTimeMins(30);

        IngredientsDistributionKey key = new IngredientsDistributionKey();
        key.setIngredientId(1L);
        key.setRecipeId(2L);

        MeasureUnit unit = new MeasureUnit();
        unit.setName("Unit");
        unit.setId(4L);

        IngredientsDistribution distribution = IngredientsDistribution.builder().id(key).unit(unit).ingredient(
                ingredient).recipe(recipe).measureUnitCount(333.0).build();

        IngredientsDistributionDto distributionDto = mapper.map(distribution, IngredientsDistributionDto.class);

        assertEquals(distribution.getIngredient().getId(), distributionDto.getIngredientId());
        assertEquals(distribution.getIngredient().getName(), distributionDto.getName());
        assertEquals(distribution.getMeasureUnitCount(), distributionDto.getCount());
        assertEquals(distribution.getUnit().getName(), distributionDto.getMeasureUnitName());

    }

    @Test
    void map_ingredientDistributionDto_to_ingredientDistribution_and_check_lower_case() {
        IngredientsDistributionDto distributionDto = new IngredientsDistributionDto().ingredientId(1L).count(
                100.0).measureUnitName("Pinta").name("Pivo");

        IngredientsDistribution distribution = mapper.map(distributionDto, IngredientsDistribution.class);

        assertEquals(distributionDto.getCount(), distribution.getMeasureUnitCount());

        // это должно быть null, чтобы не было перезаписи ингредиента.
        assertNull(distribution.getIngredient().getId());

        assertEquals(distributionDto.getName().toLowerCase(), distribution.getIngredient().getName());
        assertEquals(distributionDto.getMeasureUnitName(), distribution.getUnit().getName());
        assertNotNull(distribution.getId());
        assertNull(distribution.getId().getRecipeId());
        assertNull(distribution.getId().getIngredientId());
    }

    @Test
    void map_stepDto_with_media_to_step() {
        StepDto stepDto = new StepDto().stepNum(1).mediaId(1L).description("Memem").waitTimeMins(8);

        System.out.println(stepDto);
        Step step = mapper.map(stepDto, Step.class);
        System.out.println(step.getMedia());

        assertEquals(stepDto.getMediaId(), step.getMedia().getId());
        assertEquals(stepDto.getStepNum(), step.getStepNum());
        assertEquals(stepDto.getDescription(), step.getDescription());
        assertEquals(stepDto.getWaitTimeMins(), step.getWaitTimeMins());
    }

    @Test
    void map_stepDto_without_media_to_step() {
        StepDto stepDto = new StepDto().stepNum(1).description("Memem");

        Step step = mapper.map(stepDto, Step.class);

        assertNull(step.getMedia());
        assertEquals(stepDto.getStepNum(), step.getStepNum());
        assertEquals(stepDto.getDescription(), step.getDescription());
        assertEquals(stepDto.getWaitTimeMins(), step.getWaitTimeMins());
    }

    @Test
    void map_step_with_media_to_stepDto() {
        Step step = Step.builder().stepNum(0).description("fsdfsa").media(Media.builder().id(5L).build()).id(
                8L).waitTimeMins(90).build();

        StepDto stepDto = mapper.map(step, StepDto.class);

        assertEquals(step.getMedia().getId(), stepDto.getMediaId());
        assertEquals(step.getStepNum(), stepDto.getStepNum());
        assertEquals(step.getDescription(), stepDto.getDescription());
        assertEquals(step.getWaitTimeMins(), stepDto.getWaitTimeMins());
    }

    @Test
    void map_step_without_media_to_stepDto() {
        Step step = Step.builder().stepNum(0).description("fsdfsa").id(8L).build();

        StepDto stepDto = mapper.map(step, StepDto.class);

        assertEquals(step.getStepNum(), stepDto.getStepNum());
        assertEquals(step.getDescription(), stepDto.getDescription());
        assertNull(stepDto.getWaitTimeMins());
        assertNull(stepDto.getMediaId());
    }


    @Test
    void map_commentDto_to_comment() {
        CommentDto dto = new CommentDto();
        dto.id(1L).recipeId(2L).userUid("ASDASXVCKD123sdfd").content("Очень классно мне понравилось");
        Comment comment = mapper.map(dto, Comment.class);

        assertEquals(dto.getId(), comment.getId());
        assertEquals(dto.getUserUid(), comment.getUser().getUid());
        assertEquals(dto.getRecipeId(), comment.getRecipe().getId());
        assertEquals(dto.getContent(), comment.getContent());
        System.out.println(comment);
        System.out.println(comment.getUser());
        System.out.println(comment.getUser().getUid());
    }

    @Test
    void map_comment_to_commentDto() {
        User user = new User("anton", "1234", "Антон", Set.of(new Role("ADMIN")));
        Recipe recipe = new Recipe();
        recipe.setId(25L);
        Comment comment = new Comment(1L, recipe, user, null, "Очень густо");
        CommentDto dto = mapper.map(comment, CommentDto.class);

        assertEquals(dto.getId(), comment.getId());
        assertEquals(dto.getUserUid(), comment.getUser().getUid());
        assertEquals(dto.getRecipeId(), comment.getRecipe().getId());
        assertEquals(dto.getContent(), comment.getContent());
    }

    @Test
    void map_recipeDto_with_required_field_and_steps_to_recipe() {
        RecipeDto recipeDto = new RecipeDto();
        ArrayList<StepDto> stepDtos = new ArrayList<>();

        stepDtos.add(new StepDto().description("Посидеть посмотреть").stepNum(1).mediaId(1L));
        stepDtos.add(new StepDto().description("Посидеть поготовить").stepNum(2).mediaId(2L));
        stepDtos.add(new StepDto().description("Посидеть поесть").stepNum(3));
        recipeDto.name("Ro").mediaId(1L).authorUid("AFDSDE").cookTimeMins(40).setSteps(stepDtos);

        Recipe recipe = mapper.map(recipeDto, Recipe.class);

        System.out.println(recipe.getMedia());
        assertEquals(recipe.getName(), recipeDto.getName());
        assertEquals(recipe.getAuthor().getUid(), recipeDto.getAuthorUid());
        assertEquals(recipe.getCookTimeMins(), recipeDto.getCookTimeMins());
        assertEquals(recipe.getMedia().getId(), recipeDto.getMediaId());
        assertNull(recipe.getCollections());
        assertNull(recipe.getId());
        assertNull(recipe.getCategories());


        assertNotNull(recipe.getSteps());
        List<Step> steps = recipe.getSteps();
        for (int i = 0; i < stepDtos.size(); i++) {
            Step step = steps.get(i);
            StepDto stepDto = stepDtos.get(i);

            assertNull(step.getRecipe());
            if (null != stepDto.getMediaId()) {
                assertEquals(step.getMedia().getId(), stepDto.getMediaId());
            } else {
                assertNull(step.getMedia());
            }
            assertEquals(step.getStepNum(), stepDto.getStepNum());
            assertEquals(step.getDescription(), stepDto.getDescription());
            assertEquals(step.getWaitTimeMins(), stepDto.getWaitTimeMins());
        }
    }

    @Test
    void map_recipeDto_with_required_field_and_steps_and_ingredients_to_recipe() {
        RecipeDto recipeDto = new RecipeDto();
        ArrayList<StepDto> stepDtos = new ArrayList<>();
        ArrayList<IngredientsDistributionDto> ingredientsDistDtos = new ArrayList<>();

        stepDtos.add(new StepDto().description("Посидеть посмотреть").stepNum(1).mediaId(1L));
        stepDtos.add(new StepDto().description("Посидеть поготовить").stepNum(2).mediaId(2L));
        stepDtos.add(new StepDto().description("Посидеть поесть").stepNum(3));

        ingredientsDistDtos.add(
                new IngredientsDistributionDto().name("ing1").ingredientId(1L).count(230.0).measureUnitName("pinta"));
        ingredientsDistDtos.add(
                new IngredientsDistributionDto().name("ing2").ingredientId(2L).count(2302.0).measureUnitName("pinta2"));
        ingredientsDistDtos.add(
                new IngredientsDistributionDto().name("ing3").ingredientId(3L).count(2340.0).measureUnitName("pinta3"));

        recipeDto.name("Ro").mediaId(1L).authorUid("AFDSDE").cookTimeMins(40).steps(
                stepDtos).setIngredientsDistributions(ingredientsDistDtos);

        Recipe recipe = mapper.map(recipeDto, Recipe.class);

        assertNotNull(recipe.getIngredientsDistributions());
        assertEquals(recipe.getIngredientsDistributions().size(), ingredientsDistDtos.size());

        List<IngredientsDistribution> distributions = recipe.getIngredientsDistributions();

        for (int i = 0, max = distributions.size(); i < max; i++) {
            IngredientsDistribution distribution = distributions.get(i);
            IngredientsDistributionDto distributionDto = ingredientsDistDtos.get(i);

            assertNull(distribution.getRecipe());
            assertEquals(distributionDto.getMeasureUnitName(), distribution.getUnit().getName());
            assertEquals(distributionDto.getName(), distribution.getIngredient().getName());
            assertEquals(distributionDto.getCount(), distribution.getMeasureUnitCount());
            assertNull(distribution.getIngredient().getId());
            assertNull(distribution.getId().getRecipeId());
            assertNull(distribution.getId().getIngredientId());
        }
    }


    @Test
    void map_recipe_to_recipeDto() {
        Recipe recipe = new Recipe();
        recipe.setName("name123");
        recipe.setAuthor(new User("anton", "1234", "Антон", Set.of(new Role("ADMIN"))));
        recipe.setCookTimeMins(4044);
        recipe.setMedia(new Media(11L, null, null, null, null, null));

        RecipeDto dto = mapper.map(recipe, RecipeDto.class);
        assertEquals(recipe.getName(), dto.getName());
        assertEquals(recipe.getAuthor().getUid(), dto.getAuthorUid());
        assertEquals(recipe.getCookTimeMins(), dto.getCookTimeMins());
        assertEquals(recipe.getMedia().getId(), dto.getMediaId());
    }

    @Test
    void map_marks_to_marksDto() {
        MarkDto markDto = new MarkDto();
        markDto.mark(3).recipeId(1L).userUid("asdfadfsadfshafjsh");
        mapper.typeMap(Mark.class, MarkDto.class)
                .addMappings(m -> {
                    m.map(src -> src.getUser().getUid(), MarkDto::setUserUid);
                    m.map(src -> src.getRecipe().getId(), MarkDto::setRecipeId);
                });
        Mark mark = mapper.map(markDto, Mark.class);
        assertEquals(mark.getUser().getUid(), markDto.getUserUid());
    }

    @Test
    void map_recipeDto_with_required_field_to_recipe() {
        RecipeDto dto = new RecipeDto();
        dto.name("Re").mediaId(1L).authorUid("AFsssDDE").cookTimeMins(40);

        Recipe recipe = mapper.map(dto, Recipe.class);

        assertEquals(dto.getName(), recipe.getName());
        assertEquals(dto.getAuthorUid(), recipe.getAuthor().getUid());
        assertEquals(dto.getCookTimeMins(), recipe.getCookTimeMins());
        assertEquals(dto.getMediaId(), recipe.getMedia().getId());
        assertNull(recipe.getCollections());
        assertNull(recipe.getId());
        assertNull(recipe.getCategories());
        assertNull(recipe.getSteps());
    }


    @Test
    void map_recipe_list_to_dto_list() {
        RecipeDto recipeDto1 = new RecipeDto();
        recipeDto1.name("dto1").mediaId(1L).authorUid("author1").cookTimeMins(40);
        RecipeDto recipeDto2 = new RecipeDto();
        recipeDto2.name("dto2").mediaId(2L).authorUid("author2").cookTimeMins(50);

        Recipe recipe1 = mapper.map(recipeDto1, Recipe.class);
        Recipe recipe2 = mapper.map(recipeDto2, Recipe.class);
        List<Recipe> recipes = new ArrayList<>();
        recipes.add(recipe1);
        recipes.add(recipe2);

//        List<RecipeDto> recipeDtos = recipes.stream().map(recipe -> mapper.map(recipe, RecipeDto.class)).toList();
        List<RecipeDto> recipeDtos = mapper.map(recipes, new TypeToken<List<RecipeDto>>() {
        }.getType());

        assertEquals(2, recipeDtos.size());

        for (int i = 0; i < recipes.size(); ++i) {
            assertNotNull(recipeDtos.get(i));
            assertEquals(recipes.get(i).getName(), recipeDtos.get(i).getName());
            assertEquals(recipes.get(i).getCookTimeMins(), recipeDtos.get(i).getCookTimeMins());
            assertEquals(recipes.get(i).getMedia().getId(), recipeDtos.get(i).getMediaId());
            assertNull(recipes.get(i).getCollections());
            assertNull(recipes.get(i).getId());
            assertNull(recipes.get(i).getCategories());
            assertNull(recipes.get(i).getSteps());
        }
    }
}

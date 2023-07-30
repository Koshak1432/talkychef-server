package voicerecipeserver.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import voicerecipeserver.config.Constants;
import voicerecipeserver.controllers.RecipeApiController;
import voicerecipeserver.model.dto.IdDto;
import voicerecipeserver.model.dto.IngredientsDistributionDto;
import voicerecipeserver.model.dto.RecipeDto;
import voicerecipeserver.model.dto.StepDto;
import voicerecipeserver.model.entities.Recipe;
import voicerecipeserver.model.entities.Role;
import voicerecipeserver.model.entities.User;
import voicerecipeserver.respository.RecipeRepository;
import voicerecipeserver.respository.UserRepository;
import voicerecipeserver.security.domain.JwtAuthentication;
import voicerecipeserver.services.impl.RecipeServiceImpl;

import java.util.*;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc

public class TestingWebApplicationTests {
    @Autowired
    private RecipeApiController recipeApiController;

    @Autowired
    private RecipeServiceImpl recipeService;

    @MockBean
    private RecipeRepository recipeRepository;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserRepository userRepository;

    @Test
    public void findRecipeByIdReturnsRecipe() throws Exception {
        Recipe recipe = Recipe.builder().id(2L).author(User.builder().uid("admin").id(1L).build()).name(
                "Super dish").cookTimeMins(30).build();
        when(recipeRepository.findById(2L)).thenReturn(Optional.of(recipe));
        this.mockMvc.perform(get(Constants.BASE_API_PATH + "/recipes/2")).andExpect(status().isOk()).andExpect(
                content().contentType("application/json")).andExpect(jsonPath("$.id", is(2))).andExpect(
                jsonPath("$.author_uid", is("admin"))).andExpect(jsonPath("$.name", is("Super dish"))).andExpect(
                jsonPath("$.cook_time_mins", is(30)));
    }

    @Test
    public void findRecipeByIdReturnsNotFoundExc() throws Exception {
        this.mockMvc.perform(get(Constants.BASE_API_PATH + "/recipes/2")).andExpect(status().isNotFound());
    }


    @Test
    public void findRecipeByNameReturnsNotFoundExc() throws Exception {
        this.mockMvc.perform(get(Constants.BASE_API_PATH + "/recipes/search/Jr")).andExpect(status().isNotFound());
    }

    @Test
    public void findRecipeByNameReturnsListOfRecipes() throws Exception {
        Recipe recipe = Recipe.builder().id(2L).author(User.builder().uid("admin").id(1L).build()).name(
                "Не очень горячая курица").cookTimeMins(40).build();
        Recipe recipe2 = Recipe.builder().id(3L).author(User.builder().uid("admin").id(1L).build()).name(
                "Очень-очень горячая курица").cookTimeMins(30).build();
        List<Recipe> recipeList = Arrays.asList(recipe, recipe2);
        when(recipeRepository.findByNameContaining("Очень", 0)).thenReturn(recipeList);

        this.mockMvc.perform(get(Constants.BASE_API_PATH + "/recipes/search/Очень")).andExpect(
                status().isOk()).andExpect(content().contentType("application/json")).andExpect(
                jsonPath("$[0].id", is(2))).andExpect(jsonPath("$[0].author_uid", is("admin"))).andExpect(
                jsonPath("$[0].name", is("Не очень горячая курица"))).andExpect(
                jsonPath("$[0].cook_time_mins", is(40))).andExpect(content().contentType("application/json")).andExpect(
                jsonPath("$[1].id", is(3))).andExpect(jsonPath("$[1].author_uid", is("admin"))).andExpect(
                jsonPath("$[1].name", is("Очень-очень горячая курица"))).andExpect(
                jsonPath("$[1].cook_time_mins", is(30)));
    }

    @Test
    public void deleteRecipeByIdUnauthorized() throws Exception {
        this.mockMvc.perform(delete(Constants.BASE_API_PATH + "/recipes/2")).andExpect(status().isUnauthorized());
    }

    @Test
    public void deleteRecipeByIdAdmin() throws Exception {
        Recipe recipe = Recipe.builder().id(2L).author(User.builder().uid("admin").id(1L).build()).name(
                "Super dish").cookTimeMins(30).build();
        when(recipeRepository.findById(2L)).thenReturn(Optional.of(recipe));

        Set<Role> roles = new HashSet<>();
        roles.add(new Role("ADMIN"));

        // Set the authentication context with the desired roles
        Authentication auth = SecurityTestUtils.createAuthenticationWithRoles(roles, "user");

        this.mockMvc.perform(delete(Constants.BASE_API_PATH + "/recipes/2").with(authentication(auth))).andExpect(
                status().isOk());
    }

    @Test
    public void PostRecipeUnauthorized() throws Exception {
        Set<Role> roles = new HashSet<>();

        when(userRepository.findByUid("user")).thenReturn(Optional.of(User.builder().uid("user").id(2L).build()));
        Authentication auth = SecurityTestUtils.createAuthenticationWithRoles(roles, "user");
        RecipeDto recipeDto = new RecipeDto().id(1L).name("Super dish").cookTimeMins(3).ingredientsDistributions(
                Collections.singletonList(
                        new IngredientsDistributionDto().ingredientId(3L).name("salt").measureUnitName(
                                "th"))).authorUid("user").mediaId(3L).steps(
                Collections.singletonList(new StepDto().stepNum(1).description("some")));
        Recipe recipe = Recipe.builder().id(1L).author(User.builder().uid("user").id(2L).build()).name(
                "Super dish").cookTimeMins(30).build();

        mockMvc.perform(post(Constants.BASE_API_PATH + "/recipes").contentType(MediaType.APPLICATION_JSON).content(
                asJsonString(recipeDto)).with(authentication(auth))).andExpect(status().isForbidden());
        verify(recipeRepository, never()).delete(recipe);
    }

    private String asJsonString(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }

    public class SecurityTestUtils {

        public static Authentication createAuthenticationWithRoles(Set<Role> roles, String login) {
            JwtAuthentication jwtAuthentication = new JwtAuthentication();
            jwtAuthentication.setAuthenticated(true);
            jwtAuthentication.setLogin(login);
            jwtAuthentication.setRoles(roles);
            return jwtAuthentication;
        }
    }
}
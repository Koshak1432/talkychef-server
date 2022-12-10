package voicerecipeserver.model.dto;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import voicerecipeserver.model.dto.RecipeDto;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * CollectionDto
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-12-10T15:51:06.494Z[GMT]")


public class CollectionDto   {
  @JsonProperty("name")
  private String name = null;

  @JsonProperty("recipes")
  @Valid
  private List<RecipeDto> recipes = new ArrayList<RecipeDto>();

  public CollectionDto name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   **/
      @NotNull

  @Size(max=128)   public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public CollectionDto recipes(List<RecipeDto> recipes) {
    this.recipes = recipes;
    return this;
  }

  public CollectionDto addRecipesItem(RecipeDto recipesItem) {
    this.recipes.add(recipesItem);
    return this;
  }

  /**
   * Get recipes
   * @return recipes
   **/
      @NotNull
    @Valid
    public List<RecipeDto> getRecipes() {
    return recipes;
  }

  public void setRecipes(List<RecipeDto> recipes) {
    this.recipes = recipes;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CollectionDto collectionDto = (CollectionDto) o;
    return Objects.equals(this.name, collectionDto.name) &&
        Objects.equals(this.recipes, collectionDto.recipes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, recipes);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CollectionDto {\n");
    
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    recipes: ").append(toIndentedString(recipes)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

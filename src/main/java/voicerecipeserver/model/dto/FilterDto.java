package voicerecipeserver.model.dto;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import voicerecipeserver.model.dto.CategoryDto;
import voicerecipeserver.model.dto.IngredientDto;
import voicerecipeserver.model.dto.TimeDto;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

/**
 * FilterDto
 */
@Validated
@jakarta.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2023-12-06T07:36:51.008951992Z[GMT]")


public class FilterDto   {
  @JsonProperty("category")
  @Valid
  private List<CategoryDto> category = new ArrayList<CategoryDto>();

  @JsonProperty("time")
  @Valid
  private List<TimeDto> time = new ArrayList<TimeDto>();

  @JsonProperty("ingredients")
  @Valid
  private List<IngredientDto> ingredients = new ArrayList<IngredientDto>();

  public FilterDto category(List<CategoryDto> category) {
    this.category = category;
    return this;
  }

  public FilterDto addCategoryItem(CategoryDto categoryItem) {
    this.category.add(categoryItem);
    return this;
  }

  /**
   * Get category
   * @return category
   **/
      @NotNull
    @Valid
    public List<CategoryDto> getCategory() {
    return category;
  }

  public void setCategory(List<CategoryDto> category) {
    this.category = category;
  }

  public FilterDto time(List<TimeDto> time) {
    this.time = time;
    return this;
  }

  public FilterDto addTimeItem(TimeDto timeItem) {
    this.time.add(timeItem);
    return this;
  }

  /**
   * Get time
   * @return time
   **/
      @NotNull
    @Valid
    public List<TimeDto> getTime() {
    return time;
  }

  public void setTime(List<TimeDto> time) {
    this.time = time;
  }

  public FilterDto ingredients(List<IngredientDto> ingredients) {
    this.ingredients = ingredients;
    return this;
  }

  public FilterDto addIngredientsItem(IngredientDto ingredientsItem) {
    this.ingredients.add(ingredientsItem);
    return this;
  }

  /**
   * Get ingredients
   * @return ingredients
   **/
      @NotNull
    @Valid
    public List<IngredientDto> getIngredients() {
    return ingredients;
  }

  public void setIngredients(List<IngredientDto> ingredients) {
    this.ingredients = ingredients;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FilterDto filterDto = (FilterDto) o;
    return Objects.equals(this.category, filterDto.category) &&
        Objects.equals(this.time, filterDto.time) &&
        Objects.equals(this.ingredients, filterDto.ingredients);
  }

  @Override
  public int hashCode() {
    return Objects.hash(category, time, ingredients);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FilterDto {\n");
    
    sb.append("    category: ").append(toIndentedString(category)).append("\n");
    sb.append("    time: ").append(toIndentedString(time)).append("\n");
    sb.append("    ingredients: ").append(toIndentedString(ingredients)).append("\n");
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

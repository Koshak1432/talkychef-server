package voicerecipeserver.model.dto;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

/**
 * IngredientsDistributionDto
 */
@Validated
@jakarta.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2023-07-27T02:48:13.656840513Z[GMT]")


public class IngredientsDistributionDto   {
  @JsonProperty("name")
  private String name = null;

  @JsonProperty("measure_unit_name")
  private String measureUnitName = null;

  @JsonProperty("count")
  private Double count = null;

  @JsonProperty("ingredient_id")
  private Long ingredientId = null;

  public IngredientsDistributionDto name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   **/
      @NotNull

  @Size(max=64)   public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public IngredientsDistributionDto measureUnitName(String measureUnitName) {
    this.measureUnitName = measureUnitName;
    return this;
  }

  /**
   * Get measureUnitName
   * @return measureUnitName
   **/
      @NotNull

  @Size(max=32)   public String getMeasureUnitName() {
    return measureUnitName;
  }

  public void setMeasureUnitName(String measureUnitName) {
    this.measureUnitName = measureUnitName;
  }

  public IngredientsDistributionDto count(Double count) {
    this.count = count;
    return this;
  }

  /**
   * Get count
   * @return count
   **/
      @NotNull

    public Double getCount() {
    return count;
  }

  public void setCount(Double count) {
    this.count = count;
  }

  public IngredientsDistributionDto ingredientId(Long ingredientId) {
    this.ingredientId = ingredientId;
    return this;
  }

  /**
   * Get ingredientId
   * @return ingredientId
   **/
  
    public Long getIngredientId() {
    return ingredientId;
  }

  public void setIngredientId(Long ingredientId) {
    this.ingredientId = ingredientId;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    IngredientsDistributionDto ingredientsDistributionDto = (IngredientsDistributionDto) o;
    return Objects.equals(this.name, ingredientsDistributionDto.name) &&
        Objects.equals(this.measureUnitName, ingredientsDistributionDto.measureUnitName) &&
        Objects.equals(this.count, ingredientsDistributionDto.count) &&
        Objects.equals(this.ingredientId, ingredientsDistributionDto.ingredientId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, measureUnitName, count, ingredientId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class IngredientsDistributionDto {\n");
    
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    measureUnitName: ").append(toIndentedString(measureUnitName)).append("\n");
    sb.append("    count: ").append(toIndentedString(count)).append("\n");
    sb.append("    ingredientId: ").append(toIndentedString(ingredientId)).append("\n");
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

package voicerecipeserver.model.dto;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * MeasureUnitSetDto
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-11-21T15:54:58.689Z[GMT]")


public class MeasureUnitSetDto   {
  @JsonProperty("units")
  @Valid
  private List<String> units = new ArrayList<String>();

  public MeasureUnitSetDto units(List<String> units) {
    this.units = units;
    return this;
  }

  public MeasureUnitSetDto addUnitsItem(String unitsItem) {
    this.units.add(unitsItem);
    return this;
  }

  /**
   * Get units
   * @return units
   **/
      @NotNull

    public List<String> getUnits() {
    return units;
  }

  public void setUnits(List<String> units) {
    this.units = units;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MeasureUnitSetDto measureUnitSetDto = (MeasureUnitSetDto) o;
    return Objects.equals(this.units, measureUnitSetDto.units);
  }

  @Override
  public int hashCode() {
    return Objects.hash(units);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MeasureUnitSetDto {\n");
    
    sb.append("    units: ").append(toIndentedString(units)).append("\n");
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

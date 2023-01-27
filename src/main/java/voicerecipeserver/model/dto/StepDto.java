package voicerecipeserver.model.dto;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import voicerecipeserver.model.dto.IdDto;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * StepDto
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2023-01-22T10:56:17.279Z[GMT]")


public class StepDto   {
  @JsonProperty("media")
  private IdDto media = null;

  @JsonProperty("description")
  private String description = null;

  @JsonProperty("step_num")
  private Integer stepNum = null;

  @JsonProperty("wait_time_mins")
  private Integer waitTimeMins = null;

  public StepDto media(IdDto media) {
    this.media = media;
    return this;
  }

  /**
   * Get media
   * @return media
   **/
  
    @Valid
    public IdDto getMedia() {
    return media;
  }

  public void setMedia(IdDto media) {
    this.media = media;
  }

  public StepDto description(String description) {
    this.description = description;
    return this;
  }

  /**
   * Содержимое шага
   * @return description
   **/
      @NotNull

    public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public StepDto stepNum(Integer stepNum) {
    this.stepNum = stepNum;
    return this;
  }

  /**
   * Get stepNum
   * @return stepNum
   **/
      @NotNull

    public Integer getStepNum() {
    return stepNum;
  }

  public void setStepNum(Integer stepNum) {
    this.stepNum = stepNum;
  }

  public StepDto waitTimeMins(Integer waitTimeMins) {
    this.waitTimeMins = waitTimeMins;
    return this;
  }

  /**
   * Get waitTimeMins
   * @return waitTimeMins
   **/
  
    public Integer getWaitTimeMins() {
    return waitTimeMins;
  }

  public void setWaitTimeMins(Integer waitTimeMins) {
    this.waitTimeMins = waitTimeMins;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StepDto stepDto = (StepDto) o;
    return Objects.equals(this.media, stepDto.media) &&
        Objects.equals(this.description, stepDto.description) &&
        Objects.equals(this.stepNum, stepDto.stepNum) &&
        Objects.equals(this.waitTimeMins, stepDto.waitTimeMins);
  }

  @Override
  public int hashCode() {
    return Objects.hash(media, description, stepNum, waitTimeMins);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StepDto {\n");
    
    sb.append("    media: ").append(toIndentedString(media)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    stepNum: ").append(toIndentedString(stepNum)).append("\n");
    sb.append("    waitTimeMins: ").append(toIndentedString(waitTimeMins)).append("\n");
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

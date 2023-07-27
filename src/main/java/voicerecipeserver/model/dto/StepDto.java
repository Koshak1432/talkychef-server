package voicerecipeserver.model.dto;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

/**
 * StepDto
 */
@Validated
@jakarta.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2023-07-27T05:11:52.897277956Z[GMT]")


public class StepDto   {
  @JsonProperty("media_id")
  private Long mediaId = null;

  @JsonProperty("description")
  private String description = null;

  @JsonProperty("step_num")
  private Integer stepNum = null;

  @JsonProperty("wait_time_mins")
  private Integer waitTimeMins = null;

  public StepDto mediaId(Long mediaId) {
    this.mediaId = mediaId;
    return this;
  }

  /**
   * Get mediaId
   * @return mediaId
   **/
  
    public Long getMediaId() {
    return mediaId;
  }

  public void setMediaId(Long mediaId) {
    this.mediaId = mediaId;
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
    return Objects.equals(this.mediaId, stepDto.mediaId) &&
        Objects.equals(this.description, stepDto.description) &&
        Objects.equals(this.stepNum, stepDto.stepNum) &&
        Objects.equals(this.waitTimeMins, stepDto.waitTimeMins);
  }

  @Override
  public int hashCode() {
    return Objects.hash(mediaId, description, stepNum, waitTimeMins);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StepDto {\n");
    
    sb.append("    mediaId: ").append(toIndentedString(mediaId)).append("\n");
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

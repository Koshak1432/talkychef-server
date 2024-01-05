package voicerecipeserver.model.dto;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

/**
 * TimeDto
 */
@Validated
@jakarta.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2023-12-06T07:36:51.008951992Z[GMT]")

@NoArgsConstructor
@Getter
@Setter
@ToString
public class TimeDto   {
  @JsonProperty("id")
  private Integer id = null;
  @JsonProperty("time")
  private String time = null;

  public TimeDto time(String time) {
    this.time = time;
    return this;
  }
}

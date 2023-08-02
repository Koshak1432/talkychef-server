package voicerecipeserver.model.dto;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import voicerecipeserver.model.dto.RecipeDto;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

/**
 * CollectionDto
 */
@Validated
@jakarta.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2023-08-02T05:33:01.688027473Z[GMT]")


public class CollectionDto   {
  @JsonProperty("name")
  private String name = null;

  @JsonProperty("number")
  private Integer number = null;

  @JsonProperty("id")
  private Integer id = null;

  @JsonProperty("media_id")
  private Long mediaId = null;

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

  public CollectionDto number(Integer number) {
    this.number = number;
    return this;
  }

  /**
   * Get number
   * @return number
   **/
  @NotNull

  public Integer getNumber() {
    return number;
  }

  public void setNumber(Integer number) {
    this.number = number;
  }

  public CollectionDto id(Integer id) {
    this.id = id;
    return this;
  }

  /**
   * Get id
   * @return id
   **/
  @NotNull

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }


  public CollectionDto mediaId(Long mediaId) {
    this.mediaId = mediaId;
    return this;
  }

  /**
   * Get mediaId
   * @return mediaId
   **/
  @NotNull

  public Long getMediaId() {
    return mediaId;
  }

  public void setMediaId(Long mediaId) {
    this.mediaId = mediaId;
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
            Objects.equals(this.number, collectionDto.number) &&
            Objects.equals(this.id, collectionDto.id) &&
            Objects.equals(this.mediaId, collectionDto.mediaId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, number, id, mediaId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CollectionDto {\n");

    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    number: ").append(toIndentedString(number)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    mediaId: ").append(toIndentedString(mediaId)).append("\n");
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
package talkychefserver.model.dto;

import java.time.LocalDateTime;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

/**
 * CommentDto
 */
@Validated
@jakarta.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2023-07-27T02:48:13.656840513Z[GMT]")


public class CommentDto   {
  @JsonProperty("id")
  private Long id = null;

  @JsonProperty("user_uid")
  private String userUid = null;

  @JsonProperty("recipe_id")
  private Long recipeId = null;

  @JsonProperty("post_time")
  private LocalDateTime postTime = null;

  @JsonProperty("content")
  private String content = null;

  public CommentDto id(Long id) {
    this.id = id;
    return this;
  }

  /**
   * Get id
   * @return id
   **/
      @NotNull

    public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public CommentDto userUid(String userUid) {
    this.userUid = userUid;
    return this;
  }

  /**
   * Get userUid
   * @return userUid
   **/
      @NotNull

    public String getUserUid() {
    return userUid;
  }

  public void setUserUid(String userUid) {
    this.userUid = userUid;
  }

  public CommentDto recipeId(Long recipeId) {
    this.recipeId = recipeId;
    return this;
  }

  /**
   * Get recipeId
   * @return recipeId
   **/
      @NotNull

    public Long getRecipeId() {
    return recipeId;
  }

  public void setRecipeId(Long recipeId) {
    this.recipeId = recipeId;
  }

  public CommentDto postTime(LocalDateTime postTime) {
    this.postTime = postTime;
    return this;
  }

  /**
   * Timestamp defined by RFC3339(ISO 8601)
   * @return postTime
   **/
  
    @Valid
    public LocalDateTime getPostTime() {
    return postTime;
  }

  public void setPostTime(LocalDateTime postTime) {
    this.postTime = postTime;
  }

  public CommentDto content(String content) {
    this.content = content;
    return this;
  }

  /**
   * Get content
   * @return content
   **/
      @NotNull

    public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CommentDto commentDto = (CommentDto) o;
    return Objects.equals(this.id, commentDto.id) &&
        Objects.equals(this.userUid, commentDto.userUid) &&
        Objects.equals(this.recipeId, commentDto.recipeId) &&
        Objects.equals(this.postTime, commentDto.postTime) &&
        Objects.equals(this.content, commentDto.content);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, userUid, recipeId, postTime, content);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CommentDto {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    userUid: ").append(toIndentedString(userUid)).append("\n");
    sb.append("    recipeId: ").append(toIndentedString(recipeId)).append("\n");
    sb.append("    postTime: ").append(toIndentedString(postTime)).append("\n");
    sb.append("    content: ").append(toIndentedString(content)).append("\n");
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

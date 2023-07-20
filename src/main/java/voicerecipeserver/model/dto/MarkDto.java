package voicerecipeserver.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.validation.annotation.Validated;
import jakarta.annotation.Generated;

import jakarta.validation.constraints.*;
import java.util.Objects;

/**
 * MarkDto
 */
@Validated
@Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2023-07-19T08:28:37.528548601Z[GMT]")


public class MarkDto   {
    @JsonProperty("user_uid")
    private String userUid = null;

    @JsonProperty("recipe_id")
    private Long recipeId = null;

    @JsonProperty("mark")
    private Integer mark = null;

    public MarkDto userUid(String userUid) {
        this.userUid = userUid;
        return this;
    }

    /**
     * Get userUid
     * @return userUid
     **/
    @NotNull

    @Size(max=32)   public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public MarkDto recipeId(Long recipeId) {
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

    public MarkDto mark(Integer mark) {
        this.mark = mark;
        return this;
    }

    /**
     * Оценка от 1 до 5
     * @return mark
     **/
    @NotNull

    public Integer getMark() {
        return mark;
    }

    public void setMark(Integer mark) {
        this.mark = mark;
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MarkDto markDto = (MarkDto) o;
        return Objects.equals(this.userUid, markDto.userUid) &&
                Objects.equals(this.recipeId, markDto.recipeId) &&
                Objects.equals(this.mark, markDto.mark);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userUid, recipeId, mark);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class MarkDto {\n");

        sb.append("    userUid: ").append(toIndentedString(userUid)).append("\n");
        sb.append("    recipeId: ").append(toIndentedString(recipeId)).append("\n");
        sb.append("    mark: ").append(toIndentedString(mark)).append("\n");
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
package voicerecipeserver.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.validation.annotation.Validated;
import voicerecipeserver.model.dto.IdDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

/**
 * MarksDto
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2023-07-05T07:25:54.097643084Z[GMT]")


public class MarksDto {
    @JsonProperty("id")
    private Long id = null;

    @JsonProperty("user_id")
    private String userId = null;

    @JsonProperty("recipe_id")
    private IdDto recipeId = null;

    @JsonProperty("mark")
    private Integer mark = null;

    public MarksDto id(Long id) {
        this.id = id;
        return this;
    }

    /**
     * Get id
     *
     * @return id
     **/

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public MarksDto userId(String authorId) {
        this.userId = authorId;
        return this;
    }

    /**
     * Get user
     *
     * @return user
     **/

    @NotNull
    @Size(max = 32)
    public String getUserId() {
        return userId;
    }

    public void setUserId(String author) {
        this.userId = author;
    }

    public MarksDto recipeId(IdDto recipeId) {
        this.recipeId = recipeId;
        return this;
    }

    /**
     * Get recipeId
     *
     * @return recipeId
     **/
    @Valid
    public IdDto getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(IdDto recipeId) {
        this.recipeId = recipeId;
    }

    public MarksDto mark(Integer mark) {
        this.mark = mark;
        return this;
    }

    /**
     * Get mark
     *
     * @return mark
     **/

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
        MarksDto marksDto = (MarksDto) o;
        return Objects.equals(this.id, marksDto.id) &&
                Objects.equals(this.userId, marksDto.userId) &&
                Objects.equals(this.recipeId, marksDto.recipeId) &&
                Objects.equals(this.mark, marksDto.mark);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, recipeId, mark);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class MarksDto {\n");

        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    author: ").append(toIndentedString(userId)).append("\n");
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

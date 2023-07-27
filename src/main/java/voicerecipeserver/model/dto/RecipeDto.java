package voicerecipeserver.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * RecipeDto
 */
@Validated
@jakarta.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2023-07-24T04:20:59.334595174Z[GMT]")


public class RecipeDto {
    @JsonProperty("name")
    private String name = null;

    @JsonProperty("id")
    private Long id = null;

    @JsonProperty("media")
    private IdDto media = null;

    @JsonProperty("cook_time_mins")
    private Integer cookTimeMins = null;

    @JsonProperty("author_uid")
    private String authorUid = null;

    @JsonProperty("prep_time_mins")
    private Integer prepTimeMins = null;

    @JsonProperty("kilocalories")
    private Double kilocalories = null;

    @JsonProperty("proteins")
    private Double proteins = null;

    @JsonProperty("fats")
    private Double fats = null;

    @JsonProperty("carbohydrates")
    private Double carbohydrates = null;

    @JsonProperty("ingredients_distributions")
    @Valid
    private List<IngredientsDistributionDto> ingredientsDistributions = null;

    @JsonProperty("steps")
    @Valid
    private List<StepDto> steps = null;

    @JsonProperty("avg_mark")
    private Float avgMark = null;

    @JsonProperty("user_mark")
    private Short userMark = null;

    public RecipeDto name(String name) {
        this.name = name;
        return this;
    }

    /**
     * Get name
     *
     * @return name
     **/
    @NotNull

    @Size(max = 128)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RecipeDto id(Long id) {
        this.id = id;
        return this;
    }

    /**
     * Get id
     *
     * @return id
     **/
    @NotNull

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RecipeDto media(IdDto media) {
        this.media = media;
        return this;
    }

    /**
     * Get media
     *
     * @return media
     **/
    @NotNull

    @Valid
    public IdDto getMedia() {
        return media;
    }

    public void setMedia(IdDto media) {
        this.media = media;
    }

    public RecipeDto cookTimeMins(Integer cookTimeMins) {
        this.cookTimeMins = cookTimeMins;
        return this;
    }

    /**
     * Get cookTimeMins
     *
     * @return cookTimeMins
     **/
    @NotNull

    public Integer getCookTimeMins() {
        return cookTimeMins;
    }

    public void setCookTimeMins(Integer cookTimeMins) {
        this.cookTimeMins = cookTimeMins;
    }

    public RecipeDto authorUid(String authorUid) {
        this.authorUid = authorUid;
        return this;
    }

    /**
     * Get authorUid
     *
     * @return authorUid
     **/
    @NotNull

    @Size(max = 32)
    public String getAuthorUid() {
        return authorUid;
    }

    public void setAuthorUid(String authorUid) {
        this.authorUid = authorUid;
    }

    public RecipeDto prepTimeMins(Integer prepTimeMins) {
        this.prepTimeMins = prepTimeMins;
        return this;
    }

    /**
     * Get prepTimeMins
     *
     * @return prepTimeMins
     **/

    public Integer getPrepTimeMins() {
        return prepTimeMins;
    }

    public void setPrepTimeMins(Integer prepTimeMins) {
        this.prepTimeMins = prepTimeMins;
    }

    public RecipeDto kilocalories(Double kilocalories) {
        this.kilocalories = kilocalories;
        return this;
    }

    /**
     * Get kilocalories
     *
     * @return kilocalories
     **/

    public Double getKilocalories() {
        return kilocalories;
    }

    public void setKilocalories(Double kilocalories) {
        this.kilocalories = kilocalories;
    }

    public RecipeDto proteins(Double proteins) {
        this.proteins = proteins;
        return this;
    }

    /**
     * Get proteins
     *
     * @return proteins
     **/

    public Double getProteins() {
        return proteins;
    }

    public void setProteins(Double proteins) {
        this.proteins = proteins;
    }

    public RecipeDto fats(Double fats) {
        this.fats = fats;
        return this;
    }

    /**
     * Get fats
     *
     * @return fats
     **/

    public Double getFats() {
        return fats;
    }

    public void setFats(Double fats) {
        this.fats = fats;
    }

    public RecipeDto carbohydrates(Double carbohydrates) {
        this.carbohydrates = carbohydrates;
        return this;
    }

    /**
     * Get carbohydrates
     *
     * @return carbohydrates
     **/

    public Double getCarbohydrates() {
        return carbohydrates;
    }

    public void setCarbohydrates(Double carbohydrates) {
        this.carbohydrates = carbohydrates;
    }

    public RecipeDto ingredientsDistributions(List<IngredientsDistributionDto> ingredientsDistributions) {
        this.ingredientsDistributions = ingredientsDistributions;
        return this;
    }

    public RecipeDto addIngredientsDistributionsItem(IngredientsDistributionDto ingredientsDistributionsItem) {
        if (this.ingredientsDistributions == null) {
            this.ingredientsDistributions = new ArrayList<IngredientsDistributionDto>();
        }
        this.ingredientsDistributions.add(ingredientsDistributionsItem);
        return this;
    }

    /**
     * Get ingredientsDistributions
     *
     * @return ingredientsDistributions
     **/
    @Valid
    public List<IngredientsDistributionDto> getIngredientsDistributions() {
        return ingredientsDistributions;
    }

    public void setIngredientsDistributions(List<IngredientsDistributionDto> ingredientsDistributions) {
        this.ingredientsDistributions = ingredientsDistributions;
    }

    public RecipeDto steps(List<StepDto> steps) {
        this.steps = steps;
        return this;
    }

    public RecipeDto addStepsItem(StepDto stepsItem) {
        if (this.steps == null) {
            this.steps = new ArrayList<StepDto>();
        }
        this.steps.add(stepsItem);
        return this;
    }

    /**
     * Get steps
     *
     * @return steps
     **/
    @Valid
    public List<StepDto> getSteps() {
        return steps;
    }

    public void setSteps(List<StepDto> steps) {
        this.steps = steps;
    }

    public RecipeDto avgMark(Float avgMark) {
        this.avgMark = avgMark;
        return this;
    }

    /**
     * Get avgMark
     *
     * @return avgMark
     **/

    public Float getAvgMark() {
        return avgMark;
    }

    public void setAvgMark(Float avgMark) {
        this.avgMark = avgMark;
    }

    public RecipeDto userMark(Short userMark) {
        this.userMark = userMark;
        return this;
    }

    /**
     * Get userMark
     *
     * @return userMark
     **/

    public Short getUserMark() {
        return userMark;
    }

    public void setUserMark(Short userMark) {
        this.userMark = userMark;
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RecipeDto recipeDto = (RecipeDto) o;
        return Objects.equals(this.name, recipeDto.name) &&
                Objects.equals(this.id, recipeDto.id) &&
                Objects.equals(this.media, recipeDto.media) &&
                Objects.equals(this.cookTimeMins, recipeDto.cookTimeMins) &&
                Objects.equals(this.authorUid, recipeDto.authorUid) &&
                Objects.equals(this.prepTimeMins, recipeDto.prepTimeMins) &&
                Objects.equals(this.kilocalories, recipeDto.kilocalories) &&
                Objects.equals(this.proteins, recipeDto.proteins) &&
                Objects.equals(this.fats, recipeDto.fats) &&
                Objects.equals(this.carbohydrates, recipeDto.carbohydrates) &&
                Objects.equals(this.ingredientsDistributions, recipeDto.ingredientsDistributions) &&
                Objects.equals(this.steps, recipeDto.steps) &&
                Objects.equals(this.avgMark, recipeDto.avgMark) &&
                Objects.equals(this.userMark, recipeDto.userMark);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, id, media, cookTimeMins, authorUid, prepTimeMins, kilocalories, proteins, fats, carbohydrates, ingredientsDistributions, steps, avgMark, userMark);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(toIndentedString(name)).append(" ");
        sb.append(toIndentedString(avgMark)).append("\n");
        return sb.toString();
    }
//    @Override
//    public String toString() {
//        StringBuilder sb = new StringBuilder();
//        sb.append("class RecipeDto {\n");
//
//        sb.append("    name: ").append(toIndentedString(name)).append("\n");
//        sb.append("    id: ").append(toIndentedString(id)).append("\n");
//        sb.append("    media: ").append(toIndentedString(media)).append("\n");
//        sb.append("    cookTimeMins: ").append(toIndentedString(cookTimeMins)).append("\n");
//        sb.append("    authorUid: ").append(toIndentedString(authorUid)).append("\n");
//        sb.append("    prepTimeMins: ").append(toIndentedString(prepTimeMins)).append("\n");
//        sb.append("    kilocalories: ").append(toIndentedString(kilocalories)).append("\n");
//        sb.append("    proteins: ").append(toIndentedString(proteins)).append("\n");
//        sb.append("    fats: ").append(toIndentedString(fats)).append("\n");
//        sb.append("    carbohydrates: ").append(toIndentedString(carbohydrates)).append("\n");
//        sb.append("    ingredientsDistributions: ").append(toIndentedString(ingredientsDistributions)).append("\n");
//        sb.append("    steps: ").append(toIndentedString(steps)).append("\n");
//        sb.append("    avgMark: ").append(toIndentedString(avgMark)).append("\n");
//        sb.append("    userMark: ").append(toIndentedString(userMark)).append("\n");
//        sb.append("}");
//        return sb.toString();
//    }

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
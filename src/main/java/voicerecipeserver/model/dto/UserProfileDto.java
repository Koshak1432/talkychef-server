package voicerecipeserver.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

import java.util.Objects;

/**
 * UserProfileDto
 */
@Validated
@jakarta.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2023-07-24T05:31:24.713175498Z[GMT]")


public class UserProfileDto   {
  @JsonProperty("uid")
  private String uid = null;

  @JsonProperty("display_name")
  private String displayName = null;

  @JsonProperty("image")
  private IdDto image = null;

  @JsonProperty("info")
  private String info = null;

  @JsonProperty("tg_link")
  private String tgLink = null;

  @JsonProperty("vk_link")
  private String vkLink = null;

  public UserProfileDto uid(String uid) {
    this.uid = uid;
    return this;
  }

  /**
   * Get uid
   * @return uid
   **/
      @NotNull

    public String getUid() {
    return uid;
  }

  public void setUid(String uid) {
    this.uid = uid;
  }

  public UserProfileDto displayName(String displayName) {
    this.displayName = displayName;
    return this;
  }

  /**
   * Get displayName
   * @return displayName
   **/
      @NotNull

    public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public UserProfileDto image(IdDto image) {
    this.image = image;
    return this;
  }

  /**
   * Get image
   * @return image
   **/
      @NotNull

    @Valid
    public IdDto getImage() {
    return image;
  }

  public void setImage(IdDto image) {
    this.image = image;
  }

  public UserProfileDto info(String info) {
    this.info = info;
    return this;
  }

  /**
   * Get info
   * @return info
   **/
      @NotNull

    public String getInfo() {
    return info;
  }

  public void setInfo(String info) {
    this.info = info;
  }

  public UserProfileDto tgLink(String tgLink) {
    this.tgLink = tgLink;
    return this;
  }

  /**
   * Get tgLink
   * @return tgLink
   **/

    public String getTgLink() {
    return tgLink;
  }

  public void setTgLink(String tgLink) {
    this.tgLink = tgLink;
  }

  public UserProfileDto vkLink(String vkLink) {
    this.vkLink = vkLink;
    return this;
  }

  /**
   * Get vkLink
   * @return vkLink
   **/

    public String getVkLink() {
    return vkLink;
  }

  public void setVkLink(String vkLink) {
    this.vkLink = vkLink;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserProfileDto userProfileDto = (UserProfileDto) o;
    return Objects.equals(this.uid, userProfileDto.uid) &&
        Objects.equals(this.displayName, userProfileDto.displayName) &&
        Objects.equals(this.image, userProfileDto.image) &&
        Objects.equals(this.info, userProfileDto.info) &&
        Objects.equals(this.tgLink, userProfileDto.tgLink) &&
        Objects.equals(this.vkLink, userProfileDto.vkLink);
  }

  @Override
  public int hashCode() {
    return Objects.hash(uid, displayName, image, info, tgLink, vkLink);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserProfileDto {\n");

    sb.append("    uid: ").append(toIndentedString(uid)).append("\n");
    sb.append("    displayName: ").append(toIndentedString(displayName)).append("\n");
    sb.append("    image: ").append(toIndentedString(image)).append("\n");
    sb.append("    info: ").append(toIndentedString(info)).append("\n");
    sb.append("    tgLink: ").append(toIndentedString(tgLink)).append("\n");
    sb.append("    vkLink: ").append(toIndentedString(vkLink)).append("\n");
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

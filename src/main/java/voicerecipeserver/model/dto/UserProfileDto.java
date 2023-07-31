package voicerecipeserver.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

import java.util.Objects;

/**
 * UserProfileDto
 */
@Validated
@jakarta.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2023-07-28T08:25:35.293425098Z[GMT]")


public class UserProfileDto   {
  @JsonProperty("uid")
  private String uid = null;

  @JsonProperty("display_name")
  private String displayName = null;

  @JsonProperty("media_id")
  private Long mediaId = null;

  @JsonProperty("info")
  private String info = null;

  @JsonProperty("tg_link")
  private String tgLink = null;

  @JsonProperty("vk_link")
  private String vkLink = null;

  @JsonProperty("email")
  private String email = null;

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

  public UserProfileDto mediaId(Long mediaId) {
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

  public UserProfileDto email(String email) {
    this.email = email;
    return this;
  }

  /**
   * Get email
   * @return email
   **/
  @NotNull

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
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
            Objects.equals(this.mediaId, userProfileDto.mediaId) &&
            Objects.equals(this.info, userProfileDto.info) &&
            Objects.equals(this.tgLink, userProfileDto.tgLink) &&
            Objects.equals(this.vkLink, userProfileDto.vkLink) &&
            Objects.equals(this.email, userProfileDto.email);
  }

  @Override
  public int hashCode() {
    return Objects.hash(uid, displayName, mediaId, info, tgLink, vkLink, email);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserProfileDto {\n");

    sb.append("    uid: ").append(toIndentedString(uid)).append("\n");
    sb.append("    displayName: ").append(toIndentedString(displayName)).append("\n");
    sb.append("    mediaId: ").append(toIndentedString(mediaId)).append("\n");
    sb.append("    info: ").append(toIndentedString(info)).append("\n");
    sb.append("    tgLink: ").append(toIndentedString(tgLink)).append("\n");
    sb.append("    vkLink: ").append(toIndentedString(vkLink)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
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
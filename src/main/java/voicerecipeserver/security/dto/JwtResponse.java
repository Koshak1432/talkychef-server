package voicerecipeserver.security.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Generated;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotNull;
import java.util.Objects;

/**
 * JwtResponse
 */
@Validated
@Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2023-07-12T04:39:14.314897533Z[GMT]")


public class JwtResponse   {
    @JsonProperty("type")
    private String type = "Bearer";

    @JsonProperty("accessToken")
    private String accessToken = null;


    public JwtResponse type(String type) {
        this.type = type;
        return this;
    }

    /**
     * Get type
     * @return type
     **/
    @NotNull

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public JwtResponse accessToken(String accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    /**
     * Get accessToken
     * @return accessToken
     **/
    @NotNull

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        JwtResponse jwtResponse = (JwtResponse) o;
        return Objects.equals(this.type, jwtResponse.type) &&
                Objects.equals(this.accessToken, jwtResponse.accessToken);}

    @Override
    public int hashCode() {
        return Objects.hash(type, accessToken);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class JwtResponse {\n");

        sb.append("    type: ").append(toIndentedString(type)).append("\n");
        sb.append("    accessToken: ").append(toIndentedString(accessToken)).append("\n");
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
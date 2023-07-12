package voicerecipeserver.security.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.validation.annotation.Validated;

import java.util.Objects;

/**
 * JwtRequest
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2023-07-12T04:39:14.314897533Z[GMT]")


public class JwtRequest   {
    @JsonProperty("login")
    private String login = null;

    @JsonProperty("password")
    private String password = null;

    public JwtRequest login(String login) {
        this.login = login;
        return this;
    }

    /**
     * Get login
     * @return login
     **/

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public JwtRequest password(String password) {
        this.password = password;
        return this;
    }

    /**
     * Get password
     * @return password
     **/

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        JwtRequest jwtRequest = (JwtRequest) o;
        return Objects.equals(this.login, jwtRequest.login) &&
                Objects.equals(this.password, jwtRequest.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login, password);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class JwtRequest {\n");

        sb.append("    login: ").append(toIndentedString(login)).append("\n");
        sb.append("    password: ").append(toIndentedString(password)).append("\n");
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
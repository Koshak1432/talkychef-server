package voicerecipeserver.model.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@Setter
@Getter
@Data
public class MarkKey implements Serializable {
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "recipe_id")
    private Long recipeId;

}
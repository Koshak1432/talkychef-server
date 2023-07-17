package voicerecipeserver.model.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import jakarta.persistence.*;

@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "avg_marks")
public class AvgMark {
    @Id
    Long recipeId;

    @Column(name = "avg_mark")
    private Float avgMark;

    @Column(name = "quantity")
    private Long quantity;

    @ToString.Exclude
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id", referencedColumnName = "id")
    private Recipe recipe;

}

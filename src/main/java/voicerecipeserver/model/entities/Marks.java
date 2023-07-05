package voicerecipeserver.model.entities;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
@Entity
@Table(name = "marks")
public class Marks {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private User user;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id")
    @ToString.Exclude
    private Recipe recipe;

    private Integer mark;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Marks marks = (Marks) o;
        return Objects.equals(id, marks.id) && Objects.equals(user, marks.user) && Objects.equals(recipe, marks.recipe) && Objects.equals(mark, marks.mark);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, recipe, mark);
    }
}


package talkychefserver.model.entities;

import lombok.*;

import jakarta.persistence.*;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "marks")
public class Mark {
    @EmbeddedId
    private MarkKey id;

    @Column(name = "mark")
    private Short mark;

    @ManyToOne
    @ToString.Exclude

    @MapsId("userId")
    private User user;

    @ManyToOne
    @ToString.Exclude
    @MapsId("recipeId")
    private Recipe recipe;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mark mark1 = (Mark) o;
        return id.equals(mark1.id) && mark.equals(mark1.mark);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, mark);
    }
}



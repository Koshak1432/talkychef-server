package talkychefserver.model.entities;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name =  "measure_units")
public class MeasureUnit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(unique = true)
    private String name;


    @OneToMany(mappedBy="unit")
    @ToString.Exclude
    private List<IngredientsDistribution> ingredientsDistributions;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MeasureUnit unit)) return false;
        return getId() == unit.getId();
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

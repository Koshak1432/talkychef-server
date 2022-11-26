package voicerecipeserver.model.entities;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@Table(name = "measure_units_sets")
public class MeasureUnitsSet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ToString.Exclude
    @ManyToMany
    @JoinTable(name = "measure_units_distribution",
                joinColumns = @JoinColumn(name = "set_id"),
                inverseJoinColumns = @JoinColumn(name = "unit_id"))
    private Set<MeasureUnit> units;

    @OneToMany(mappedBy = "set",orphanRemoval = false)
    @ToString.Exclude
    private List<Ingredient> ingredientList;

    public MeasureUnitsSet id(Long id){
        this.id = id;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MeasureUnitsSet that = (MeasureUnitsSet) o;
        return getId() == that.getId();
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

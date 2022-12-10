package voicerecipeserver.model.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "recipes")
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @Column(name = "cook_time_mins")
    private @NotNull Double cookTimeMins;

    @Column(name = "prep_time_mins")
    private Double prepTimeMins;

    private Double kilocalories;

    private Double proteins;

    private Double fats;

    private Double carbohydrates;


    @OneToMany(mappedBy = "recipe" , orphanRemoval = true, cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Step> steps;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "recipe", orphanRemoval = true)
    @ToString.Exclude
    private List<IngredientsDistribution> ingredientsDistributions;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "media_id")
    @ToString.Exclude
    private Media media;

    @ManyToMany(mappedBy = "recipes")
    @ToString.Exclude
    private Set<Category> categories;

    @ManyToMany(mappedBy = "recipes")
    @ToString.Exclude
    private List<Collection> collections;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    @ToString.Exclude
    private User author;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recipe recipe = (Recipe) o;
        return getId() == recipe.getId();
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public void removeStep(Step step){
        steps.remove(step);
        step.setRecipe(null);
    }

    public void addStep(Step step){
        steps.add(step);
        step.setRecipe(this);
    }

}

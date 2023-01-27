package voicerecipeserver.model.entities;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity(name = "collections")
public class Collection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Integer number;
    @ManyToMany(cascade =   {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "collections_distribution",
            joinColumns = @JoinColumn(name = "collection_id"),
            inverseJoinColumns = @JoinColumn(name = "recipe_id")
    )
    @ToString.Exclude
    @Setter(AccessLevel.NONE)
    private Set<Recipe> recipes;

    public void addRecipe(Recipe recipe){
        if(!recipes.contains(recipe)){
            recipes.add(recipe);
            recipe.getCollections().add(this);
            number++;
        }
    }

    public void removeRecipe(Recipe recipe){
        if(recipes.contains(recipe)){
            recipes.add(recipe);
            recipe.getCollections().remove(this);
            number--;
        }
    }

}

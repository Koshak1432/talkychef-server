package voicerecipeserver.model.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name;

    @ToString.Exclude
    @ManyToMany
    @JoinTable(name = "categories_distribution", joinColumns = @JoinColumn(name = "category_id"), inverseJoinColumns
            = @JoinColumn(name = "recipe_id"))
    private List<Recipe> recipes;


    @ManyToMany
    @JoinTable(name = "selections_distribution", joinColumns = @JoinColumn(name = "category_id"), inverseJoinColumns =
    @JoinColumn(name = "selection_id"))
    private List<Selection> selections = new ArrayList<>();

}

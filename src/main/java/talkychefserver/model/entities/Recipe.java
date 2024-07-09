package talkychefserver.model.entities;

import lombok.*;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Builder
@Table(name = "recipes")
@AllArgsConstructor
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @Column(name = "cook_time_mins")
    private Integer cookTimeMins;

    @Column(name = "prep_time_mins")
    private Integer prepTimeMins;

    private Double kilocalories;

    private Double proteins;

    private Double fats;

    private Double carbohydrates;

    private Integer servings;

    private Long totalWeight;

    @OneToMany(mappedBy = "recipe", orphanRemoval = true, cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Step> steps;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "recipe", orphanRemoval = true)
    @ToString.Exclude
    private List<IngredientsDistribution> ingredientsDistributions;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "media_id")
    @ToString.Exclude
    private Media media;

    @ManyToMany(mappedBy = "recipes")
    @ToString.Exclude
    private List<Category> categories;

    @ManyToMany(mappedBy = "recipes")
    @ToString.Exclude
    private List<Collection> collections;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    @ToString.Exclude
    private User author;

    @ToString.Exclude
    @OneToMany(mappedBy = "recipe", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Mark> marks;

    @OneToOne(mappedBy = "recipe", orphanRemoval = true, cascade = CascadeType.ALL)
    private AvgMark avgMark;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Comment> comments;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recipe recipe = (Recipe) o;
        return Objects.equals(getId(), recipe.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

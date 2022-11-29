package voicerecipeserver.model.entities;


import lombok.*;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Builder
@Table(name = "ingredients")
public class Ingredient {
    // TODO Hibernate disables JDBC insert batching when using the IDENTITY generator strategy - проверить воздействие.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // TODO разобраться с NaturalId, для других сущностей тоже.
    private String name;


    @OneToMany(mappedBy = "ingredient", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<IngredientsDistribution> ingredientsDistributionList;

}

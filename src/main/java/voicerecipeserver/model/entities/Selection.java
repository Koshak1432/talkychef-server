package voicerecipeserver.model.entities;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "selections")
public class Selection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToMany(mappedBy = "selections")
    private List<Category> categories;
}

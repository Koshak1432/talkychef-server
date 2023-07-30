package voicerecipeserver.model.entities;

import lombok.*;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
@Entity
@Table(name = "recipe_steps")
public class Step {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "media_id")
    @ToString.Exclude
    private Media media;

    @NotNull
    private String description;

    @NotNull
    @Column(name = "step_num")
    private Integer stepNum;

    @Column(name = "wait_time_mins")
    private Integer waitTimeMins;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id")
    @ToString.Exclude
    private Recipe recipe;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Step step)) return false;
        return getId() == step.getId();
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

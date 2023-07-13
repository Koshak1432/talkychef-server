package voicerecipeserver.model.entities;


import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "media")
@Builder
public class Media {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id")
    @ToString.Exclude
    private MediaType mediaType;


    @Column(name = "file_data", columnDefinition="BLOB")
//    @Type(type = "org.hibernate.type.BinaryType")
    @NotNull
    private byte[] fileData;

    @OneToOne(mappedBy="media", orphanRemoval = true)
    @ToString.Exclude
    private Recipe recipe;

    @OneToOne(mappedBy="media", orphanRemoval = true)
    @ToString.Exclude
    private Step step;

    //TODO add/remove methods for bidirect.

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Media media = (Media) o;
        return getId() == media.getId();
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }


}

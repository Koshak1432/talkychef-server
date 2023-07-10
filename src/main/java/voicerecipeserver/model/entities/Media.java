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
    @Lob
    @Type(type = "org.hibernate.type.BinaryType")
    @NotNull
    private byte[] fileData;

    @OneToOne(fetch = FetchType.LAZY, mappedBy="media", cascade = CascadeType.REMOVE)
    @ToString.Exclude
    private Recipe recipe;

    @OneToOne(fetch = FetchType.LAZY, mappedBy="media", cascade = CascadeType.REMOVE)
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

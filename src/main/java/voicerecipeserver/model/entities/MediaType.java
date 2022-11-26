package voicerecipeserver.model.entities;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
@Entity
@Table(name = "media_types")
public class MediaType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //avoid one-to-many, It can kill the DB with overXXX images/videos?
    @Column(name = "mime_type")
    @NotNull
    private String mimeType;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MediaType mediaType = (MediaType) o;
        return getId() == mediaType.getId();
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }


}

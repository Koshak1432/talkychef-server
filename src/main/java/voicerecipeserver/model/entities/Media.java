package voicerecipeserver.model.entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;

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

    @Column(name = "file_data", columnDefinition = "BLOB")
    @JdbcTypeCode(Types.VARBINARY)
    @NotNull
    private byte[] fileData;

    @OneToOne(mappedBy = "media", orphanRemoval = true)
    @ToString.Exclude
    private Recipe recipe;

    @OneToOne(mappedBy = "media", orphanRemoval = true)
    @ToString.Exclude
    private Step step;

    @ToString.Exclude
    @OneToOne(mappedBy = "profileImage", cascade = CascadeType.ALL, orphanRemoval = true)
    private UserInfo userInfo;

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

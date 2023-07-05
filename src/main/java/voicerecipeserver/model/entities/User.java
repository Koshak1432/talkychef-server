package voicerecipeserver.model.entities;


import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String uid;

    @Column(name = "display_name")
    private String displayName;

    private String info;

    @Column(name = "ok_link")
    private String okLink;

    @Column(name = "tg_link")
    private String tgLink;

    @Column(name = "vk_link")
    private String vkLink;

    @OneToMany(mappedBy = "author")
    @ToString.Exclude
    private List<Recipe> recipes;

    @ToString.Exclude
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

}

package voicerecipeserver.model.entities;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

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

    @Column(name = "uid")
    private String uid;

    @Column(name = "password")
    private String password;

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
    private List<Mark> marks;

    @ToString.Exclude
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    @ManyToMany
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns =
    @JoinColumn(name = "role_id"))
    @ToString.Exclude
    private Set<Role> roles;

    public User(String uid, String password, String displayName, Set<Role> roles) {
        this.uid = uid;
        this.password = password;
        this.displayName = displayName;
        this.roles = roles;
    }

}

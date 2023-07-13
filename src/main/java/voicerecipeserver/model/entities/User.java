package voicerecipeserver.model.entities;


import javax.persistence.*;
import lombok.*;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
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
    private String uid;
    @Column(name = "login")
    private String login;
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
    @ToString.Include
    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

    public User(String login, String password, String displayName, Set<Role> roles) {
        this.login = login;
        this.password = password;
        this.displayName = displayName;
        this.roles = roles;
    }

}

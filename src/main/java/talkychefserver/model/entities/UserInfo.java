package voicerecipeserver.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "user_info")
public class UserInfo {
    @Id
    private Long id;

    @Column(name = "info", length = 1024)
    private String info;

    @Column(name = "tg_link")
    private String tgLink;

    @Column(name = "vk_link")
    private String vkLink;

    @Column(name = "email")
    private String email;

    @Column(name = "token")
    private String token;

    @Column(name = "display_name")
    private String displayName;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @MapsId
    @ToString.Exclude
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id")
    @ToString.Exclude
    private Media media;



}

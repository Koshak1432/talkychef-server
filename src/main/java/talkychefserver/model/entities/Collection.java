package talkychefserver.model.entities;

import lombok.*;

import jakarta.persistence.*;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity(name = "collections")
public class Collection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Integer number;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    @ToString.Exclude
    private User author;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "media_id")
    @ToString.Exclude
    private Media media;

/**
 * @deprecated
 * Будет подгружаться вся коллекция. Если во всей коллекции нет нужды, лучше использовать другие методы
 * Добавление/удаление рецептов через коллекцию не будет работать - сделано специально, чтобы поле использовалось только как read-only
 * */
    @ManyToMany
    @JoinTable(
            name = "collections_distribution",
            joinColumns = @JoinColumn(name = "collection_id"),
            inverseJoinColumns = @JoinColumn(name = "recipe_id")
    )
    @ToString.Exclude
    @Setter(AccessLevel.NONE)
    @Deprecated()
    private Set<Recipe> recipes;

    public Collection(String name, Integer number, User author) {
        this.name = name;
        this.number = number;
        this.author = author;
    }
}

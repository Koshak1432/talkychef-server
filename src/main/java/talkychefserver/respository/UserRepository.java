package talkychefserver.respository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import talkychefserver.model.entities.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByUid(String uid);

    @Query(value = """
                    (
                        SELECT * FROM users
                        WHERE uid ILIKE :namePart || '%'
                        ORDER BY uid
                    )
                    UNION
                    (
                        SELECT * FROM users
                        WHERE uid ILIKE '% ' || :namePart || '%'
                        ORDER BY uid
                    )
                    LIMIT :limit OFFSET :limit * :page
            """, nativeQuery = true)
    List<User> findByUidContaining(String namePart, Integer limit, Integer page);

}

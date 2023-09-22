package voicerecipeserver.respository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import voicerecipeserver.model.entities.Recipe;
import voicerecipeserver.model.entities.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByUid(String uid);

    @Query(value = """
                    SELECT * FROM users
                    WHERE uid ILIKE :namePart || '%'
                    UNION
                    SELECT * FROM users
                    WHERE uid ILIKE '% ' || :namePart || '%'
                    ORDER BY uid
                    LIMIT CASE WHEN (:limit > 0) THEN :limit END
            """, nativeQuery = true)
    List<User> findByUidContaining(@Param("namePart") String inline, @Param("limit") Integer limit);

}

package voicerecipeserver.respository;


import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
        import voicerecipeserver.model.entities.User;
import voicerecipeserver.model.entities.UserInfo;

import java.util.Optional;

public interface UserInfoRepository extends CrudRepository<UserInfo, Long> {
    Optional<UserInfo> findByEmail(String email);

    Optional<UserInfo> findByToken(String token);
}

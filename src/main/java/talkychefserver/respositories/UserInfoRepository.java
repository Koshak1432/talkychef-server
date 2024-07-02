package talkychefserver.respositories;


import org.springframework.data.repository.CrudRepository;
import talkychefserver.model.entities.UserInfo;

import java.util.Optional;

public interface UserInfoRepository extends CrudRepository<UserInfo, Long> {
    Optional<UserInfo> findByEmail(String email);

    Optional<UserInfo> findByToken(String token);
}

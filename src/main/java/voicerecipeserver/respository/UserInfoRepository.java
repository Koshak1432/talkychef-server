package voicerecipeserver.respository;


import org.springframework.data.repository.CrudRepository;
        import voicerecipeserver.model.entities.User;
import voicerecipeserver.model.entities.UserInfo;

import java.util.Optional;

public interface UserInfoRepository extends CrudRepository<UserInfo, Long> {
}

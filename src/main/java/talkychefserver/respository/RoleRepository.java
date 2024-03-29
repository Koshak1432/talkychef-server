package voicerecipeserver.respository;

import org.springframework.data.repository.CrudRepository;
import voicerecipeserver.model.entities.Role;

import java.util.Optional;

public interface RoleRepository extends CrudRepository<Role, Long> {
    Optional<Role> findByName(String name);
}

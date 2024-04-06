package talkychefserver.respositories;

import org.springframework.data.repository.CrudRepository;
import talkychefserver.model.entities.Role;

import java.util.Optional;

public interface RoleRepository extends CrudRepository<Role, Long> {
    Optional<Role> findByName(String name);
}

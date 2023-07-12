package voicerecipeserver.security.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import voicerecipeserver.model.entities.Role;
import voicerecipeserver.model.entities.User;
import voicerecipeserver.respository.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public interface UserService {
    public Optional<User> getByLogin(@NonNull String login);
}
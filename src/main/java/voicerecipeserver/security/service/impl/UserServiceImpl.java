package voicerecipeserver.security.service.impl;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import voicerecipeserver.model.entities.User;
import voicerecipeserver.respository.UserRepository;
import voicerecipeserver.security.service.UserService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    UserRepository userRepository;

    @Autowired
    private void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> getByLogin(@NonNull String login) {
        Optional<User> user = userRepository.findByLogin(login);
        return user;

    }

}
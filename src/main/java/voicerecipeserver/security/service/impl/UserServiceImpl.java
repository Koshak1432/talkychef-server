package voicerecipeserver.security.service.impl;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import voicerecipeserver.model.dto.IdDto;
import voicerecipeserver.model.dto.UserDto;
import voicerecipeserver.model.entities.Recipe;
import voicerecipeserver.model.entities.Role;
import voicerecipeserver.model.entities.User;
import voicerecipeserver.model.exceptions.BadRequestException;
import voicerecipeserver.model.exceptions.NotFoundException;
import voicerecipeserver.respository.UserRepository;
import voicerecipeserver.security.config.BeanConfig;
import voicerecipeserver.security.service.UserService;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    @Autowired
    private BeanConfig passwordEncoder;

    private final ModelMapper mapper;


    public Optional<User> getByLogin(@NonNull String login) {
        Optional<User> user = userRepository.findByLogin(login);
        return user;
    }

    @Override
    public ResponseEntity<IdDto> postUser(UserDto userDto) throws NotFoundException, BadRequestException {
        User user = mapper.map(userDto, User.class);
        user.setId(null);
        user.setUid(userDto.getLogin()); //todo uid дублирует login
        user.setRoles(Collections.singleton(Role.USER));
        user.setPassword(passwordEncoder.getPasswordEncoder().encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        return new ResponseEntity<>(new IdDto().id(savedUser.getId()), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<IdDto> updateUser(UserDto userDto) throws NotFoundException, BadRequestException {
        User user = findUser(userDto.getLogin());
        user.setPassword(userDto.getPassword());
        user.setDisplayName(userDto.getDisplayName());
        User savedUser = userRepository.save(user);
        return new ResponseEntity<>(new IdDto().id(savedUser.getId()), HttpStatus.OK);

    }

    private User findUser(String login) throws NotFoundException {
        Optional<User> userOptional = userRepository.findByLogin(login);
        if (userOptional.isEmpty()) {
            throw new NotFoundException("Не удалось найти рецепт с id: " + login);
        }
        return userOptional.get();
    }

}
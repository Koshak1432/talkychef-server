package voicerecipeserver.security.service.impl;

import lombok.NonNull;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import voicerecipeserver.model.dto.IdDto;
import voicerecipeserver.model.dto.UserDto;
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
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private BeanConfig passwordEncoder;

    private final ModelMapper mapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, BeanConfig passwordEncoder,  ModelMapper mapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mapper = mapper;
    }


    public Optional<User> getByLogin(@NonNull String login) {
        return userRepository.findByLogin(login);
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

    public ResponseEntity<IdDto> updateUserPassword(UserDto userDto) throws NotFoundException {
        User user = findUserByLogin(userDto.getLogin());
        user.setPassword(passwordEncoder.getPasswordEncoder().encode(userDto.getPassword()));
        User savedUser = userRepository.save(user);
        return new ResponseEntity<>(new IdDto().id(savedUser.getId()), HttpStatus.OK);
    }

    User findUserByLogin(String login) throws NotFoundException {
        Optional<User> userOptional = userRepository.findByLogin(login);
        if (userOptional.isEmpty()) {
            throw new NotFoundException("Не удалось найти пользователя с логином: " + login);
        }

        return userOptional.get();
    }


}
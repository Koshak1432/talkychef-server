package voicerecipeserver.security.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import voicerecipeserver.model.dto.CommentDto;
import voicerecipeserver.model.dto.IdDto;
import voicerecipeserver.model.dto.UserDto;
import voicerecipeserver.model.entities.Role;
import voicerecipeserver.model.entities.User;
import voicerecipeserver.model.exceptions.BadRequestException;
import voicerecipeserver.model.exceptions.NotFoundException;
import voicerecipeserver.respository.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public interface UserService {
    public Optional<User> getByLogin(@NonNull String login);

    ResponseEntity<IdDto> postUser(UserDto userDto) throws NotFoundException, BadRequestException;

    ResponseEntity<IdDto> updateUser(UserDto userDto) throws NotFoundException, BadRequestException;
}
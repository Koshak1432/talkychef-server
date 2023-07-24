package voicerecipeserver.security.service;

import lombok.NonNull;
import org.springframework.http.ResponseEntity;
import voicerecipeserver.model.dto.IdDto;
import voicerecipeserver.model.dto.UserDto;
import voicerecipeserver.model.dto.UserProfileDto;
import voicerecipeserver.model.entities.User;
import voicerecipeserver.model.exceptions.AuthException;
import voicerecipeserver.model.exceptions.BadRequestException;
import voicerecipeserver.model.exceptions.NotFoundException;

import java.util.Optional;

public interface UserService {
    Optional<User> getByLogin(@NonNull String login);

    ResponseEntity<IdDto> postUser(UserDto userDto) throws NotFoundException, BadRequestException;

    ResponseEntity<UserProfileDto> getUserProfile() throws Exception;
    ResponseEntity<UserProfileDto> getUserProfile(String login) throws Exception;


    ResponseEntity<IdDto> profileUpdate(UserProfileDto profileDto) throws BadRequestException, NotFoundException;
}
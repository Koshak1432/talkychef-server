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
import voicerecipeserver.model.exceptions.UserException;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> getByLogin(@NonNull String login);

    ResponseEntity<IdDto> postUser(UserDto userDto) throws NotFoundException, BadRequestException, UserException;

    ResponseEntity<UserProfileDto> getUserProfile() throws Exception;
    ResponseEntity<List<UserProfileDto>>getUserProfile(String login, Integer limit) throws Exception;


    ResponseEntity<IdDto> profileUpdate(UserProfileDto profileDto) throws BadRequestException, NotFoundException;
    ResponseEntity<IdDto> profilePost(UserProfileDto profileDto) throws BadRequestException, NotFoundException, UserException;
}
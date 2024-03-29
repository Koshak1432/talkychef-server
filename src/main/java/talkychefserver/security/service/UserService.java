package voicerecipeserver.security.service;

import org.springframework.http.ResponseEntity;
import voicerecipeserver.model.dto.IdDto;
import voicerecipeserver.model.dto.UserDto;
import voicerecipeserver.model.dto.UserProfileDto;
import voicerecipeserver.model.exceptions.AuthException;
import voicerecipeserver.model.exceptions.BadRequestException;
import voicerecipeserver.model.exceptions.NotFoundException;

import java.util.List;

public interface UserService {
    ResponseEntity<IdDto> addUser(UserDto userDto) throws NotFoundException, BadRequestException;

    ResponseEntity<UserProfileDto> getCurrentUserProfile() throws NotFoundException;

    ResponseEntity<List<UserProfileDto>> getUserProfilesByPartLogin(String login, Integer limit, Integer page) throws
            NotFoundException;

    ResponseEntity<UserProfileDto> getUserProfileByLogin(String login) throws NotFoundException;

    ResponseEntity<IdDto> updateProfile(UserProfileDto profileDto) throws BadRequestException, NotFoundException;

    ResponseEntity<IdDto> addProfile(UserProfileDto profileDto) throws BadRequestException, NotFoundException;

    ResponseEntity<Void> sendEmailInstructions(String email) throws NotFoundException;

    ResponseEntity<IdDto> verifyCode(String token) throws NotFoundException, BadRequestException;

    ResponseEntity<Void> changePassword(String token, UserDto userDto) throws NotFoundException, AuthException;
}
package talkychefserver.security.service;

import org.springframework.http.ResponseEntity;
import talkychefserver.model.dto.IdDto;
import talkychefserver.model.dto.UserDto;
import talkychefserver.model.dto.UserProfileDto;

import java.util.List;

public interface UserService {
    ResponseEntity<IdDto> addUser(UserDto userDto);

    ResponseEntity<UserProfileDto> getCurrentUserProfile();

    ResponseEntity<List<UserProfileDto>> getUserProfilesByPartLogin(String login, Integer limit, Integer page);

    ResponseEntity<UserProfileDto> getUserProfileByLogin(String login);

    ResponseEntity<IdDto> updateProfile(UserProfileDto profileDto);

    ResponseEntity<IdDto> addProfile(UserProfileDto profileDto);

    ResponseEntity<Void> sendEmailInstructions(String email);

    ResponseEntity<IdDto> verifyCode(String token);

    ResponseEntity<Void> changePassword(String token, UserDto userDto);
}
package talkychefserver.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import talkychefserver.api.ProfileApi;
import talkychefserver.model.dto.IdDto;
import talkychefserver.model.dto.UserDto;
import talkychefserver.model.dto.UserProfileDto;
import talkychefserver.security.service.UserService;

import java.util.List;

@CrossOrigin(maxAge = 1440)
@RestController
public class ProfileApiController implements ProfileApi {
    private final UserService userService;

    @Autowired
    public ProfileApiController(UserService userService) {
        this.userService = userService;
    }


    @Override
    public ResponseEntity<UserProfileDto> getCurrentUserProfile() {
        return userService.getCurrentUserProfile();
    }

    @Override
    public ResponseEntity<IdDto> updateProfile(UserProfileDto profileDto) {
        return userService.updateProfile(profileDto);
    }

    @Override
    public ResponseEntity<IdDto> addProfile(UserProfileDto profileDto) {
        return userService.addProfile(profileDto);
    }

    @Override
    public ResponseEntity<List<UserProfileDto>> getProfilesByPartUid(String login, Integer limit, Integer page) {
        return userService.getUserProfilesByPartLogin(login, limit, page);
    }

    @Override
    public ResponseEntity<UserProfileDto> getProfileByUid(String login) {
        return userService.getUserProfileByLogin(login);
    }

    @Override
    public ResponseEntity<Void> sendInstructions(String email) {
        return userService.sendEmailInstructions(email);
    }

    @Override
    public ResponseEntity<IdDto> verifyCode(String token) {
        return userService.verifyCode(token);
    }

    @Override
    public ResponseEntity<Void> changePassword(String token, UserDto userDto) {
        return userService.changePassword(token, userDto);

    }
}

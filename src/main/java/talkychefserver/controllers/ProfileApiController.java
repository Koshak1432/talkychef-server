package voicerecipeserver.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import voicerecipeserver.api.ProfileApi;
import voicerecipeserver.model.dto.IdDto;
import voicerecipeserver.model.dto.UserDto;
import voicerecipeserver.model.dto.UserProfileDto;
import voicerecipeserver.model.exceptions.AuthException;
import voicerecipeserver.model.exceptions.BadRequestException;
import voicerecipeserver.model.exceptions.NotFoundException;
import voicerecipeserver.security.service.UserService;

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
    public ResponseEntity<UserProfileDto> getCurrentUserProfile() throws NotFoundException {
        return userService.getCurrentUserProfile();
    }

    @Override
    public ResponseEntity<IdDto> updateProfile(UserProfileDto profileDto) throws BadRequestException, NotFoundException {
        return userService.updateProfile(profileDto);
    }

    @Override
    public ResponseEntity<IdDto> addProfile(UserProfileDto profileDto) throws BadRequestException, NotFoundException {
        return userService.addProfile(profileDto);
    }

    @Override
    public ResponseEntity<List<UserProfileDto>> getProfilesByPartUid(String login, Integer limit, Integer page) throws
            NotFoundException {
        return userService.getUserProfilesByPartLogin(login, limit, page);
    }

    @Override
    public ResponseEntity<UserProfileDto> getProfileByUid(String login) throws NotFoundException {
        return userService.getUserProfileByLogin(login);
    }

    @Override
    public ResponseEntity<Void> sendInstructions(String email) throws NotFoundException {
        return userService.sendEmailInstructions(email);
    }

    @Override
    public ResponseEntity<IdDto> verifyCode(String token) throws NotFoundException, BadRequestException {
        return userService.verifyCode(token);
    }

    @Override
    public ResponseEntity<Void> changePassword(String token, UserDto userDto) throws NotFoundException,
            BadRequestException, AuthException {
        return userService.changePassword(token, userDto);

    }
}

package voicerecipeserver.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import voicerecipeserver.api.ProfileApi;
import voicerecipeserver.model.dto.IdDto;
import voicerecipeserver.model.dto.RecipeDto;
import voicerecipeserver.model.dto.UserDto;
import voicerecipeserver.model.dto.UserProfileDto;
import voicerecipeserver.model.exceptions.AuthException;
import voicerecipeserver.model.exceptions.BadRequestException;
import voicerecipeserver.model.exceptions.NotFoundException;
import voicerecipeserver.model.exceptions.UserException;
import voicerecipeserver.security.service.UserService;
import voicerecipeserver.services.RecipeService;

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
    public ResponseEntity<UserProfileDto> profileGet() throws Exception {
        return userService.getUserProfile();
    }

    @Override
    public ResponseEntity<IdDto> profilePut(UserProfileDto profileDto) throws BadRequestException, NotFoundException {
        return userService.profileUpdate(profileDto);
    }
    @Override
    public ResponseEntity<IdDto> profilePost(UserProfileDto profileDto) throws BadRequestException, NotFoundException, UserException {
        return userService.profilePost(profileDto);
    }

    @Override
    public ResponseEntity<List<UserProfileDto>> profileByUidGet(String login, Integer limit) throws Exception {
        return userService.getUserProfile(login, limit);
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
    public ResponseEntity<Void> changePassword(String token, UserDto userDto) throws NotFoundException, BadRequestException, AuthException {
        return userService.changePassword(token, userDto);

    }
}
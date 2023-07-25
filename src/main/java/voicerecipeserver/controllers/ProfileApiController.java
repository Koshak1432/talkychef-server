package voicerecipeserver.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import voicerecipeserver.api.ProfileApi;
import voicerecipeserver.model.dto.IdDto;
import voicerecipeserver.model.dto.RecipeDto;
import voicerecipeserver.model.dto.UserProfileDto;
import voicerecipeserver.model.exceptions.AuthException;
import voicerecipeserver.model.exceptions.BadRequestException;
import voicerecipeserver.model.exceptions.NotFoundException;
import voicerecipeserver.model.exceptions.UserException;
import voicerecipeserver.security.service.UserService;
import voicerecipeserver.services.RecipeService;

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
    public ResponseEntity<UserProfileDto> profileByIdGet(String login) throws Exception {
        return userService.getUserProfile(login);
    }
}

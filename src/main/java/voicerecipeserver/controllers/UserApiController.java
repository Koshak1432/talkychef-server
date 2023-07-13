package voicerecipeserver.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import voicerecipeserver.api.UserApi;
import voicerecipeserver.security.service.UserService;

@CrossOrigin(maxAge = 1440)
@RestController
public class UserApiController implements UserApi {
    private final UserService userService;

    @Autowired
    public UserApiController(UserService userService) {
        this.userService = userService;
    }

}

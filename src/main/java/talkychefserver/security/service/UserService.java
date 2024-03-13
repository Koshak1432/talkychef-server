package talkychefserver.security.service;

import org.springframework.http.ResponseEntity;
import talkychefserver.model.dto.IdDto;
import talkychefserver.model.dto.UserDto;
import talkychefserver.model.dto.UserProfileDto;
import talkychefserver.model.exceptions.AuthException;
import talkychefserver.model.exceptions.BadRequestException;
import talkychefserver.model.exceptions.NotFoundException;

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
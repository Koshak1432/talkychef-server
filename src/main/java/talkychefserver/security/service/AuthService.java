package talkychefserver.security.service;

import lombok.NonNull;
import talkychefserver.model.dto.UserDto;
import talkychefserver.model.exceptions.AuthException;
import talkychefserver.model.exceptions.BadRequestException;
import talkychefserver.model.exceptions.NotFoundException;
import talkychefserver.security.dto.JwtRequest;
import talkychefserver.security.dto.JwtResponse;

public interface AuthService {

    JwtResponse login(@NonNull JwtRequest authRequest) throws AuthException, NotFoundException;

    JwtResponse getAccessToken(@NonNull String refreshToken) throws AuthException, NotFoundException;

    JwtResponse refresh(@NonNull String refreshToken) throws AuthException, NotFoundException;

    JwtResponse registration(UserDto userDto) throws AuthException, NotFoundException, BadRequestException;

    JwtResponse changePassword(UserDto userDto) throws NotFoundException, AuthException, BadRequestException;

}
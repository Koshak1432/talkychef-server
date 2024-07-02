package talkychefserver.security.service;

import lombok.NonNull;
import talkychefserver.model.dto.UserDto;
import talkychefserver.security.dto.JwtRequest;
import talkychefserver.security.dto.JwtResponse;

public interface AuthService {

    JwtResponse login(@NonNull JwtRequest authRequest);

    JwtResponse getAccessToken(@NonNull String refreshToken);

    JwtResponse refresh(@NonNull String refreshToken);

    JwtResponse registration(UserDto userDto);

    JwtResponse changePassword(UserDto userDto);

}
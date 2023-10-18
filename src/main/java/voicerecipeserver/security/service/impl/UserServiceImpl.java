package voicerecipeserver.security.service.impl;

import lombok.NonNull;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import voicerecipeserver.model.dto.IdDto;
import voicerecipeserver.model.dto.UserDto;
import voicerecipeserver.model.dto.UserProfileDto;
import voicerecipeserver.model.entities.Collection;
import voicerecipeserver.model.entities.Media;
import voicerecipeserver.model.entities.Role;
import voicerecipeserver.model.entities.User;
import voicerecipeserver.model.entities.UserInfo;
import voicerecipeserver.model.exceptions.AuthException;
import voicerecipeserver.model.exceptions.BadRequestException;
import voicerecipeserver.model.exceptions.NotFoundException;
import voicerecipeserver.model.exceptions.UserException;
import voicerecipeserver.respository.*;
import voicerecipeserver.security.config.BeanConfig;
import voicerecipeserver.security.domain.JwtAuthentication;
import voicerecipeserver.security.service.UserService;
import voicerecipeserver.utils.FindUtils;

import java.util.*;

import static voicerecipeserver.security.service.impl.AuthServiceCommon.getAuthInfo;
import static voicerecipeserver.security.service.impl.AuthServiceCommon.getUserLogin;
import static voicerecipeserver.utils.FindUtils.*;


@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserInfoRepository userInfoRepository;
    private final RoleRepository roleRepository;
    private final BeanConfig passwordEncoder;
    private final ModelMapper mapper;
    private final MediaRepository mediaRepository;
    private final MailSender mailSender;


    @Autowired
    public UserServiceImpl(UserRepository userRepository, BeanConfig passwordEncoder, ModelMapper mapper,
                           RoleRepository roleRepository, UserInfoRepository userInfoRepository,
                           MediaRepository mediaRepository, MailSender mailSender) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mapper = mapper;
        this.roleRepository = roleRepository;
        this.userInfoRepository = userInfoRepository;
        this.mediaRepository = mediaRepository;
        this.mailSender = mailSender;
    }


    private Role findRole(String name) throws NotFoundException {
        Optional<Role> roleOptional = roleRepository.findByName(name);
        if (roleOptional.isEmpty()) {
            throw new NotFoundException("Couldn't find role " + name);
        }
        return roleOptional.get();
    }

    @Override
    @Transactional
    public ResponseEntity<IdDto> postUser(UserDto userDto) throws NotFoundException {
        User user = mapper.map(userDto, User.class);
        user.setId(null);
        user.setUid(userDto.getLogin());
        Role userRole = findRole("USER");
        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        user.setRoles(roles);
        user.setPassword(passwordEncoder.getPasswordEncoder().encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        UserInfo userInfo = new UserInfo();
        userInfo.setUser(user);
        userInfo.setDisplayName(userDto.getDisplayName());
        userInfoRepository.save(userInfo);
        return ResponseEntity.ok(new IdDto().id(savedUser.getId()));
    }

    @Override
    public ResponseEntity<UserProfileDto> getUserProfile() throws Exception {
        User user = FindUtils.findUser(userRepository,AuthServiceCommon.getUserLogin());
        UserInfo userInfo = userInfoRepository.findById(user.getId()).orElseThrow(
                () -> new UserException("Couldn't find user info"));
        UserProfileDto userDto = mapper.map(userInfo, UserProfileDto.class);
        userDto.setUid(user.getUid());
        return ResponseEntity.ok(userDto);
    }

    @Override
    public ResponseEntity<List<UserProfileDto>> getUserProfile(String login, Integer limit) throws Exception {
        if (limit == null) {
            limit = 0;
        }
        List<UserProfileDto> userProfileDtos = new ArrayList<>();
        List<User> users = userRepository.findByUidContaining(login, limit);
        if (users.isEmpty()) {
            throw new NotFoundException("Couldn't find users with substring: " + login);
        }
        for (User user : users) {
            getUserProfileInfo(userProfileDtos, user);
        }

        return ResponseEntity.ok(userProfileDtos);
    }

    private void getUserProfileInfo(List<UserProfileDto> userProfileDtos, User user) {
        Optional<UserInfo> userInfo = userInfoRepository.findById(user.getId());
        UserProfileDto userProfileDto;
        if (userInfo.isPresent()) {
            userProfileDto = mapper.map(userInfo, UserProfileDto.class);
        } else {
            userProfileDto = new UserProfileDto();
        }
        userProfileDto.setUid(user.getUid());
        userProfileDtos.add(userProfileDto);
    }


    @Override
    @Transactional
    public ResponseEntity<IdDto> profileUpdate(UserProfileDto profileDto) throws BadRequestException,
            NotFoundException {
        if (!AuthServiceCommon.checkAuthorities(profileDto.getUid())) {
            throw new BadRequestException("No rights");
        }
        User user = FindUtils.findUser(userRepository, profileDto.getUid());
        UserInfo userInfo = user.getUserInfo();
        if (userInfo == null) {
            throw new NotFoundException("Couldn't find user info");
        }
        setUserInfo(profileDto, userInfo);
        Media media = null;
        Long oldMediaId = null;
        Long mediaId = profileDto.getMediaId();
        if (mediaId != null) {
            media = FindUtils.findMedia(mediaRepository, mediaId);
        }
        userInfo.setMedia(media);
        if (userInfo.getMedia() != null) {
            oldMediaId = userInfo.getMedia().getId();
        }
        userInfoRepository.save(userInfo);
        if (oldMediaId != null) {
            if (media == null || !Objects.equals(media.getId(), oldMediaId)) {
                mediaRepository.deleteById(oldMediaId);
            }
        }
        return ResponseEntity.ok(new IdDto().id(userInfo.getId()));
    }

    private static void setUserInfo(UserProfileDto profileDto, UserInfo userInfo) {
        userInfo.setInfo(profileDto.getInfo());
        userInfo.setVkLink(profileDto.getVkLink());
        userInfo.setTgLink(profileDto.getTgLink());
        userInfo.setDisplayName(profileDto.getDisplayName());
        userInfo.setEmail(profileDto.getEmail());
    }

    @Override
    @Transactional
    public ResponseEntity<IdDto> profilePost(UserProfileDto profileDto) throws BadRequestException, NotFoundException,
            UserException {
        if (!AuthServiceCommon.checkAuthorities(profileDto.getUid())) {
            throw new BadRequestException("No rights");
        }
        UserInfo userInfo = mapper.map(profileDto, UserInfo.class);
        User user = FindUtils.findUser(userRepository, profileDto.getUid());
        if (user.getUserInfo() != null) {
            throw new UserException("User info already exists");
        }
        userInfo.setUser(user);
        UserInfo newUser = userInfoRepository.save(userInfo);
        return ResponseEntity.ok(new IdDto().id(newUser.getId()));
    }

    @Override
    @Transactional
    public ResponseEntity<Void> sendEmailInstructions(String email) throws NotFoundException {
        UserInfo user = FindUtils.findUserByEmail(userInfoRepository, email);
        String token = UUID.randomUUID().toString();
        user.setToken(token);
        userInfoRepository.save(user);
        sendMessage(user, token);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<IdDto> verifyCode(String token) throws NotFoundException, BadRequestException {
        UserInfo userFromToken = FindUtils.findUserByToken(userInfoRepository, token);
        return ResponseEntity.ok(new IdDto().id(userFromToken.getId()));
    }

    @Override
    @Transactional
    public ResponseEntity<Void> changePassword(String token, UserDto userDto) throws NotFoundException, AuthException {
        UserInfo userFromToken = FindUtils.findUserByToken(userInfoRepository, token);
        userFromToken.setToken(null);
        userInfoRepository.save(userFromToken);
        User user = userRepository.findById(userFromToken.getId()).orElseThrow(() -> new AuthException("Couldn't find user with id: " + userFromToken.getId()));
        user.setPassword(passwordEncoder.getPasswordEncoder().encode(userDto.getPassword()));
        userRepository.save(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    private void sendMessage(UserInfo userInfo, String token) {
        if (!userInfo.getEmail().isEmpty()) {
            String message = String.format("Hello, %s\n" +
                            "You sent an issue to change your password. Please, confirm your email: https://server.talkychef.ru/api/v1/restore-password/%s",
                    userInfo.getDisplayName(), token);
            mailSender.send(userInfo.getEmail(), "Activation code", message);
        }
    }


    @Transactional
    public ResponseEntity<IdDto> updateUserPassword(UserDto userDto) throws NotFoundException, BadRequestException {
        if (!AuthServiceCommon.checkAuthorities(userDto.getLogin())) {
            throw new BadRequestException("No rights");
        }
        User user = FindUtils.findUser(userRepository, userDto.getLogin());
        user.setPassword(passwordEncoder.getPasswordEncoder().encode(userDto.getPassword()));
        User savedUser = userRepository.save(user);
        return ResponseEntity.ok(new IdDto().id(savedUser.getId()));
    }


}

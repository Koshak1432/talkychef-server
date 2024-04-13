package talkychefserver.security.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import talkychefserver.model.dto.IdDto;
import talkychefserver.model.dto.UserDto;
import talkychefserver.model.dto.UserProfileDto;
import talkychefserver.model.entities.Media;
import talkychefserver.model.entities.Role;
import talkychefserver.model.entities.User;
import talkychefserver.model.entities.UserInfo;
import talkychefserver.model.exceptions.AuthException;
import talkychefserver.model.exceptions.BadRequestException;
import talkychefserver.model.exceptions.NotFoundException;
import talkychefserver.respositories.MediaRepository;
import talkychefserver.respositories.RoleRepository;
import talkychefserver.respositories.UserInfoRepository;
import talkychefserver.respositories.UserRepository;
import talkychefserver.security.config.BeanConfig;
import talkychefserver.security.service.UserService;
import talkychefserver.utils.FindUtils;
import talkychefserver.utils.GetUtil;

import java.util.*;

@Slf4j
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

    private Role findRole(String name) {
        Optional<Role> roleOptional = roleRepository.findByName(name);
        if (roleOptional.isEmpty()) {
            log.error("Couldn't find role [{}]", name);
            throw new NotFoundException("Couldn't find role " + name);
        }
        return roleOptional.get();
    }

    @Override
    @Transactional
    public ResponseEntity<IdDto> addUser(UserDto userDto) {
        log.info("Adding user");
        if (userDto.getLogin() == null) {
            log.error("User login is null");
            throw new BadRequestException("Login is null");
        }
        User user = mapper.map(userDto, User.class);
        if (userInfoRepository.findByEmail(userDto.getEmail()).isPresent()) {
            log.error("Email [{}] is already registered", userDto.getEmail());
            throw new BadRequestException("Email is already registered");
        }
        if (userRepository.findByUid(userDto.getLogin()).isPresent()) {
            log.error("Login [{}] is already used", userDto.getLogin());
            throw new BadRequestException("Login is already used");
        }
        user.setId(null);
        user.setUid(userDto.getLogin());
        Set<Role> roles = getRoles();
        user.setRoles(roles);
        user.setPassword(passwordEncoder.getPasswordEncoder().encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        log.info("User [{}] added", savedUser.getUid());
        addUserInfo(userDto, user);
        return ResponseEntity.ok(new IdDto().id(savedUser.getId()));
    }

    private void addUserInfo(UserDto userDto, User user) {
        log.info("Adding user [{}] info", user.getUid());
        UserInfo userInfo = new UserInfo();
        userInfo.setUser(user);
        userInfo.setEmail(userDto.getEmail());
        userInfo.setDisplayName(userDto.getDisplayName());
        UserInfo savedInfo = userInfoRepository.save(userInfo);
        log.info("Added user [{}] info [{}]", user.getUid(), savedInfo.getId());
    }

    private Set<Role> getRoles() {
        Role userRole = findRole("USER");
        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        return roles;
    }

    @Override
    public ResponseEntity<UserProfileDto> getCurrentUserProfile() {
        log.info("Processing get current user profile request");
        User user = FindUtils.findUserByUid(userRepository, AuthServiceCommon.getUserLogin());
        UserInfo userInfo = FindUtils.findUserInfoById(userInfoRepository, user.getId());
        UserProfileDto userProfileDto = mapper.map(userInfo, UserProfileDto.class);
        log.info("Processed get current user [{}] profile [{}] request", user.getUid(), userInfo.getId());
        return ResponseEntity.ok(userProfileDto);
    }

    @Override
    public ResponseEntity<UserProfileDto> getUserProfileByLogin(String login) {
        log.info("Processing get user profile by login [{}] request", login);
        User user = FindUtils.findUserByUid(userRepository, login);
        UserInfo userInfo = FindUtils.findUserInfoById(userInfoRepository, user.getId());
        UserProfileDto userProfileDto = mapper.map(userInfo, UserProfileDto.class);
        log.info("Processed get user profile by login [{}] request", login);
        return ResponseEntity.ok(userProfileDto);
    }

    @Override
    public ResponseEntity<List<UserProfileDto>> getUserProfilesByPartLogin(String login, Integer limit, Integer page) {
        log.info("Processing get user profiles by part login [{}] request", login);
        List<UserProfileDto> userProfileDtos = new ArrayList<>();
        List<User> users = userRepository.findByUidContaining(login, GetUtil.getCurrentLimit(limit),
                                                              GetUtil.getCurrentPage(page));
        for (User user : users) {
            UserInfo userInfo = FindUtils.findUserInfoById(userInfoRepository, user.getId());
            UserProfileDto userProfileDto = mapper.map(userInfo, UserProfileDto.class);
            userProfileDtos.add(userProfileDto);
        }
        log.info("Response profile lists size: {}", userProfileDtos.size());
        return ResponseEntity.ok(userProfileDtos);
    }

    @Override
    @Transactional
    public ResponseEntity<IdDto> updateProfile(UserProfileDto profileDto) {
        log.info("Processing update profile request");
        if (!AuthServiceCommon.checkAuthorities(profileDto.getUid())) {
            log.error("User has no rights to update profile");
            throw new AuthException("No rights");
        }
        User user = FindUtils.findUserByUid(userRepository, profileDto.getUid());
        UserInfo userInfo = user.getUserInfo();
        if (userInfo == null) {
            log.error("Couldn't find user [{}] info", user.getUid());
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
        log.info("User profile updated");
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
    public ResponseEntity<IdDto> addProfile(UserProfileDto profileDto) {
        log.info("Processing add profile request");
        if (!AuthServiceCommon.checkAuthorities(profileDto.getUid())) {
            log.error("User has no rights to add user info");
            throw new AuthException("No rights");
        }
        UserInfo userInfo = mapper.map(profileDto, UserInfo.class);
        User user = FindUtils.findUserByUid(userRepository, profileDto.getUid());
        if (user.getUserInfo() != null) {
            log.error("User info [{}] already exists", userInfo.getId());
            throw new BadRequestException("User info already exists");
        }
        userInfo.setUser(user);
        UserInfo savedInfo = userInfoRepository.save(userInfo);
        log.info("Added user info [{}]", savedInfo.getId());
        return ResponseEntity.ok(new IdDto().id(savedInfo.getId()));
    }

    @Override
    @Transactional
    public ResponseEntity<Void> sendEmailInstructions(String email) {
        log.info("Process send email instructions to [{}] request", email);
        UserInfo user = FindUtils.findUserByEmail(userInfoRepository, email);
        String token = UUID.randomUUID().toString();
        user.setToken(token);
        userInfoRepository.save(user);
        sendMessage(user, token);
        log.info("Sent email");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<IdDto> verifyCode(String token) {
        log.info("Verifying code");
        UserInfo userFromToken = FindUtils.findUserByToken(userInfoRepository, token);
        log.info("Code verified");
        return ResponseEntity.ok(new IdDto().id(userFromToken.getId()));
    }

    @Override
    @Transactional
    public ResponseEntity<Void> changePassword(String token, UserDto userDto) {
        log.info("Processing change password request");
        UserInfo userFromToken = FindUtils.findUserByToken(userInfoRepository, token);
        userFromToken.setToken(null);
        userInfoRepository.save(userFromToken);
        User user = FindUtils.findUserById(userRepository, userFromToken.getId());
        user.setPassword(passwordEncoder.getPasswordEncoder().encode(userDto.getPassword()));
        userRepository.save(user);
        log.info("User [{}] password changed", user.getUid());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void sendMessage(UserInfo userInfo, String token) {
        if (!userInfo.getEmail().isEmpty()) {
            String message = String.format(
                    "Hello, %s\n" + "You sent an issue to change your password. Please, confirm your email: " +
                            "https://server.talkychef.ru/api/v1/restore-password/%s",
                    userInfo.getDisplayName(), token);
            mailSender.send(userInfo.getEmail(), "Activation code", message);
        }
    }


    @Transactional
    public User updateUserPassword(UserDto userDto) {
        log.info("Updating user password");
        if (!AuthServiceCommon.checkAuthorities(userDto.getLogin())) {
            log.error("User has no rights to update password");
            throw new BadRequestException("No rights");
        }
        User user = FindUtils.findUserByUid(userRepository, userDto.getLogin());
        user.setPassword(passwordEncoder.getPasswordEncoder().encode(userDto.getPassword()));
        User savedUser = userRepository.save(user);
        log.info("User [{}] password updated", user.getUid());
        return savedUser;
    }
}

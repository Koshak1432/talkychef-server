package talkychefserver.security.service.impl;

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
            throw new NotFoundException("Couldn't find role " + name);
        }
        return roleOptional.get();
    }

    @Override
    @Transactional
    public ResponseEntity<IdDto> addUser(UserDto userDto) {
        User user = mapper.map(userDto, User.class);
        if (userInfoRepository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new BadRequestException("Email is already registered");
        }
        if (userRepository.findByUid(userDto.getLogin()).isPresent()) {
            throw new BadRequestException("Login is already registered");
        }
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
        userInfo.setEmail(userDto.getEmail());
        userInfo.setDisplayName(userDto.getDisplayName());
        userInfoRepository.save(userInfo);
        return ResponseEntity.ok(new IdDto().id(savedUser.getId()));
    }

    @Override
    public ResponseEntity<UserProfileDto> getCurrentUserProfile() {
        User user = FindUtils.findUserByUid(userRepository, AuthServiceCommon.getUserLogin());
        UserInfo userInfo = FindUtils.findUserInfoById(userInfoRepository, user.getId());
        UserProfileDto userProfileDto = mapper.map(userInfo, UserProfileDto.class);
        return ResponseEntity.ok(userProfileDto);
    }

    @Override
    public ResponseEntity<UserProfileDto> getUserProfileByLogin(String login) {
        User user = FindUtils.findUserByUid(userRepository, login);
        UserInfo userInfo = FindUtils.findUserInfoById(userInfoRepository, user.getId());
        UserProfileDto userProfileDto = mapper.map(userInfo, UserProfileDto.class);
        return ResponseEntity.ok(userProfileDto);
    }

    @Override
    public ResponseEntity<List<UserProfileDto>> getUserProfilesByPartLogin(String login, Integer limit, Integer page) {
        List<UserProfileDto> userProfileDtos = new ArrayList<>();
        List<User> users = userRepository.findByUidContaining(login, GetUtil.getCurrentLimit(limit),
                                                              GetUtil.getCurrentPage(page));
        for (User user : users) {
            UserInfo userInfo = FindUtils.findUserInfoById(userInfoRepository, user.getId());
            UserProfileDto userProfileDto = mapper.map(userInfo, UserProfileDto.class);
            userProfileDtos.add(userProfileDto);
        }
        return ResponseEntity.ok(userProfileDtos);
    }

    @Override
    @Transactional
    public ResponseEntity<IdDto> updateProfile(UserProfileDto profileDto) {
        if (!AuthServiceCommon.checkAuthorities(profileDto.getUid())) {
            throw new BadRequestException("No rights");
        }
        User user = FindUtils.findUserByUid(userRepository, profileDto.getUid());
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
    public ResponseEntity<IdDto> addProfile(UserProfileDto profileDto) {
        if (!AuthServiceCommon.checkAuthorities(profileDto.getUid())) {
            throw new BadRequestException("No rights");
        }
        UserInfo userInfo = mapper.map(profileDto, UserInfo.class);
        User user = FindUtils.findUserByUid(userRepository, profileDto.getUid());
        if (user.getUserInfo() != null) {
            throw new BadRequestException("User info already exists");
        }
        userInfo.setUser(user);
        UserInfo newUser = userInfoRepository.save(userInfo);
        return ResponseEntity.ok(new IdDto().id(newUser.getId()));
    }

    @Override
    @Transactional
    public ResponseEntity<Void> sendEmailInstructions(String email) {
        UserInfo user = FindUtils.findUserByEmail(userInfoRepository, email);
        String token = UUID.randomUUID().toString();
        user.setToken(token);
        userInfoRepository.save(user);
        sendMessage(user, token);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<IdDto> verifyCode(String token) {
        UserInfo userFromToken = FindUtils.findUserByToken(userInfoRepository, token);
        return ResponseEntity.ok(new IdDto().id(userFromToken.getId()));
    }

    @Override
    @Transactional
    public ResponseEntity<Void> changePassword(String token, UserDto userDto) {
        UserInfo userFromToken = FindUtils.findUserByToken(userInfoRepository, token);
        userFromToken.setToken(null);
        userInfoRepository.save(userFromToken);
        User user = FindUtils.findUserById(userRepository, userFromToken.getId());
        user.setPassword(passwordEncoder.getPasswordEncoder().encode(userDto.getPassword()));
        userRepository.save(user);
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
    public ResponseEntity<IdDto> updateUserPassword(UserDto userDto) {
        if (!AuthServiceCommon.checkAuthorities(userDto.getLogin())) {
            throw new BadRequestException("No rights");
        }
        User user = FindUtils.findUserByUid(userRepository, userDto.getLogin());
        user.setPassword(passwordEncoder.getPasswordEncoder().encode(userDto.getPassword()));
        User savedUser = userRepository.save(user);
        return ResponseEntity.ok(new IdDto().id(savedUser.getId()));
    }


}

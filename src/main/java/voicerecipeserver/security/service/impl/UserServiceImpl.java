package voicerecipeserver.security.service.impl;

import lombok.NonNull;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import voicerecipeserver.model.dto.IdDto;
import voicerecipeserver.model.dto.UserDto;
import voicerecipeserver.model.dto.UserProfileDto;
import voicerecipeserver.model.entities.Media;
import voicerecipeserver.model.entities.Role;
import voicerecipeserver.model.entities.User;
import voicerecipeserver.model.entities.UserInfo;
import voicerecipeserver.model.exceptions.AuthException;
import voicerecipeserver.model.exceptions.BadRequestException;
import voicerecipeserver.model.exceptions.NotFoundException;
import voicerecipeserver.model.exceptions.UserException;
import voicerecipeserver.respository.MediaRepository;
import voicerecipeserver.respository.RoleRepository;
import voicerecipeserver.respository.UserInfoRepository;
import voicerecipeserver.respository.UserRepository;
import voicerecipeserver.security.config.BeanConfig;
import voicerecipeserver.security.domain.JwtAuthentication;
import voicerecipeserver.security.service.UserService;
import voicerecipeserver.utils.FindUtils;

import java.util.*;

import static voicerecipeserver.security.service.impl.AuthServiceCommon.getAuthInfo;


@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserInfoRepository userInfoRepository;
    private final RoleRepository roleRepository;
    private final BeanConfig passwordEncoder;
    private final ModelMapper mapper;
    private final MediaRepository mediaRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, BeanConfig passwordEncoder, ModelMapper mapper,
                           RoleRepository roleRepository, UserInfoRepository userInfoRepository,
                           MediaRepository mediaRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mapper = mapper;
        this.roleRepository = roleRepository;
        this.userInfoRepository = userInfoRepository;
        this.mediaRepository = mediaRepository;
    }


    public Optional<User> getByLogin(@NonNull String login) {
        return userRepository.findByUid(login);
    }

    Role getRoleByName(String name) throws NotFoundException {
        Optional<Role> roleOptional = roleRepository.findByName(name);
        if (roleOptional.isEmpty()) {
            throw new NotFoundException("Couldn't find role " + name);
        }
        return roleOptional.get();
    }

    @Override
    public ResponseEntity<IdDto> postUser(UserDto userDto) throws UserException, NotFoundException {
        User user = mapper.map(userDto, User.class);
        user.setId(null);
        user.setUid(userDto.getLogin());
        Role userRole = getRoleByName("USER");
        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        user.setRoles(roles);
        user.setPassword(passwordEncoder.getPasswordEncoder().encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        if (savedUser.getUserInfo() != null) {
            throw new UserException("Информация о пользователе существует");
        }
        UserInfo userInfo = new UserInfo();
        userInfo.setUser(user);
        userInfo.setDisplayName(userDto.getDisplayName());
        userInfoRepository.save(userInfo);
        return ResponseEntity.ok(new IdDto().id(savedUser.getId()));
    }

    @Override
    public ResponseEntity<UserProfileDto> getUserProfile() throws Exception {
        JwtAuthentication principal = getAuthInfo();
        if (principal == null) {
            throw new AuthException("Not authorized yet");
        }
        User user = FindUtils.findUser(userRepository, principal.getLogin());
        UserInfo userInfo = userInfoRepository.findById(user.getId()).orElseThrow(
                () -> new UserException("Нет информации о пользователе"));
        UserProfileDto userDto = mapper.map(userInfo, UserProfileDto.class);
        userDto.setUid(user.getUid());
        System.out.println("USER DTO: " + userDto);
        System.out.println("USER: " + user);
        return ResponseEntity.ok(userDto);
    }

    @Override
    public ResponseEntity<List<UserProfileDto>> getUserProfile(String login, Integer limit) throws Exception {
        if (limit == null) {
            limit = 0;
        }
        List<User> users = userRepository.findByUidContaining(login, limit);
        List<UserProfileDto> userProfileDtos = new ArrayList<>();
        if (users.isEmpty()) {
            throw new NotFoundException("Не удалось найти пользователей с подстрокой: " + login);
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
//        userProfileDto.setDisplayName(user.getDisplayName());
        userProfileDtos.add(userProfileDto);
    }


    @Override
    public ResponseEntity<IdDto> profileUpdate(UserProfileDto profileDto) throws BadRequestException,
            NotFoundException {
        if (!AuthServiceCommon.checkAuthorities(profileDto.getUid())) {
            throw new BadRequestException("Нет прав");
        }
        User user = findUserByLogin(profileDto.getUid());
        UserInfo userInfo = user.getUserInfo();
        if (userInfo == null) {
            throw new NotFoundException("Информация пользователя не найдена");
        }
        setUserInfo(profileDto, userInfo);
        Media media = null;
        Long oldMediaId = null;
        Long mediaId = profileDto.getMediaId();
        if (mediaId != null) {
            media = mediaRepository.findById(mediaId).orElseThrow(
                    () -> new NotFoundException("Медиа не найдено"));
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
    }

    @Override
    public ResponseEntity<IdDto> profilePost(UserProfileDto profileDto) throws BadRequestException, NotFoundException,
            UserException {
        if (!AuthServiceCommon.checkAuthorities(profileDto.getUid())) {
            throw new BadRequestException("Нет прав");
        }
        UserInfo userInfo = mapper.map(profileDto, UserInfo.class);
        User user = findUserByLogin(profileDto.getUid());
        if (user.getUserInfo() != null) {
            throw new UserException("Информация о пользователе существует");
        }
        userInfo.setUser(user);
        UserInfo newUser = userInfoRepository.save(userInfo);
        return ResponseEntity.ok(new IdDto().id(newUser.getId()));
    }

    public ResponseEntity<IdDto> updateUserPassword(UserDto userDto) throws NotFoundException, BadRequestException {
        if (!AuthServiceCommon.checkAuthorities(userDto.getLogin())) {
            throw new BadRequestException("Нет прав");
        }
        User user = findUserByLogin(userDto.getLogin());
        user.setPassword(passwordEncoder.getPasswordEncoder().encode(userDto.getPassword()));
        User savedUser = userRepository.save(user);
        return ResponseEntity.ok(new IdDto().id(savedUser.getId()));
    }

    User findUserByLogin(String login) throws NotFoundException {
        Optional<User> userOptional = userRepository.findByUid(login);
        if (userOptional.isEmpty()) {
            throw new NotFoundException("Не удалось найти пользователя с логином: " + login);
        }

        return userOptional.get();
    }
}
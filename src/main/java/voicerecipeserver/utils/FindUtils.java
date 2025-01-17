package voicerecipeserver.utils;

import voicerecipeserver.model.entities.*;
import voicerecipeserver.model.exceptions.NotFoundException;
import voicerecipeserver.respository.*;

import java.util.List;


public class FindUtils {
    private FindUtils() {
    }

    public static User findUserByUid(UserRepository repository, String userUid) throws NotFoundException {
        return repository.findByUid(userUid).orElseThrow(
                () -> new NotFoundException("Couldn't find user with uid: " + userUid));
    }

    public static User findUserById(UserRepository repository, Long id) throws NotFoundException {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Couldn't find user with id: " + id));
    }

    public static UserInfo findUserByToken(UserInfoRepository userInfoRepository, String token) throws
            NotFoundException {
        return userInfoRepository.findByToken(token).orElseThrow(
                () -> new NotFoundException("Couldn't find user with token: " + token));
    }

    public static Category findCategory(CategoryRepository repository, Long id) throws NotFoundException {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Couldn't find category with id: " + id));
    }

    public static Recipe findRecipe(RecipeRepository repository, Long id) throws NotFoundException {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Couldn't find recipe with id: " + id));
    }

    public static Comment findComment(CommentRepository repository, Long commentId) throws NotFoundException {
        return repository.findById(commentId).orElseThrow(
                () -> new NotFoundException("Couldn't find comment with id: " + commentId));
    }

    public static Collection findCollection(CollectionRepository repository, Long collectionId) throws
            NotFoundException {
        return repository.findById(collectionId).orElseThrow(
                () -> new NotFoundException("Couldn't find collection with id: " + collectionId));
    }

    public static Media findMedia(MediaRepository repository, Long mediaId) throws NotFoundException {
        return repository.findById(mediaId).orElseThrow(
                () -> new NotFoundException("Couldn't find media with id: " + mediaId));
    }

    public static UserInfo findUserByEmail(UserInfoRepository userInfoRepository, String email) throws
            NotFoundException {
        return userInfoRepository.findByEmail(email).orElseThrow(
                () -> new NotFoundException("Couldn't find user with email: " + email));
    }

    public static Collection findCollectionById(CollectionRepository repository, Long id) throws NotFoundException {
        return repository.findById(id).orElseThrow(
                () -> new NotFoundException("Couldn't find collection with id " + id));
    }

    public static Selection findSelectionById(SelectionRepository repository, Long id) throws NotFoundException {
        return repository.findById(id).orElseThrow(
                () -> new NotFoundException("Couldn't find selection with id " + id));
    }

    public static UserInfo findUserInfoById(UserInfoRepository repository, Long id) throws NotFoundException {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Couldn't find user info"));
    }
}

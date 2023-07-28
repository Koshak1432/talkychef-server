package voicerecipeserver.utils;

import voicerecipeserver.model.entities.Collection;
import voicerecipeserver.model.entities.Comment;
import voicerecipeserver.model.entities.Recipe;
import voicerecipeserver.model.entities.User;
import voicerecipeserver.model.exceptions.NotFoundException;
import voicerecipeserver.respository.CollectionRepository;
import voicerecipeserver.respository.CommentRepository;
import voicerecipeserver.respository.RecipeRepository;
import voicerecipeserver.respository.UserRepository;

import java.util.Optional;

public class FindUtils {
    private FindUtils() {
    }

    public static User findUser(UserRepository repository, String userUid) throws NotFoundException {
        Optional<User> userOptional = repository.findByUid(userUid);
        if (userOptional.isEmpty()) {
            throw new NotFoundException("Couldn't find user with uid: " + userUid);
        }
        return userOptional.get();
    }

    public static Recipe findRecipe(RecipeRepository repository, Long id) throws NotFoundException {
        Optional<Recipe> recipeOptional = repository.findById(id);
        if (recipeOptional.isEmpty()) {
            throw new NotFoundException("Couldn't find recipe with id: " + id);
        }
        return recipeOptional.get();
    }

    public static Comment findComment(CommentRepository repository, Long commentId) throws NotFoundException {
        Optional<Comment> commentOptional = repository.findById(commentId);
        if (commentOptional.isEmpty()) {
            throw new NotFoundException("Couldn't find comment with id: " + commentId);
        }
        return commentOptional.get();
    }

    public static Collection findCollection(CollectionRepository repository, Long collectionId) throws
            NotFoundException {
        Optional<Collection> collectionOptional = repository.findById(collectionId);
        if (collectionOptional.isEmpty()) {
            throw new NotFoundException("Не удалось найти коллекцию с id: " + collectionId);
        }
        return collectionOptional.get();
    }

    public static boolean findCollectionRecipe(CollectionRepository repository, Long recipeId,  Long collectionId) throws
            NotFoundException {
        Optional<Collection> collectionOptional = repository.findRecipe(recipeId, collectionId);
        if (collectionOptional.isEmpty()) {
            throw new NotFoundException("Не удалось найти рецепт в коллекции с id: " + collectionId);
        }
        return true;
    }
}

package voicerecipeserver.respository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import voicerecipeserver.model.entities.Collection;
import voicerecipeserver.model.entities.Recipe;

import java.util.List;
import java.util.Optional;

public interface CollectionRepository extends CrudRepository<Collection, Long> {
    Optional<Collection> findByName(String name);

    @Modifying()
    @Query(value = """
INSERT INTO collections_distribution(collection_id, recipe_id) VALUES (?2,?1);
UPDATE collections
SET number=number+1
WHERE  id=?2
""",nativeQuery = true)
    void addRecipeToCollection(long recipeId, long collectionId);

}

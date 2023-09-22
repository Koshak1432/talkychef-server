package voicerecipeserver.respository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import voicerecipeserver.model.entities.Category;
import voicerecipeserver.model.entities.Collection;
import voicerecipeserver.model.entities.Recipe;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository  extends CrudRepository<Category, Long> {
    List<Category> findAll();

    @Query(value = """
          WITH recepiesId AS (SELECT * FROM categories_distribution 
          WHERE category_id=:id)
          SELECT recipes.* FROM recipes 
          JOIN recepiesId ON id=recipe_id 
          LIMIT :limit
            """, nativeQuery = true)
    List<Recipe>  findRecipesWithCategoryId(@Param("id") Long inline, @Param("limit") Integer limit);
}

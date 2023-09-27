package voicerecipeserver.respository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
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
          SELECT recipe_id FROM categories_distribution 
          WHERE category_id=:id
          LIMIT :limit
            """, nativeQuery = true)
    List<Long>  findRecipesWithCategoryId(@Param("id") Long inline, @Param("limit") Integer limit);


    @Transactional
    @Modifying
    @Query(value = """
            DELETE FROM categories_distribution
            WHERE  category_id=:categoryId AND recipe_id =:recipeId
            """, nativeQuery = true)
    void deleteByCategoryRecipeId(Long categoryId, Long recipeId);

    @Modifying()
    @Transactional
    @Query(value = """
            INSERT INTO categories_distribution(category_id, recipe_id) VALUES (:categoryId, :recipeId);
            """, nativeQuery = true)
    void addRecipeToCategory(Long recipeId, Long categoryId);




}

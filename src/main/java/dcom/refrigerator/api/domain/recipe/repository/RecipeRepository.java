package dcom.refrigerator.api.domain.recipe.repository;

import dcom.refrigerator.api.domain.ingredient.Ingredient;
import dcom.refrigerator.api.domain.recipe.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Integer> {

    Optional<Recipe> findById(Integer id);

    @Query("select rc from Recipe as rc inner join rc.food as fd on fd.writer.id= :id")
    List<Recipe> findAllRecipeByUserId(@Param("id") Integer id);

}

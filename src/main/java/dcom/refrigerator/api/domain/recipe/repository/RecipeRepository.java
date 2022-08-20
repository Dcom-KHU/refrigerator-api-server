package dcom.refrigerator.api.domain.recipe.repository;

import dcom.refrigerator.api.domain.ingredient.Ingredient;
import dcom.refrigerator.api.domain.recipe.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Integer> {

    Optional<Recipe> findById(Integer id);
    Optional<Recipe> findByName(String name);





}

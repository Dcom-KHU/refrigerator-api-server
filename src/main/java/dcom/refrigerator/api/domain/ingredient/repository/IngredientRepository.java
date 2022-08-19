package dcom.refrigerator.api.domain.ingredient.repository;

import dcom.refrigerator.api.domain.ingredient.Ingredient;
import dcom.refrigerator.api.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Integer> {

    Optional<Ingredient> findById(Integer id);
    Optional<Ingredient> findByName(String name);

    Optional<Ingredient> save(Ingredient ingredient);

    void delete(Ingredient ingredient);



}

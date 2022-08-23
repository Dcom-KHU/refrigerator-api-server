package dcom.refrigerator.api.domain.food.repositroy;

import dcom.refrigerator.api.domain.food.Food;
import dcom.refrigerator.api.domain.recipe.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FoodRepository extends JpaRepository<Food, Integer> {

    Optional<Food> findById(Integer id);
    Optional<Food> findByName(String name);





}

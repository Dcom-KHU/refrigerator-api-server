package dcom.refrigerator.api.domain.food.repositroy;

import dcom.refrigerator.api.domain.food.Food;
import dcom.refrigerator.api.domain.recipe.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FoodRepository extends JpaRepository<Food, Integer> {

    Optional<Food> findById(Integer id);
    Optional<Food> findByName(String name);


    Optional<Food> findByDescription(String description);

    @Query("select rc from Recipe as rc inner join rc.food as fd on fd.writer.id= :id")
    List<Recipe> findAllFoodRecipesByUserId(Integer id);

    @Query("select rc from Recipe as rc inner join rc.food as fd on fd.name= :name")
    List<Recipe> findAllFoodRecipesByFoodName(String name);

    List<Food> findAllByWriterId(Integer id);
}


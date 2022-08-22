package dcom.refrigerator.api.domain.foodImage.repository;

import dcom.refrigerator.api.domain.food.Food;
import dcom.refrigerator.api.domain.foodImage.FoodImage;
import dcom.refrigerator.api.domain.recipe.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FoodImageRepository extends JpaRepository<FoodImage, Integer> {

    List<FoodImage> findAllByFood(Food food);

}

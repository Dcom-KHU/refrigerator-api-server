package dcom.refrigerator.api.domain.food.repositroy;

import dcom.refrigerator.api.domain.food.Food;
import dcom.refrigerator.api.domain.recipe.Recipe;
import dcom.refrigerator.api.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FoodRepository extends JpaRepository<Food, Integer> {

    @Query("select f " +
            "from Food f " +
            "left join fetch f.recipes as r " +
            "left join fetch f.images " +
            "left join fetch f.writer " +
            "left join fetch r.ingredient " +
            "where f.id = :id")
    Optional<Food> findById(Integer id);
    Optional<Food> findByName(String name);
    List<Food> findAllByWriterId(Integer id);
}


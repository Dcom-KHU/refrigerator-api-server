package dcom.refrigerator.api.domain.food.repositroy;

import dcom.refrigerator.api.domain.food.Food;
import dcom.refrigerator.api.domain.recipe.Recipe;
import dcom.refrigerator.api.domain.user.User;
import io.swagger.models.auth.In;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query(value = "select * from food f where (f.id, f.ingredientCount) in (select r.food_id, count(r.food_id) from recipe r where r.ingredient_id in (select r2.ingredient_id from refrigerator r2 where  r2.user_id=:userId) group by r.food_id)",nativeQuery = true)
    List<Food> findRefrigeratorFood(@Param(value="userId") Integer userId);


    @Query(value = "select * from Food order by RAND() limit 1",nativeQuery = true)
    List<Food> findTodayFood();

}


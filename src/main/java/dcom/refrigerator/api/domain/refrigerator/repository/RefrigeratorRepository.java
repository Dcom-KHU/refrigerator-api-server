package dcom.refrigerator.api.domain.refrigerator.repository;

import dcom.refrigerator.api.domain.food.Food;
import dcom.refrigerator.api.domain.ingredient.Ingredient;
import dcom.refrigerator.api.domain.recipe.Recipe;
import dcom.refrigerator.api.domain.refrigerator.Refrigerator;
import dcom.refrigerator.api.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Ref;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RefrigeratorRepository extends JpaRepository<Refrigerator, Integer> {
    @Query("select ref from Refrigerator ref left join fetch ref.ingredient where ref.user = :user")
    List<Refrigerator> findByUser(User user);

    @Query("select r2 from Refrigerator r2  where r2.expiredDate <:threshold")
    List<Refrigerator> findByTimeLogLessThan(@Param("threshold") LocalDateTime threshold);

    default List<Refrigerator> findOlds() {
        long minutesGap = 4320;
        return findByTimeLogLessThan(LocalDateTime.now().minusMinutes(minutesGap));
    }
}

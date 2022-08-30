package dcom.refrigerator.api.domain.refrigerator.repository;

import dcom.refrigerator.api.domain.recipe.Recipe;
import dcom.refrigerator.api.domain.refrigerator.Refrigerator;
import dcom.refrigerator.api.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RefrigeratorRepository extends JpaRepository<Refrigerator, Integer> {
    @Query("select ref from Refrigerator ref left join fetch ref.ingredient where ref.user = :user")
    List<Refrigerator> findByUser(User user);
}

package dcom.refrigerator.api.domain.refrigerator.repository;

import dcom.refrigerator.api.domain.recipe.Recipe;
import dcom.refrigerator.api.domain.refrigerator.Refrigerator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefrigeratorRepository extends JpaRepository<Refrigerator, Integer> {

    Optional<Refrigerator> findById(Integer id);
    Optional<Refrigerator> findByUserId(Integer userId);





}

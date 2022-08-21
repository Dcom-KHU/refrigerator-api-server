package dcom.refrigerator.api.domain.food.service;

import dcom.refrigerator.api.domain.food.Food;
import dcom.refrigerator.api.domain.food.repositroy.FoodRepository;
import dcom.refrigerator.api.domain.ingredient.Ingredient;
import dcom.refrigerator.api.domain.recipe.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
@Service
@RequiredArgsConstructor
public class FoodService {
    private final FoodRepository foodRepository;

    public Integer joinFood(Food food){

        Optional<Food> foodOptionalByName=foodRepository.findByName(food.getName());

        if ( foodOptionalByName.isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "이미 존제하는 음식 name 입니다.");
        }


        Food temp= foodRepository.save(food);

        return temp.getId();
    }
}

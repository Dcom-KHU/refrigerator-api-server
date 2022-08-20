package dcom.refrigerator.api.domain.recipe.service;

import dcom.refrigerator.api.domain.food.Food;
import dcom.refrigerator.api.domain.food.FoodCategory;
import dcom.refrigerator.api.domain.food.repositroy.FoodRepository;
import dcom.refrigerator.api.domain.ingredient.Ingredient;
import dcom.refrigerator.api.domain.recipe.Recipe;
import dcom.refrigerator.api.domain.recipe.dto.RecipeRequestDto;
import dcom.refrigerator.api.domain.recipe.repository.RecipeRepository;
import dcom.refrigerator.api.domain.user.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.*;

@Transactional
@Service
@RequiredArgsConstructor
public class RecipeService {
    private final RecipeRepository recipeRepository;
    private final FoodRepository foodRepository;


    public Integer joinRecipe(RecipeRequestDto.RecipeRegister data){
        Optional<Food> foodOptional= foodRepository.findByName(data.getName());
        if (foodOptional.isPresent()){

            Set<Ingredient> ingredients= new HashSet<>();
            for (String ingredient: data.getIngredient()) {
                ingredients.add( Ingredient.builder().name(ingredient).build());
            }

            String amountToStr=" ";
            for (String amt: data.getIngredientAmount()){
                amountToStr+=amt+", ";
            }
            Recipe recipe=Recipe.builder().food(foodOptional.get()).ingredients(ingredients).amount(amountToStr).build();
            recipeRepository.save(recipe);
            return recipe.getId();


        } else{



        }
        return 1;
    }

}

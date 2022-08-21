package dcom.refrigerator.api.domain.recipe.service;

import antlr.StringUtils;
import dcom.refrigerator.api.domain.food.Food;
import dcom.refrigerator.api.domain.food.FoodCategory;
import dcom.refrigerator.api.domain.food.repositroy.FoodRepository;
import dcom.refrigerator.api.domain.food.service.FoodService;
import dcom.refrigerator.api.domain.ingredient.Ingredient;
import dcom.refrigerator.api.domain.ingredient.repository.IngredientRepository;
import dcom.refrigerator.api.domain.ingredient.service.IngredientService;
import dcom.refrigerator.api.domain.recipe.Recipe;
import dcom.refrigerator.api.domain.recipe.dto.RecipeRequestDto;
import dcom.refrigerator.api.domain.recipe.repository.RecipeRepository;
import dcom.refrigerator.api.domain.user.User;
import dcom.refrigerator.api.domain.user.service.UserService;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Transactional
@Slf4j
@Service
@RequiredArgsConstructor
public class RecipeService {
    private final RecipeRepository recipeRepository;
    private final FoodRepository foodRepository;
    private final FoodService foodService;
    private final UserService userService;
    private final IngredientRepository ingredientRepository;


    public Integer joinRecipe(RecipeRequestDto.RecipeRegister data) {

        Optional<Food> foodOptional = foodRepository.findByName(data.getName());

        //이미 존재하는 음식인 경우
        if (foodOptional.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 존재하는 이름입니다");
        }

        //add food
        foodService.joinFood(Food.builder()
                .name(data.getName())
                .writer(userService.getCurrentUser())
                .description(data.getDescription())
                .category(FoodCategory.valueOf(data.getCategory()))
                .build());


        Recipe recipe= Recipe.builder()
                .food(foodRepository.findByName(data.getName()).get())
                .amount(data.getIngredientAmount().
                        replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\\"", "").replaceAll("\\'",""))
                .build();


        Set<Ingredient> ingredients = new HashSet<>();

        for (String ingredient : data.getIngredient().replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\\"", "").replaceAll("\\'","").split(",")) {

            String ingredientName=ingredient.substring(1, ingredient.length());
            Optional<Ingredient> ingredientOptional= ingredientRepository.findByName(ingredientName);


            if (!ingredientOptional.isPresent()) {
                Ingredient temp = new Ingredient();

                temp.setName(ingredientName);
                ingredients.add(temp);
            }
            else{
                ingredients.add(ingredientOptional.get());
            }
        }


        recipe.setIngredients(ingredients);
        // cascade persist 로 ingredient set 저장
        return recipeRepository.save(recipe).getId();
    }


    public void deleteRecipe(Integer id){
        Recipe recipe=recipeRepository.findById(id).orElseThrow(()->new ResponseStatusException(
                HttpStatus.NOT_FOUND, "id에 해당되는 레시피가 없습니다."));

        recipeRepository.delete(recipe);
    }




}
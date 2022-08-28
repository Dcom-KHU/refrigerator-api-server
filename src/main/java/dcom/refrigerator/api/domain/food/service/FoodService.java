package dcom.refrigerator.api.domain.food.service;

import dcom.refrigerator.api.domain.food.Food;
import dcom.refrigerator.api.domain.food.FoodCategory;
import dcom.refrigerator.api.domain.food.dto.FoodRequestDto;
import dcom.refrigerator.api.domain.food.dto.FoodResponseDto;
import dcom.refrigerator.api.domain.food.repositroy.FoodRepository;
import dcom.refrigerator.api.domain.foodImage.service.FoodImageService;
import dcom.refrigerator.api.domain.ingredient.Ingredient;
import dcom.refrigerator.api.domain.ingredient.service.IngredientService;
import dcom.refrigerator.api.domain.recipe.Recipe;
import dcom.refrigerator.api.domain.recipe.service.RecipeService;
import dcom.refrigerator.api.domain.user.User;
import dcom.refrigerator.api.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.*;

@Transactional
@Service
@RequiredArgsConstructor
@Slf4j
public class FoodService {
    private final FoodRepository foodRepository;
    private final UserService userService;
    private final FoodImageService foodImageService;
    private final IngredientService ingredientService;
    private  final RecipeService recipeService;

    public Integer joinFood(FoodRequestDto.FoodRegister data) throws Exception{

        Optional<Food> foodOptionalByDescription=foodRepository.findByDescription(data.getDescription());



        //설명이 같은경우- 레시피가 이미 존재하는 경우 처리
        if (foodOptionalByDescription.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"이미 존재하는 레시피입니다. description 을 바꾸세요");
        }
        else {
            foodRepository.save(Food.builder()
                    .name(data.getName())
                    .writer(userService.getCurrentUser())
                    .description(data.getDescription())
                    .category(FoodCategory.valueOf(data.getCategory()))
                    .build());
        }
        Food foodByDescription=foodRepository.findByDescription(data.getDescription()).get();




        //비어있는 경우 내부에서 처리
        //food image 처리
        foodImageService.registerImage(data.getImages(),data.getImageDescriptions(),foodByDescription);




        //recipe 생성
        Recipe recipe = Recipe.builder()
                .food(foodByDescription)
                .amount(data.getIngredientAmount().
                        replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\\"", "").replaceAll("\\'", ""))
                .build();
        Set<Ingredient> ingredients = new HashSet<>();
        for (String ingredient : data.getIngredient().replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\\"", "").replaceAll("\\'", "").split(",")) {
            String ingredientName = ingredient.substring(0, ingredient.length()).strip();
            ingredientService.joinIngredient(Ingredient.builder().name(ingredientName).build());

            ingredients.add(ingredientService.getIngredientByName(ingredientName));
        }

        //recipe save
        recipe.setIngredients(ingredients);
        recipeService.joinRecipe(recipe);



        return foodRepository.findByDescription(data.getDescription()).orElseThrow(()-> {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"각 이미지에는 설명이 반드시 필요합니다.");})
                .getId();
    }

    public List<FoodResponseDto.FoodRecipesByUserInfo> getFoodRecipesByUserId(){
            User user= userService.getCurrentUser();
            List<FoodResponseDto.FoodRecipesByUserInfo> foodRecipesByUserInfo= new ArrayList<>();

            List<Recipe> recipes=foodRepository.findAllFoodRecipesByUserId(user.getId());


            if(!recipes.isEmpty()) {
                for (Recipe recipe : recipes) {
                    Food food =recipe.getFood();


                    foodRecipesByUserInfo.add(FoodResponseDto.FoodRecipesByUserInfo.of(
                            User.builder().id(user.getId()).nickname(user.getNickname()).build()
                            , recipe));

                }

            }
            else throw new ResponseStatusException(HttpStatus.NOT_FOUND,"해당하는 레시피 를 찾을 수 없습니다.");


            return foodRecipesByUserInfo;
    }

}

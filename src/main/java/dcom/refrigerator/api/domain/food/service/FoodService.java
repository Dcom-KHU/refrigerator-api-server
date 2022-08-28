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

        Optional<Food> foodOptionalByName=foodRepository.findByName(data.getName());



        //설명이 같은경우- 레시피가 이미 존재하는 경우 처리
        if (foodOptionalByName.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"이미 존재하는 음식입니다. ");
        }
        else {
            foodRepository.save(Food.builder()
                    .name(data.getName())
                    .writer(userService.getCurrentUser())
                    .description(data.getDescription())
                    .category(FoodCategory.valueOf(data.getCategory()))
                    .build());

        }
        Food foodByName=foodRepository.findByName(data.getName()).get();




        //비어있는 경우 내부에서 처리
        //food image 처리
        foodImageService.registerImage(data.getImages(),data.getImageDescriptions(),foodByName);


        log.info("test0");

        String[] ingredientAmounts=data.getIngredientAmount().replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\\"", "").replaceAll("\\'", "").split(",");
        Iterator<String> iter = Arrays.stream(ingredientAmounts).iterator();
        String[] ingredients= data.getIngredient().replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\\"", "").replaceAll("\\'", "").split(",");
        log.info("test1");

        //재료별 재료 양 필수
        if(ingredients.length!=ingredientAmounts.length)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"재료와 재료 양의 개수를 맞춰주세요");
        log.info("test2");

        //recipe 생성
        for (String ingredient :ingredients ) {
            if(iter.hasNext()) {
                String ingredientName = ingredient.substring(0, ingredient.length()).strip();
                ingredientService.joinIngredient(Ingredient.builder().name(ingredientName).build());
                //recipe save
                Recipe recipe = Recipe.builder()
                        .food(foodByName)
                        .amount(iter.next().strip())
                        .build();
                recipe.setIngredient(ingredientService.getIngredientByName(ingredientName));
                recipeService.joinRecipe(recipe);
            }
        }

        log.info("test3");



        return foodRepository.findByName(data.getName()).orElseThrow(()-> {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"저장 실패");})
                .getId();
    }

    public List<FoodResponseDto.FoodRecipes> getFoodsByUserId(){
            User user= userService.getCurrentUser();
            List<FoodResponseDto.FoodRecipes> foodRecipeByUserIds = new ArrayList<>();

            //user id로 음식 리스트 가져옴
            List<Food> foods= foodRepository.findAllByWriterId(user.getId());

            //음식이 없는경우
            if(foods.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,"해당하는 userId 의 음식을 찾을 수 없습니다.");
            }
            //food 별로 이름에 맞는 recipe 추가
            for(Food food:foods){
                List<Recipe> recipes=foodRepository.findAllFoodRecipesByFoodName(food.getName());
                foodRecipeByUserIds.add(FoodResponseDto.FoodRecipes.of(user,recipes,food));
            }



            return foodRecipeByUserIds;
    }



    public List<FoodResponseDto.FoodRecipes> getFoodRecipesByFoodName(String foodName){
        Optional<Food> foodOptional= foodRepository.findByName(foodName);
        if(!foodOptional.isPresent())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"해당하는 음식이 없습니다");

        List<FoodResponseDto.FoodRecipes> foodRecipes = new ArrayList<>();

        List<Recipe> recipes=foodRepository.findAllFoodRecipesByFoodName(foodName);

        Food food=foodOptional.get();
        if(!recipes.isEmpty()) {
            foodRecipes.add(FoodResponseDto.FoodRecipes.of(food.getWriter(),recipes,food));



        }
        else throw new ResponseStatusException(HttpStatus.NOT_FOUND,"해당하는 레시피 를 찾을 수 없습니다.");


        return foodRecipes;
    }


    public void deleteFoodById(Integer foodId){
        Food food=foodRepository.findById(foodId).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"해당하는 음식이 없습니다"));
        List<Recipe> recipes= recipeService.getAllByFoodId(foodId);


        foodImageService.deleteAllFoodImagesByFoodId(foodId);
        foodRepository.delete(food);
        recipeService.deleteAllRecipe(recipes);
    }
}

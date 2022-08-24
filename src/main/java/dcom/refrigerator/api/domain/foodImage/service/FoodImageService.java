package dcom.refrigerator.api.domain.foodImage.service;

import dcom.refrigerator.api.domain.food.Food;
import dcom.refrigerator.api.domain.food.FoodCategory;
import dcom.refrigerator.api.domain.food.repositroy.FoodRepository;
import dcom.refrigerator.api.domain.food.service.FoodService;
import dcom.refrigerator.api.domain.foodImage.FoodImage;
import dcom.refrigerator.api.domain.foodImage.repository.FoodImageRepository;
import dcom.refrigerator.api.domain.ingredient.Ingredient;
import dcom.refrigerator.api.domain.ingredient.repository.IngredientRepository;
import dcom.refrigerator.api.domain.recipe.Recipe;
import dcom.refrigerator.api.domain.recipe.dto.RecipeRequestDto;
import dcom.refrigerator.api.domain.recipe.repository.RecipeRepository;
import dcom.refrigerator.api.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Transactional
@Slf4j
@Service
@RequiredArgsConstructor
public class FoodImageService {
    private  final FoodImageRepository foodImageRepository;
    private  final FoodRepository foodRepository;


    public FoodImage getFoodImageById(Integer foodImageId) {
        return foodImageRepository.findById(foodImageId).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"image 를 찾을 수 없습니다."));

    }

    public FoodImage getFoodImageByOriginFileName(String originFileName) {
        return foodImageRepository.findFoodImageByOriginFileName(originFileName).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"image 를 찾을 수 없습니다."));
    }



}
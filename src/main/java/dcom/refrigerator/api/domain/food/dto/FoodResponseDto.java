package dcom.refrigerator.api.domain.food.dto;


import dcom.refrigerator.api.domain.food.Food;
import dcom.refrigerator.api.domain.ingredient.Ingredient;
import dcom.refrigerator.api.domain.recipe.Recipe;
import dcom.refrigerator.api.domain.refrigerator.Refrigerator;
import dcom.refrigerator.api.domain.user.User;
import io.swagger.annotations.ApiModel;
import lombok.*;

public class FoodResponseDto {
    @ApiModel(value = "냉장고 음식 검색 output")
    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RefrigeratorFoodOutput {
        private Refrigerator foodList;
        private Food foodName;
        private User userId;
        private Ingredient ingredientId;
        private Recipe recipeList;
    }
}

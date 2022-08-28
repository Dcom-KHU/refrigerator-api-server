package dcom.refrigerator.api.domain.food.dto;


import dcom.refrigerator.api.domain.food.Food;
import dcom.refrigerator.api.domain.ingredient.Ingredient;
import dcom.refrigerator.api.domain.recipe.Recipe;
import dcom.refrigerator.api.domain.refrigerator.Refrigerator;
import dcom.refrigerator.api.domain.user.User;
import io.swagger.annotations.ApiModel;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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



    @ApiModel(value = "음식-레시피들  정보")
    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FoodRecipesByUserInfo {

        private Integer id;

        private FoodResponseDto.Writer writer;

        private String name;

        private String description;

        private String category;

        private List<Ingredient> food;


        public static FoodResponseDto.FoodRecipesByUserInfo of(User user, Recipe recipe){
            FoodResponseDto.Writer writer= FoodResponseDto.Writer.builder().id(user.getId()).nickname(user.getNickname()).build();

            return FoodResponseDto.FoodRecipesByUserInfo.builder()
                    .id(recipe.getId())
                    .writer(writer)
                    .name(recipe.getFood().getName())
                    .description(recipe.getFood().getDescription())
                    .category(recipe.getFood().getCategory().toString())
                    .food(new ArrayList<>( recipe.getIngredients()))
                    .build();
        }




    }
    @Builder
    @Getter
    public static class Writer{
        private Integer id;

        private String nickname;
    }












}

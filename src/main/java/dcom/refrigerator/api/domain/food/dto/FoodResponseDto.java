package dcom.refrigerator.api.domain.food.dto;


import dcom.refrigerator.api.domain.food.Food;
import dcom.refrigerator.api.domain.food.FoodCategory;
import dcom.refrigerator.api.domain.foodImage.dto.FoodImageResponseDto;
import dcom.refrigerator.api.domain.ingredient.Ingredient;
import dcom.refrigerator.api.domain.recipe.Recipe;
import dcom.refrigerator.api.domain.recipe.dto.RecipeResponseDto;
import dcom.refrigerator.api.domain.refrigerator.Refrigerator;
import dcom.refrigerator.api.domain.user.User;
import dcom.refrigerator.api.domain.user.dto.UserResponseDto;
import io.swagger.annotations.ApiModel;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FoodResponseDto {
    @ApiModel(value = "음식 상세 정보")
    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Info {
        private Integer id;
        private UserResponseDto.Profile writer;
        private List<RecipeResponseDto> ingredients;
        private List<FoodImageResponseDto.FoodImageData> images;
        private String name;
        private String description;
        private String mainImage;
        private FoodCategory category;

        public static Info of(Food food) {
            return Info.builder()
                    .id(food.getId())
                    .writer(UserResponseDto.Profile.of(food.getWriter()))
                    .ingredients(RecipeResponseDto.of(food.getRecipes()))
                    .name(food.getName())
                    .images(FoodImageResponseDto.FoodImageData.of(food.getImages()))
                    .description(food.getDescription())
                    .mainImage(food.getMainImage())
                    .category(food.getCategory())
                    .build();
        }

        public static List<Info> of(List<Food> foods) {
            return foods.stream().map(Info::of).collect(Collectors.toList());
        }
    }

    @ApiModel(value = "음식 요약 정보")
    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Simple {
        private Integer id;
        private UserResponseDto.Simple writer;
        private String name;
        private String description;
        private String mainImage;
        private FoodCategory category;

        public static Simple of(Food food) {
            return Simple.builder()
                    .id(food.getId())
                    .writer(UserResponseDto.Simple.of(food.getWriter()))
                    .name(food.getName())
                    .description(food.getDescription())
                    .mainImage(food.getMainImage())
                    .category(food.getCategory())
                    .build();
        }

        public static List<Simple> of(List<Food> foods) {
            return foods.stream().map(Simple::of).collect(Collectors.toList());
        }
    }
}

package dcom.refrigerator.api.domain.foodImage.dto;

import dcom.refrigerator.api.domain.food.Food;
import dcom.refrigerator.api.domain.foodImage.FoodImage;
import dcom.refrigerator.api.domain.ingredient.Ingredient;
import dcom.refrigerator.api.domain.recipe.Recipe;
import dcom.refrigerator.api.domain.refrigerator.Refrigerator;
import dcom.refrigerator.api.domain.user.User;
import dcom.refrigerator.api.domain.user.dto.UserResponseDto;
import io.swagger.annotations.ApiModel;
import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

public class FoodImageResponseDto {




    @ApiModel(value = "사진 정보")
    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FoodImageData {

        private Integer id;
        private String originFileName;  // 파일 원본명
        private String filePath;  // 파일 저장 경로
        private String description;


        public static FoodImageResponseDto.FoodImageData of(FoodImage foodImage) {
            return FoodImageData.builder()
                    .id(foodImage.getId())
                    .originFileName(foodImage.getOriginFileName())
                    .filePath(foodImage.getFilePath())
                    .description(foodImage.getDescription())
                    .build();
        }

        public static List<FoodImageResponseDto.FoodImageData> of(List<FoodImage> foodImages) {
            return foodImages.stream().map(FoodImageResponseDto.FoodImageData::of).collect(Collectors.toList());
        }
    }
}



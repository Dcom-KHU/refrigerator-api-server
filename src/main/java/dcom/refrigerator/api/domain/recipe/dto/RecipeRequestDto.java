package dcom.refrigerator.api.domain.recipe.dto;

import com.sun.istack.NotNull;
import dcom.refrigerator.api.domain.food.Food;
import dcom.refrigerator.api.domain.food.FoodCategory;
import dcom.refrigerator.api.domain.ingredient.Ingredient;
import dcom.refrigerator.api.domain.recipe.Recipe;
import dcom.refrigerator.api.domain.user.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RecipeRequestDto {


    @ApiModel(value="recipe 등록")
    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecipeRegister {

        @NotEmpty
        @Size( max = 128, message = "길이는 128자 이내여야 합니다.")
        @ApiModelProperty(value = "음식 이름", required = true)
        private String name;

        @NotEmpty
        @ApiModelProperty(value = "레시피 설명", required = true)
        private String description;

        @NotEmpty
        @ApiModelProperty(value = "음식 카테고리 ", required = true)
        private String category;

        @NotEmpty
        @ApiModelProperty(value = "재료 이름 리스트(String type)", required = true)
        private String ingredient;

        @NotEmpty
        @Size( max = 128, message = "길이는 128자 이내여야 합니다.")
        @ApiModelProperty(value = "재료 개수 리스트(String type) ", required = true)
        private String ingredientAmount;




        @ApiModelProperty(value = "이미지 설명 ", required = true)
        private String imageDescriptions;




    }
}

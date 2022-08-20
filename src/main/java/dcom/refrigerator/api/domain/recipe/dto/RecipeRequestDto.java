package dcom.refrigerator.api.domain.recipe.dto;

import com.sun.istack.NotNull;
import dcom.refrigerator.api.domain.food.Food;
import dcom.refrigerator.api.domain.ingredient.Ingredient;
import dcom.refrigerator.api.domain.recipe.Recipe;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
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
        @ApiModelProperty(value = "레시피 이름", required = true)
        private String name;

        @ApiModelProperty(value = "레시피 설명", required = true)
        private String description;

        @ApiModelProperty(value = "음식 카테고리 ", required = true)
        private String category;

        @ApiModelProperty(value = "재료 이름 리스트", required = true)
        private List<String> ingredient;

        @ApiModelProperty(value = "재료 개수  리스트 ", required = true)
        private List<String> ingredientAmount;

        @ApiModelProperty(value = "이미지 ", required = true)
        private List<MultipartFile> images;

        @ApiModelProperty(value = "이미지 설명 ", required = true)
        private List<String> imageDescriptions;
    }

/*

        public Ingredient toRecipeEntity() {
            return Ingredient.builder()
                    .name(this.name)
                    .build();
        }
        public Ingredient toRecipeEntity() {
            return Ingredient.builder()
                    .name(this.name)
                    .build();
        }


*/

}

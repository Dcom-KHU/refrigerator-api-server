package dcom.refrigerator.api.domain.recipe.dto;

import dcom.refrigerator.api.domain.food.Food;
import dcom.refrigerator.api.domain.ingredient.Ingredient;
import dcom.refrigerator.api.domain.recipe.Recipe;
import dcom.refrigerator.api.domain.user.User;
import dcom.refrigerator.api.domain.user.dto.UserResponseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class RecipeResponseDto {

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecipeInfo {

        private Integer id;

        private Writer writer;

        private String name;

        private String description;

        private String category;

        private List<Ingredient> food;


        public static RecipeInfo of(User user,Recipe recipe){
            Writer writer1=Writer.builder().id(user.getId()).nickname(user.getNickname()).build();

            return RecipeInfo.builder()
                    .id(recipe.getId())
                    .writer(writer1)
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

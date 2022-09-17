package dcom.refrigerator.api.domain.recipe.dto;

import dcom.refrigerator.api.domain.recipe.Recipe;
import io.swagger.annotations.ApiModel;
import lombok.*;

import javax.persistence.Entity;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@ApiModel(value = "레시피 반환 정보")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecipeResponseDto {
    private Integer id;
    private String food;
    private String amount;

    public static RecipeResponseDto of(Recipe recipe) {
        return RecipeResponseDto.builder()
                .id(recipe.getId())
                .food(recipe.getFood().getName())
                .amount(recipe.getAmount())
                .build();
    }

    public static List<RecipeResponseDto> of(Collection<Recipe> recipes) {
        return recipes.stream().map(RecipeResponseDto::of).collect(Collectors.toList());
    }
}

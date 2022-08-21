package dcom.refrigerator.api.domain.ingredient.dto;

import dcom.refrigerator.api.domain.ingredient.Ingredient;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class IngredientRequestDto {

    @ApiModel(value="재료 등록")
    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class IngredientRegister {
        @NotEmpty
        @Size(max=128, message = "길이는 128자 이내여야 합니다.")
        @ApiModelProperty(value = "재료 이름", required = true)
        private String name;


        public Ingredient toEntity() {
            return Ingredient.builder()
                    .name(this.name)
                    .build();
        }

    }


}

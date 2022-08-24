package dcom.refrigerator.api.domain.food.dto;

import dcom.refrigerator.api.domain.refrigerator.Refrigerator;
import dcom.refrigerator.api.domain.user.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

public class FoodRequestDto {
    @ApiModel(value = "냉장고 음식 검색 입력")
    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RefrigeratorFoodInput {

        @ApiModelProperty(value = "음식 아이디")
        private User userId;
        private Refrigerator foodList;
    }
}

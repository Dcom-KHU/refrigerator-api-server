package dcom.refrigerator.api.domain.refrigerator.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

public class RefrigeratorRequestDto {
    @ApiModel(value="refrigerator 등록")
    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class addIngredient{
        @NotEmpty
        @Size( max = 128, message = "길이는 128자 이내여야 합니다.")
        @ApiModelProperty(value = "재료 이름", required = true)
        private String name;

        @FutureOrPresent
        @ApiModelProperty(value = "유통 기한", required = true)
        private LocalDateTime expiredDate;
    }

    @ApiModel(value="refrigerator 수정")
    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class modifyIngredient{
        @NotNull
        private Integer id;

        @FutureOrPresent
        @ApiModelProperty(value = "유통 기한", required = true)
        private LocalDateTime expiredDate;
    }
}

package dcom.refrigerator.api.domain.refrigerator.dto;

import dcom.refrigerator.api.domain.refrigerator.Refrigerator;
import io.swagger.annotations.ApiModel;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class RefrigeratorResponseDto {
    @ApiModel(value = "냉장고에 있는 재료 정보")
    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WithIngredient {
        private Integer id;
        private LocalDateTime expiredDate;
        private String name;

        public static WithIngredient of(Refrigerator refrigerator) {
            return WithIngredient.builder()
                    .id(refrigerator.getId())
                    .expiredDate(refrigerator.getExpiredDate())
                    .name(refrigerator.getIngredient().getName())
                    .build();
        }

        public static List<WithIngredient> of(Collection<Refrigerator> refrigerators) {
            return refrigerators.stream().map(WithIngredient::of).collect(Collectors.toList());
        }
    }
}

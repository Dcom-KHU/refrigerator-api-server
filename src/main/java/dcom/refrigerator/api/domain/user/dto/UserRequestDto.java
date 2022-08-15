package dcom.refrigerator.api.domain.user.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

public class UserRequestDto {

    @ApiModel(value="유저 로그인")
    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Login {
        @ApiModelProperty(value = "유저 닉네임", required = true)
        private String nickname;

        @ApiModelProperty(value = "유저 이름", required = true)
        private String name;

        @ApiModelProperty(value = "유저 좌우명", required = true)
        private String password;

        @ApiModelProperty(value = "유저 좌우명", required = true)
        private String email;
    }
}
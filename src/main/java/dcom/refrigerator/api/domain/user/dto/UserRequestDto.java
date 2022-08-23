package dcom.refrigerator.api.domain.user.dto;

import dcom.refrigerator.api.domain.user.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UserRequestDto {

    @ApiModel(value="유저 회원가입")
    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Join {
        @NotNull
        @NotEmpty
        @Size(max = 128, message = "길이는 128자 이내여야 합니다.")
        @ApiModelProperty(value = "유저 닉네임", required = true)
        private String nickname;

        @NotNull
        @NotEmpty
        @Size(max = 128, message = "길이는 128자 이내여야 합니다.")
        @ApiModelProperty(value = "유저 이름", required = true)
        private String name;

        @NotNull
        @NotEmpty
        @Size(max = 128, message = "길이는 128자 이내여야 합니다.")
        @ApiModelProperty(value = "유저 비호밀번", required = true)
        private String password;

        @NotNull
        @NotEmpty
        @Size(max = 128, message = "길이는 128자 이내여야 합니다.")
        @ApiModelProperty(value = "유저 이메일", required = true)
        private String email;

        public User toEntity() {
            return User.builder()
                    .name(this.name)
                    .nickname(this.nickname)
                    .email(this.email)
                    .password(this.password)
                    .point(0)
                    .notificationFood(false)
                    .notificationRefrigerator(false)
                    .build();
        }

    }


    @ApiModel(value="유저 로그인")
    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Login {

        @NotNull
        @NotEmpty
        @Size(max = 128, message = "길이는 128자 이내여야 합니다.")
        @ApiModelProperty(value = "유저 비호밀번", required = true)
        private String password;

        @NotNull
        @NotEmpty
        @Size(max = 128, message = "길이는 128자 이내여야 합니다.")
        @ApiModelProperty(value = "유저 이메일", required = true)
        private String email;

        public User toLoginDto() {
            return User.builder()
                    .email(this.email)
                    .password(this.password)
                    .build();
        }
    }




}
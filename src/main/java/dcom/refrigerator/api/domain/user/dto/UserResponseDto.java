package dcom.refrigerator.api.domain.user.dto;

import dcom.refrigerator.api.domain.user.User;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.List;
import java.util.stream.Collectors;

public class UserResponseDto {
    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Simple {
        private Integer id;
        private String name;
        private String nickname;
        private String email;

        public static Simple of(User user) {
            return Simple.builder()
                    .name(user.getName())
                    .email(user.getEmail())
                    .nickname(user.getNickname())
                    .build();
        }

        public static List<Simple> of(List<User> users) {
            return users.stream().map(Simple::of).collect(Collectors.toList());
        }
    }

    @Builder
    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Profile {
        private Integer id;
        private String name;
        private String nickname;
        private String email;
        private String password;
        private Integer point;
        private Boolean notificationFood;
        private Boolean notificationRefrigerator;


        public static Profile of(User user) {
            return Profile.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .nickname(user.getNickname())
                    .email(user.getEmail())
                    .password(user.getPassword())
                    .point(user.getPoint())
                    .notificationFood(Boolean.getBoolean("notificationFood"))
                    .notificationRefrigerator(Boolean.getBoolean("notificationRefrigerator"))
                    .build();
        }
    }
}
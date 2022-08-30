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
        private String nickname;
        private String email;

        public static Simple of(User user) {
            return Simple.builder()
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
        private String nickname;
        private String email;
        private Integer point;

        public static Profile of(User user) {
            return Profile.builder()
                    .id(user.getId())
                    .nickname(user.getNickname())
                    .email(user.getEmail())
                    .point(user.getPoint())
                    .build();
        }
    }
}
package dcom.refrigerator.api.global.security.config;

import dcom.refrigerator.api.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;

@ToString
@NoArgsConstructor
@Getter
@Builder
public class Token {

    private String token;
    private String refreshToken;

    public Token(String token, String refreshToken) {
        this.token = token;
        this.refreshToken = refreshToken;
    }
}
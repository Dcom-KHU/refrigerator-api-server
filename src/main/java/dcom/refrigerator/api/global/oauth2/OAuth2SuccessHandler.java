package dcom.refrigerator.api.global.oauth2;

import com.fasterxml.jackson.databind.ObjectMapper;
import dcom.refrigerator.api.domain.user.User;
import dcom.refrigerator.api.domain.user.repository.UserRepository;
import dcom.refrigerator.api.global.security.config.Token;
import dcom.refrigerator.api.global.security.config.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.Column;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {


    private final TokenService tokenService;
    private final UserRepository userRepository;

    @Value("${website.url}")
    private String websiteURL;
    @CrossOrigin(origins ="http://3.138.230.191:8080")
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {


        log.info("hello");


        OAuth2User oAuth2User = (OAuth2User)authentication.getPrincipal();

        String email = (String) oAuth2User.getAttribute("email");

        Optional<User> userOptional = userRepository.findByEmail(email);

        Token token = tokenService.generateToken(email, "USER");

        ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken", token.getToken())
                .maxAge( 24 * 60 * 60)
                .path("/")
                .domain("localhost")
                .build();

        response.addHeader("Set-Cookie", accessTokenCookie.toString());

        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", token.getRefreshToken())
                .maxAge(7 * 24 * 60 * 60)
                .path("/")
                .domain("localhost")
                .build();

        response.addHeader("Set-Cookie", refreshTokenCookie.toString());


        if( userOptional.isPresent()){
            User user=userOptional.get();
        }
        else {
            User user = userRepository.save(
                    User.builder()
                            .nickname((String) oAuth2User.getAttribute("nickname"))
                            .email(email)
                            .name((String) oAuth2User.getAttribute("name"))
                            .notificationRefrigerator(false)
                            .notificationFood(false)
                            .build()
            );
        }

        log.info("{}",response.getHeaders("Set-Cookie"));

        response.setContentType("text/plain;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        getRedirectStrategy().sendRedirect(request, response, websiteURL + "/user/test");


    }
}
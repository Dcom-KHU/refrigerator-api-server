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
    private final ObjectMapper objectMapper;

    @Value("${website.url}")
    private String websiteURL;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {




        OAuth2User oAuth2User = (OAuth2User)authentication.getPrincipal();

        String email = (String) oAuth2User.getAttribute("email");


        Optional<User> userOptional = userRepository.findByEmail(email);


        log.info("{}", userOptional);


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

        /*Token token = tokenService.generateToken(email, "USER");
        Cookie accessTokenCookie= new Cookie("access",token.getToken());
        accessTokenCookie.setMaxAge(60*10);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setDomain("localhost");
        accessTokenCookie.setHttpOnly(true);

        response.addCookie(accessTokenCookie);

        Cookie refreshTokenCookie= new Cookie("refresh", token.getRefreshToken());
        refreshTokenCookie.setMaxAge(2*7*24*60*60);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setDomain("localhost");
        refreshTokenCookie.setHttpOnly(true);

        response.addCookie(refreshTokenCookie);*/

        Token token = tokenService.generateToken(email, "USER");

        ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken", token.getToken())
                .path("/")
                .secure(true)
                .sameSite("None")
                .httpOnly(true)
                .domain("localhost")
                .build();

        response.setHeader("Set-Cookie", accessTokenCookie.toString());

        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", token.getRefreshToken())
                .path("/")
                .secure(true)
                .sameSite("None")
                .httpOnly(true)
                .domain("localhost")
                .build();

        response.addHeader("Set-Cookie", refreshTokenCookie.toString());




        getRedirectStrategy().sendRedirect(request, response,websiteURL);
        /*writeTokenResponse(response, token,tokenCookie,refreshTokenCookie);*/
    }


    private void writeTokenResponse(HttpServletResponse response, Token token,ResponseCookie cookie,ResponseCookie cookie2)
            throws IOException {




        response.setHeader("Set-Cookie", cookie.toString());
        response.addHeader("Set-Cookie", cookie2.toString());
        response.setContentType("application/json;charset=UTF-8");


        log.info("cookie");
        log.info("{}",response.getHeaders("Set-Cookie"));

        var writer = response.getWriter();
        writer.println(objectMapper.writeValueAsString(token));
        writer.flush();
    }



}
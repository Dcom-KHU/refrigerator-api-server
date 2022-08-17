package dcom.refrigerator.api.global.security.config;


import dcom.refrigerator.api.domain.user.dto.UserRequestDto;
import dcom.refrigerator.api.domain.user.dto.UserResponseDto;
import dcom.refrigerator.api.domain.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.web.servlet.oauth2.login.RedirectionEndpointDsl;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;

@Api(tags = {"token Controller"})
@Slf4j
@RequiredArgsConstructor
@ResponseBody
@Controller
@RequestMapping(value = "/token")
public class TokenController {
    private final TokenService tokenService;
    private final UserService userService;

    @Value("${website.url}")
    private String websiteURL;

    @GetMapping("/expired")
    public String auth() {
        throw new RuntimeException();
    }

    @GetMapping("/refresh")
    public String refreshAuth(HttpServletRequest request, HttpServletResponse response) {
        String token = request.getHeader("Refresh");
        if (token != null && tokenService.verifyToken(token)) {

            String email = tokenService.getUid(token);
            Token newToken = tokenService.generateToken(email, "USER");

            response.addHeader("Auth", newToken.getToken());
            response.addHeader("Refresh", newToken.getRefreshToken());
            response.setContentType("application/json;charset=UTF-8");

            return "HAPPY NEW TOKEN";
        }

        throw new RuntimeException();
    }

    @PostMapping("/login")
    public ResponseEntity<String> successLogin(@RequestBody UserRequestDto.Login login) throws URISyntaxException {

        Token token=tokenService.generateToken(userService.findSimpleByEmail(login.getEmail()).getEmail(),"USER");

        ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken", token.getToken())
                .path("/")
                .secure(true)
                .sameSite("None")
                .httpOnly(true)
                .domain("localhost")
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Set-Cookie", accessTokenCookie.toString());

        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", token.getRefreshToken())
                .path("/")
                .secure(true)
                .sameSite("None")
                .httpOnly(true)
                .domain("localhost")
                .build();

        headers.add("Set-Cookie", refreshTokenCookie.toString());

        URI uri=new URI("redirect://user/test");
        headers.setLocation(uri);

        return ResponseEntity.ok()
                .headers(headers)
                .body("토큰이 왔어요~");
    }



}
package dcom.refrigerator.api.global.security.config;


import dcom.refrigerator.api.domain.user.dto.UserRequestDto;
import dcom.refrigerator.api.domain.user.dto.UserResponseDto;
import dcom.refrigerator.api.domain.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
import org.springframework.web.server.ResponseStatusException;

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

    @ApiOperation("refreshToken 쿠키를 주면 accessToken 쿠키를 재발급합니다")
    @GetMapping("/refresh")
    public ResponseEntity<String> refreshToken(@CookieValue("refreshToken") String refreshToken, HttpServletRequest request, HttpServletResponse response) {
        String token = refreshToken;
        if (token != null && tokenService.verifyToken(token)) {

            String email = tokenService.getUid(token);
            Token newToken = tokenService.generateOnlyAccessToken(email, "USER");

            ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken", newToken.getToken())
                    .maxAge(24 * 60 * 60)
                    .path("/")
                    .build();

            HttpHeaders headers = new HttpHeaders();
            headers.set("Set-Cookie", accessTokenCookie.toString());



            return ResponseEntity.status(HttpStatus.CREATED)
                    .headers(headers)
                    .body("무야호~");
        }

        throw new RuntimeException();
    }





}
package dcom.refrigerator.api.domain.user.controller;

import com.nimbusds.oauth2.sdk.Response;
import dcom.refrigerator.api.domain.user.User;
import dcom.refrigerator.api.domain.user.dto.UserRequestDto;
import dcom.refrigerator.api.domain.user.dto.UserResponseDto;
import dcom.refrigerator.api.domain.user.service.UserService;
import dcom.refrigerator.api.global.security.config.Token;
import dcom.refrigerator.api.global.security.config.TokenService;
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
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Api(tags = {"user Controller"})
@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    @Value("${website.url}")
    private String websiteURL;
    private final TokenService tokenService;
    private final UserService userService;
    @ApiOperation("로그인을 합니다")
    @PostMapping("/login")
    public ResponseEntity<String> successLogin(@RequestBody UserRequestDto.Login login) throws URISyntaxException {

        UserResponseDto.Profile profile=userService.findProfileByEmail(login.getEmail());
        log.info(profile.getEmail());
        log.info(login.getEmail());

        if (!profile.getEmail().equals(login.getEmail())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "잘못된 이메일입니다.");
        } else if (!profile.getPassword().equals(login.getPassword())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "잘못된 비밀번호 입니다.");
        } else {

            Token token=tokenService.generateToken(profile.getEmail(),"USER");



            ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken", token.getToken())
                    .maxAge( 24 * 60 * 60)
                    .path("/")
                    .secure(true)
                    .sameSite("None")
                    .build();

            HttpHeaders headers = new HttpHeaders();
            headers.set("Set-Cookie", accessTokenCookie.toString());

            ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", token.getRefreshToken())
                    .maxAge( 7*24 * 60 * 60)
                    .path("/")
                    .secure(true)
                    .sameSite("None")
                    .build();

            headers.add("Set-Cookie", refreshTokenCookie.toString());

            URI uri=new URI(websiteURL+"/user/test");
            headers.setLocation(uri);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body("로그인 성");}
    }


    @ApiOperation("회원가입을 합니다")
    @PostMapping("/join")
    public ResponseEntity<String> successJoin(@RequestBody UserRequestDto.Join join) throws URISyntaxException {


        userService.joinUser(join);


        Token token=tokenService.generateToken(join.getEmail(),"USER");



        ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken", token.getToken())
                .maxAge( 24 * 60 * 60)
                .path("/")
                .secure(true)
                .sameSite("None")
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Set-Cookie", accessTokenCookie.toString());

        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", token.getRefreshToken())
                .maxAge( 7*24 * 60 * 60)
                .path("/")
                .secure(true)
                .sameSite("None")
                .build();

        headers.add("Set-Cookie", refreshTokenCookie.toString());

        URI uri=new URI(websiteURL+"/user/test");
        headers.setLocation(uri);

        return ResponseEntity.status(HttpStatus.CREATED)
                .headers(headers)
                .body("회원가입 성공!");
    }

}



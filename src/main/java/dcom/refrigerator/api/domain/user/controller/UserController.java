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
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.security.web.RedirectStrategy;


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
    public ResponseEntity<UserResponseDto.Simple> successLogin(@Valid @RequestBody UserRequestDto.Login login, HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws URISyntaxException {
        User user=login.toLoginDto();
        if (userService.verifyLoginUser(user)){

            log.info("user");
            log.info(user.getEmail());


            Token token = tokenService.generateToken(user.getEmail(), "USER");


            ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken", token.getToken())
                    .maxAge(24 * 60 * 60)
                    .path("/")
                    .build();

            HttpHeaders headers = new HttpHeaders();
            headers.set("Set-Cookie", accessTokenCookie.toString());

            ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", token.getRefreshToken())
                    .maxAge(7 * 24 * 60 * 60)
                    .path("/")
                    .build();




            headers.add("Set-Cookie", refreshTokenCookie.toString());

            URI uri = new URI("/");
            headers.setLocation(uri);

            log.info(accessTokenCookie.toString());
            log.info(refreshTokenCookie.toString());

            return ResponseEntity.status(HttpStatus.CREATED).headers(headers).body(userService.getSimpleByEmail(user.getEmail()));

        }
        else throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"로그인 실패");
    }

    @ApiOperation("회원가입을 합니다")
    @PostMapping("/join")
    public ResponseEntity<UserResponseDto.Profile> successJoin(@Valid @RequestBody UserRequestDto.Join join) throws URISyntaxException {


        userService.joinUser(join);


        Token token = tokenService.generateToken(join.getEmail(), "USER");


        ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken", token.getToken())
                .maxAge(24 * 60 * 60)
                .path("/")
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Set-Cookie", accessTokenCookie.toString());

        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", token.getRefreshToken())
                .maxAge(7 * 24 * 60 * 60)
                .path("/")
                .build();

        headers.add("Set-Cookie", refreshTokenCookie.toString());

        URI uri = new URI("/");
        headers.setLocation(uri);

        return ResponseEntity.status(HttpStatus.CREATED)
                .headers(headers)
                .body(userService.getProfileByEmail(join.getEmail()));
    }


    @ApiOperation("해당 id를  가진 유저의 정보를 삭제 합니다.")
    @DeleteMapping(value = "/delete/{userId}")
    public ResponseEntity<Void> deleteId(@ApiParam(value="유저 id", required = true) @PathVariable final Integer userId) {
        userService.deleteUser(userId);

        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @ApiOperation("해당 email 을 가진 유저의 간단한 정보를 반환 합니다.")
    @GetMapping(value = "/{userEmail}")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<UserResponseDto.Simple> getUserByEmail (
            @ApiParam(value = "유저 email", required = true) @PathVariable final String userEmail){

        return ResponseEntity.ok(userService.getSimpleByEmail(userEmail));
    }


    @ApiOperation("현재 유저의 전체 정보를 반환 합니다.")
    @GetMapping(value = "/my_profile/info")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<UserResponseDto.Profile> getMyProfile() {
        log.info("controller");
        return ResponseEntity.ok(userService.getMyProfile());
    }





}



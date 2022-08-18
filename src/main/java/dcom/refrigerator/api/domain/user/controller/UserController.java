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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

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

    @ApiOperation("test용")
    @GetMapping("/test")
    public String test(){

        log.info("success");
        return "test";
    }

}

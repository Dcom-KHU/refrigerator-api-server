package dcom.refrigerator.api.domain.user.service;

import dcom.refrigerator.api.domain.user.User;
import dcom.refrigerator.api.domain.user.dto.UserRequestDto;
import dcom.refrigerator.api.domain.user.dto.UserResponseDto;
import dcom.refrigerator.api.domain.user.repository.UserRepository;
import dcom.refrigerator.api.global.security.config.Token;
import dcom.refrigerator.api.global.security.config.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
@RequiredArgsConstructor
public class UserService {
    protected final UserRepository userRepository;
    private final HttpServletRequest request;


    public UserResponseDto.Simple findSimpleByEmail(String email){
        return UserResponseDto.Simple.of(userRepository.findByEmail(email).orElseThrow(
                ()->new ResponseStatusException(HttpStatus.NOT_FOUND,"유저를 찾을 수 없습니다.")
        ));
    }


    private String getUserEmailInHeader() {
        String userEmailString = request.getHeader("userEmail");

        if (userEmailString == null)
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        try {
            return userEmailString;
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "userID를 파싱할 수 없습니다.");
        }
    }

    public User getCurrentUser() {
        return userRepository.findByEmail(getUserEmailInHeader()).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"이메일를 찾을 수 없습니다."));
    }
}
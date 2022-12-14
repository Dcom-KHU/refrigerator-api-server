package dcom.refrigerator.api.domain.user.service;

import dcom.refrigerator.api.domain.user.User;
import dcom.refrigerator.api.domain.user.dto.UserRequestDto;
import dcom.refrigerator.api.domain.user.dto.UserResponseDto;
import dcom.refrigerator.api.domain.user.repository.UserRepository;
import dcom.refrigerator.api.global.security.config.Token;
import dcom.refrigerator.api.global.security.config.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final HttpServletRequest request;




    public UserResponseDto.Simple getSimpleByEmail(String email){
        return UserResponseDto.Simple.of(userRepository.findByEmail(email).orElseThrow(
                ()->new ResponseStatusException(HttpStatus.NOT_FOUND,"유저를 찾을 수 없습니다.")
        ));
    }


    public UserResponseDto.Profile getProfileByEmail(String email){
        return UserResponseDto.Profile.of(userRepository.findByEmail(email).orElseThrow(
                ()->new ResponseStatusException(HttpStatus.NOT_FOUND,"유저를 찾을 수 없습니다.")
        ));
    }


    public void joinUser(UserRequestDto.Join data){
        Optional<User> checkDuplicate=userRepository.findByEmail(data.getEmail());

        if (checkDuplicate.isPresent())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"이미 존재하는 회원입니다");

        else {
            User user=data.toEntity();
            userRepository.save(user);
        }

    }

    public Boolean verifyLoginUser(User user) {

        User user1 = userRepository.findByEmail(user.getEmail()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다.")
        );

        if (!user1.getEmail().equals(user.getEmail())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "잘못된 이메일입니다.");
        } else if (!user1.getPassword().equals(user.getPassword())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "잘못된 비밀번호 입니다.");
        } else return true;
    }

    public  void deleteUser(Integer id ){
        User user1 = userRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다.")
        );

        log.info("{}",user1);
        userRepository.delete(user1);
    }



    private Integer getUserIdInHeader() {
        String userIdString = request.getHeader("userId");
        log.info("id");
        log.info(userIdString);

        if (userIdString == null)
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        try {
            return Integer.parseInt(userIdString);
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "userID를 파싱할 수 없습니다.");
        }
    }


    public User getCurrentUser() {
        return userRepository.getById(getUserIdInHeader());
    }


    public UserResponseDto.Profile getMyProfile() {
        log.info("service");

        return UserResponseDto.Profile.of(userRepository.getAllInfoOfUserById(getCurrentUser().getId()).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다."
                )
        ));
    }



    public UserResponseDto.Profile getMyProfileByJwt(String accessToken) {
        log.info("service");

        return UserResponseDto.Profile.of(tokenService.getUserByToken(accessToken));
    }

    public void storeRefreshToken(User user, String refreshToken){
        User user1= userRepository.findByEmail(user.getEmail()).orElseThrow(
                ()->new ResponseStatusException(HttpStatus.NOT_FOUND,"해당하는 user 가 없습니다."));
        user1.setRefreshToken(refreshToken);
        userRepository.save(user1);

    }

    public User getUserByEmail(String email){
        Optional<User> user= userRepository.findByEmail(email);
        if (!user.isPresent())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"해당하는 이메일을 가진 유저가 없습니다.");

        else return user.get();
    }


    public Boolean checkRefreshTokenWithDB(User user,String refreshToken){
        if(user.getRefreshToken().equals(refreshToken))
            return true;
        else
            return false;
    }

}
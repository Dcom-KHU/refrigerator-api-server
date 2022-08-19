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

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
@RequiredArgsConstructor
public class UserService {
    protected final UserRepository userRepository;


    public UserResponseDto.Simple findSimpleByEmail(String email){

        return UserResponseDto.Simple.of(userRepository.findByEmail(email).orElseThrow(
                ()->new ResponseStatusException(HttpStatus.NOT_FOUND,"유저를 찾을 수 없습니다.")
        ));
    }


    public UserResponseDto.Profile findProfileByEmail(String email){

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
}
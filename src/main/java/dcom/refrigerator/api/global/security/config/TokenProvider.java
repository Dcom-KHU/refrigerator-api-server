package dcom.refrigerator.api.global.security.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.stream.Collectors;

@Component
public class TokenProvider implements InitializingBean {


    private final Logger logger = LoggerFactory.getLogger(TokenProvider.class);


    private final String secret;
    private final  long tokenValidityInSeconds;
    private Key key;

    public TokenProvider(
            @Value("${jwt.secret}") String secret, @Value("${jwt.token-validity-in-seconds}") long tokenValidityInSeconds)
    {
        this.secret=secret;
        this.tokenValidityInSeconds= tokenValidityInSeconds*1000;
    }




    @Override
    public void afterPropertiesSet(){
        byte [] keyBytes = Decoders.BASE64.decode(secret);
        this.key= Keys.hmacShaKeyFor(keyBytes);
    }

    public String createToken(Authentication authentication){
        String authories= authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));

        /*return Jwts.builder()
                .setSubject();
*/
        return "";
    }


}

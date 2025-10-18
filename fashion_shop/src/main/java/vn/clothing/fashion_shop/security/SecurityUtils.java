package vn.clothing.fashion_shop.security;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import vn.clothing.fashion_shop.web.rest.DTO.authenticate.ResponseLoginDTO;
import vn.clothing.fashion_shop.web.rest.DTO.authenticate.ResponseLoginDTO.UserInsideToken;

@Service
public class SecurityUtils {
    //access token time
    @Value("${fashionshop.jwt.access-token-validity-in-seconds}")
    private long accessTokenExpiration;
    
    @Value("${fashionshop.jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenExpiration;
    private final JwtEncoder jwtEncoder;

    public SecurityUtils(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    public static final MacAlgorithm JWT_ALGORITHM = MacAlgorithm.HS512;
    

    public String createAccessToken(String email, ResponseLoginDTO<Object> responseLoginDTO){
        ResponseLoginDTO.UserInsideToken userToken = new ResponseLoginDTO.UserInsideToken();
        userToken.setId(responseLoginDTO.getUser().getId());
        userToken.setFullName(responseLoginDTO.getUser().getFullName());
        userToken.setEmail(responseLoginDTO.getUser().getEmail());
        Instant now = Instant.now();
        Instant validity = now.plus(accessTokenExpiration, ChronoUnit.SECONDS);
        JwtClaimsSet.Builder builder = JwtClaimsSet.builder()
            .issuedAt(now)
            .expiresAt(validity)
            .subject(email)
            .claim("user", userToken)
        ;
        JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, builder.build())).getTokenValue();
    }
    
    public String createRefreshToken(String email, ResponseLoginDTO.ResponseUserData responseLoginDTO){
        ResponseLoginDTO.UserInsideToken userToken =new ResponseLoginDTO.UserInsideToken();
        userToken.setId(responseLoginDTO.getId());
        userToken.setFullName(responseLoginDTO.getFullName());
        userToken.setEmail(responseLoginDTO.getEmail());
        Instant now = Instant.now();
        Instant validity = now.plus(refreshTokenExpiration, ChronoUnit.SECONDS);
        JwtClaimsSet.Builder builder = JwtClaimsSet.builder()
            .issuedAt(now)
            .expiresAt(validity)
            .subject(email)
            .claim("user", userToken)
        ;
        JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, builder.build())).getTokenValue();
    }
}

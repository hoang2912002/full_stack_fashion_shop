package vn.clothing.fashion_shop.security;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.ClaimAccessor;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Service;

import com.nimbusds.jose.util.Base64;

import vn.clothing.fashion_shop.config.SecurityJwtConfiguration;
import vn.clothing.fashion_shop.web.rest.DTO.authenticate.ResponseLoginDTO;
import vn.clothing.fashion_shop.web.rest.DTO.authenticate.ResponseLoginDTO.UserInsideToken;

@Service
public class SecurityUtils {
    // access token time
    @Value("${fashionshop.jwt.access-token-validity-in-seconds}")
    private long accessTokenExpiration;

    @Value("${fashionshop.jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenExpiration;
    private final JwtEncoder jwtEncoder;

    @Value("${fashionshop.jwt.base64-secret}")
    private String jwtKey;


    public SecurityUtils(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    public static final MacAlgorithm JWT_ALGORITHM = MacAlgorithm.HS512;

    public String createAccessToken(String email, ResponseLoginDTO<Object> responseLoginDTO) {
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
                .claim("user", userToken);
        JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, builder.build())).getTokenValue();
    }

    public String createRefreshToken(String email, ResponseLoginDTO.ResponseUserData responseLoginDTO) {
        ResponseLoginDTO.UserInsideToken userToken = new ResponseLoginDTO.UserInsideToken();
        userToken.setId(responseLoginDTO.getId());
        userToken.setFullName(responseLoginDTO.getFullName());
        userToken.setEmail(responseLoginDTO.getEmail());
        Instant now = Instant.now();
        Instant validity = now.plus(refreshTokenExpiration, ChronoUnit.SECONDS);
        JwtClaimsSet.Builder builder = JwtClaimsSet.builder()
                .issuedAt(now)
                .expiresAt(validity)
                .subject(email)
                .claim("user", userToken);
        JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, builder.build())).getTokenValue();
    }

    public static Optional<String> getCurrentUserLogin() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(extractPrincipal(securityContext.getAuthentication()));
    }

    private static String extractPrincipal(Authentication authentication) {
        if (authentication == null) {
            return null;
        } else if (authentication.getPrincipal() instanceof UserDetails springSecurityUser) {
            return springSecurityUser.getUsername();
        } else if (authentication.getPrincipal() instanceof Jwt jwt) {
            return jwt.getSubject();
        } else if (authentication.getPrincipal() instanceof String s) {
            return s;
        }
        return null;
    }

    public static Optional<String> getCurrentUserJWT() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication())
                .filter(authentication -> authentication.getCredentials() instanceof String)
                .map(authentication -> (String) authentication.getCredentials());
    }

    /**
     * Get the Id of the current user.
     *
     * @return the Id of the current user.
     */
    public static Optional<Long> getCurrentUserId() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication())
                .filter(authentication -> authentication.getPrincipal() instanceof ClaimAccessor)
                .map(authentication -> (ClaimAccessor) authentication.getPrincipal())
                .map(principal -> principal.getClaim("user"));
    }

    public Jwt getUserFromJWTToken(String token) {
        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withSecretKey(getSecretKey())
                .macAlgorithm(JWT_ALGORITHM).build();
        try {
            return jwtDecoder.decode(token);
        } catch (Exception e) {
            throw e;
        }
    }

    public SecretKey getSecretKey() {
        byte[] keyBytes = Base64.from(jwtKey).decode();
        return new SecretKeySpec(keyBytes, 0, keyBytes.length, JWT_ALGORITHM.getName());
    }

}

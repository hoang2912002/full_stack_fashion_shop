package vn.clothing.fashion_shop.service.impls;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.clothing.fashion_shop.service.AuthenticateService;
import vn.clothing.fashion_shop.web.rest.DTO.responses.LoginResponse.ResponseUserData;
import vn.clothing.fashion_shop.web.rest.DTO.responses.LoginResponse.UserInsideToken;
import vn.clothing.fashion_shop.web.rest.errors.EnumError;
import vn.clothing.fashion_shop.web.rest.errors.ServiceException;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticateServiceImpl implements AuthenticateService {
    @Value("${fashionshop.jwt.access-token-validity-in-seconds}")
    private long accessTokenExpiration;

    @Value("${fashionshop.jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenExpiration;

    private final JwtEncoder jwtEncoder;

    @Value("${fashionshop.jwt.base64-secret}")
    private String jwtKey;

    public static final MacAlgorithm JWT_ALGORITHM = MacAlgorithm.HS512;

    @Override
    @Transactional(readOnly = true)
    public String createAccessToken(String email, ResponseUserData responseLoginDTO) {
        log.info("[createAccessToken] start create accessToken ....");
        try {
            return this.generateToken(email, accessTokenExpiration,responseLoginDTO);
        } catch (Exception e) {
            log.error("[createAccessToken] Error: {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public String createRefreshToken(String email, ResponseUserData responseLoginDTO) {
        log.info("[createRefreshToken] start create refreshToken ....");
        try {
            return this.generateToken(email, refreshTokenExpiration,responseLoginDTO);
        } catch (Exception e) {
            log.error("[createRefreshToken] Error: {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }

    private String generateToken(String email, Long expiration,ResponseUserData responseLoginDTO){
        try {
            final UserInsideToken userToken = UserInsideToken.builder()
                .id(responseLoginDTO.getId())
                .fullName(responseLoginDTO.getFullName())
                .email(responseLoginDTO.getEmail())
                .build();
            final Instant now = Instant.now();
            final Instant validity = now.plus(expiration, ChronoUnit.SECONDS);
            final JwtClaimsSet.Builder builder = JwtClaimsSet.builder()
                    .issuedAt(now)
                    .expiresAt(validity)
                    .subject(email)
                    .claim("user", userToken);
            JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
            return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, builder.build())).getTokenValue();
        } catch (Exception e) {
            log.error("[generateToken] Error: {}", e.getMessage(), e);
            throw new ServiceException(EnumError.INTERNAL_ERROR, "sys.internal.error");
        }
    }

}

package com.github.lzm320a99981e.compnent.token;

import com.github.lzm320a99981e.zodiac.tools.IdGenerator;
import com.google.common.base.Preconditions;
import io.jsonwebtoken.*;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

/**
 * 令牌管理
 */
@Slf4j
public class TokenManager {
    /**
     * 标识用户生成令牌时所传入的参数
     */
    private static final String TOKEN_BODY_KEY = "BODY";

    /**
     * 标识令牌为一个刷新令牌
     */
    private static final String REFRESH_TOKEN_MARK_KEY = "__REFRESH_TOKEN_MARK_KEY__";

    /**
     * 签名算法
     */
    private SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    /**
     * 秘钥
     */
    private final String base64EncodedSecretKey;

    /**
     * 访问令牌有效时间（单位：秒）
     */
    @Setter
    private Integer accessTokenDurationInSeconds;

    /**
     * 刷新令牌有效时间（单位：秒），需要大于有效令牌的有效时间
     */
    @Setter
    private Integer refreshTokenDurationInSeconds;


    public TokenManager(String base64EncodedSecretKey) {
        this.base64EncodedSecretKey = base64EncodedSecretKey;
    }

    public TokenManager(SignatureAlgorithm signatureAlgorithm, String base64EncodedSecretKey) {
        this.signatureAlgorithm = signatureAlgorithm;
        this.base64EncodedSecretKey = base64EncodedSecretKey;
    }

    public Token generate() {
        return generate(Collections.emptyMap());
    }

    /**
     * 生成令牌
     *
     * @param body
     * @return
     */
    public Token generate(Map<String, Object> body) {
        Preconditions.checkNotNull(body);
        Token token = new Token();
        // 访问令牌
        builder(token, body, true);
        // 刷新令牌
        builder(token, body, false);
        return token;
    }

    /**
     * 刷新令牌
     *
     * @param refreshToken
     * @return
     */
    public Token refresh(String refreshToken) {
        Claims claims = parse(refreshToken);
        if (!claims.containsKey(REFRESH_TOKEN_MARK_KEY)) {
            throw new TokenException("此令牌不符合刷新令牌规范，请核对后再使用", TokenException.Type.INVALID);
        }

        Token token = new Token();
        builder(token, (Map<String, Object>) claims.get(TOKEN_BODY_KEY), true);
        token.setRefreshToken(refreshToken);

        return token;
    }

    /**
     * 校验令牌
     *
     * @param token
     * @return
     */
    public Map<String, Object> verify(String token) {
        return (Map<String, Object>) parse(token).get(TOKEN_BODY_KEY);
    }

    /**
     * 解析令牌
     *
     * @param token
     * @return
     */
    private Claims parse(String token) {
        try {
            Jwt jwt = Jwts.parser().setSigningKey(this.base64EncodedSecretKey).parse(token);
            return (Claims) jwt.getBody();
        } catch (Exception e) {
            if (ExpiredJwtException.class.isAssignableFrom(e.getClass())) {
                throw new TokenException(e, TokenException.Type.EXPIRED);
            }
            throw new TokenException(e, TokenException.Type.INVALID);
        }
    }

    /**
     * 构建令牌
     *
     * @param token
     * @param body
     * @param access
     */
    private void builder(Token token, Map<String, Object> body, boolean access) {
        DateTime now = DateTime.now();
        // 令牌构建
        JwtBuilder builder = Jwts.builder().setId(IdGenerator.uuid32()).setIssuedAt(now.toDate()).signWith(this.signatureAlgorithm, this.base64EncodedSecretKey);
        // 令牌携带数据体
        builder.claim(TOKEN_BODY_KEY, body);
        // 有效时间
        Integer expiresIn = access ? this.accessTokenDurationInSeconds : this.refreshTokenDurationInSeconds;
        if (Objects.nonNull(expiresIn)) {
            builder.setExpiration(now.plusSeconds(expiresIn).toDate());
        }
        // 访问令牌
        if (access) {
            token.setExpiresIn(expiresIn);
            token.setAccessToken(builder.compact());
            return;
        }
        // 刷新令牌
        builder.claim(REFRESH_TOKEN_MARK_KEY, true);
        token.setRefreshToken(builder.compact());
    }
}

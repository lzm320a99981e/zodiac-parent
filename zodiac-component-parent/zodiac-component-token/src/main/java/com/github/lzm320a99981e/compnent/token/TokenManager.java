package com.github.lzm320a99981e.compnent.token;

import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.Map;

@Slf4j
public class TokenManager {
    private static final String TOKEN_BODY_KEY = "BODY";
    private static final String ACCESS_TOKEN_MARK_KEY = "__ACCESS_TOKEN_MARK_KEY__";
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

    /**
     * 生成Token
     *
     * @param body
     * @return
     */
    public Token generate(Date expiration, Map<String, Object> body) {
//        Preconditions.checkNotNull(expiration);
//        Preconditions.checkNotNull(body);
//        Token.TokenBuilder builder = Token.builder();
//        // body
//        builder.body(body);
//        // id
//        String id = UUID.randomUUID().toString();
//        builder.id(id);
//        // 创建时间
//        Date creation = new Date();
//        builder.keyCreation(creation);
//        // 过期时间
//        builder.keyExpiration(expiration);
//        // 生成 key
//        builder.key(
//                Jwts.builder()
//                        .setId(id)
//                        .claim(TOKEN_BODY_KEY, body)
//                        .signWith(this.signatureAlgorithm, this.base64EncodedSecretKey)
//                        .setIssuedAt(creation)
//                        .setExpiration(expiration)
//                        .compact()
//        );
//        return builder.build();
        return null;
    }


}

package com.github.lzm320a99981e.component.token;

import com.alibaba.fastjson.JSON;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class TokenManagerTests {
    @Test
    public void test() {
        final TokenManager tokenManager = new TokenManager("12345678");
        tokenManager.setAccessTokenDurationInSeconds(120);
        tokenManager.setRefreshTokenDurationInSeconds(1200);
        final HashMap<String, Object> body = new HashMap<>();
        body.put("aa", "张三");

        final Token token = tokenManager.generate(body);
        System.out.println(JSON.toJSONString(token, true));
        System.out.println(JSON.toJSONString(tokenManager.verify(token.getAccessToken()), true));
    }

    @Test
    public void test2() {
        final TokenManager tokenManager = new TokenManager("12345678");
        final Map<String, Object> body = tokenManager.verify("eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI4N2NkZmIzY2YzMzg0NjU2YjY5OWMyNTY0YzcxYzdlZSIsImlhdCI6MTU1NjA3MzU5NywiQk9EWSI6e30sImV4cCI6MTU1NjA3MzcxN30.4WeLXA9zEI3UtL_UQxhQDaOJzZvY_a5Qt3RBlm1O4yI");
        System.out.println(body);
        // eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI4N2NkZmIzY2YzMzg0NjU2YjY5OWMyNTY0YzcxYzdlZSIsImlhdCI6MTU1NjA3MzU5NywiQk9EWSI6e30sImV4cCI6MTU1NjA3MzcxN30.4WeLXA9zEI3UtL_UQxhQDaOJzZvY_a5Qt3RBlm1O4yI
    }
}

package com.x2bee.common.base.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

public interface TokenServiceForFilter {
    public boolean verifyToken(String token) ;
    public Jws<Claims> parseToken(String token) ;
}

package com.nasrulhazim.app.security;

public class SecurityConstants {
    public static final String SECRET = "785a2a755978674d7c4e437a3b2f62515f38383362556866576f313d72";
    public static final long EXPIRATION_TIME = 864_000_000; // 10 days
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/sign-up";
}

package com.rbf.product.common.security;

public final class AuthTokenContext {

    private static final ThreadLocal<String> CURRENT_TOKEN = new ThreadLocal<>();

    private AuthTokenContext() {
    }

    public static void setToken(String token) {
        CURRENT_TOKEN.set(token);
    }

    public static String getToken() {
        return CURRENT_TOKEN.get();
    }

    public static void clear() {
        CURRENT_TOKEN.remove();
    }
}

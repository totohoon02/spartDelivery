package com.sparta.spartdelivery.enums;

public enum UserRoleEnum {
    BOSS(Authority.BOSS),       // 사장 권한
    CLIENT(Authority.CLIENT);   // 고객 권한

    private final String authority;

    UserRoleEnum(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return this.authority;
    }

    public static class Authority {
        public static final String BOSS = "ROLE_BOSS";
        public static final String CLIENT = "ROLE_CLIENT";
    }
}
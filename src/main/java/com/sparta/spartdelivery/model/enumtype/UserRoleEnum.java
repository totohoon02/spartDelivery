package com.sparta.spartdelivery.model.enumtype;

public enum UserRoleEnum {
    BOSS(Authority.BOSS),  // 사용자 권한
    CLIENT(Authority.CLIENT);  // 관리자 권한

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
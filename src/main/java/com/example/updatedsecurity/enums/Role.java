package com.example.updatedsecurity.enums;

public enum Role {
    ADMIN("A"), USER("U");
    private String shortRole;

    private Role(String shortRole){
        this.shortRole = shortRole;
    }

    public String getShortRole() {
        return shortRole;
    }

    public static Role fromShortRole(String shortRole) {
        switch (shortRole) {
            case "U":
                return Role.USER;
            case "A":
                return Role.ADMIN;
            default:
                throw new IllegalArgumentException("not in records");
        }
    }
}

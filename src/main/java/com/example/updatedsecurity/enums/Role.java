package com.example.updatedsecurity.enums;

public enum Role {
    ADMIN("A"), MANAGER("M"), USER("U"), EMPLOYEE("W");
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
            case "M":
                return Role.MANAGER;
            case "E":
                return Role.EMPLOYEE;
            default:
                throw new IllegalArgumentException("not in records");
        }
    }
}

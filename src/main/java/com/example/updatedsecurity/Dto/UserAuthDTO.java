package com.example.updatedsecurity.Dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserAuthDTO {
    public static final String EMAIL_ALIAS = "u_email";
    public static final String NAME_ALIAS = "u_name";
    public static final String ROLE_ALIAS = "u_role";


    private String email;
    private String name;
    private String role;
    private List<PermissionDTO> permissions = new ArrayList<>();

    public UserAuthDTO(
            Object[] tuples,
            Map<String, Integer> aliasToIndexMap
    ) {
        this.email = String.valueOf(tuples[aliasToIndexMap.get(EMAIL_ALIAS)]);
        this.name = String.valueOf(tuples[aliasToIndexMap.get(NAME_ALIAS)]);
        this.role = String.valueOf(tuples[aliasToIndexMap.get(ROLE_ALIAS)]);
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<PermissionDTO> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<PermissionDTO> permissions) {
        this.permissions = permissions;
    }
}

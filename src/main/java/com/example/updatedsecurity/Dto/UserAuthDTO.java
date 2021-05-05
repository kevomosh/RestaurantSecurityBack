package com.example.updatedsecurity.Dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserAuthDTO {
    public static final String EMAIL_ALIAS = "u_email";
    public static final String NAME_ALIAS = "u_name";

    private String email;
    private String name;
    private List<GroupDTO> groups = new ArrayList<>();

    public UserAuthDTO(
            Object[] tuples,
            Map<String, Integer> aliasToIndexMap
    ) {
        this.email = String.valueOf(tuples[aliasToIndexMap.get(EMAIL_ALIAS)]);
        this.name = String.valueOf(tuples[aliasToIndexMap.get(NAME_ALIAS)]);

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

    public List<GroupDTO> getGroups() {
        return groups;
    }

    public void setGroups(List<GroupDTO> groups) {
        this.groups = groups;
    }
}

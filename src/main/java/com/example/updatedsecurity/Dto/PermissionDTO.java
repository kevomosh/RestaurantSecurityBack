package com.example.updatedsecurity.Dto;

import java.util.Map;

public class PermissionDTO {
    public static final String CODE_ALIAS = "p_code";

    private String code;

    public PermissionDTO(
            Object[] tuples,
            Map<String, Integer> aliasToIndexMap){
        this.code = String.valueOf(tuples[aliasToIndexMap.get(CODE_ALIAS)]).toUpperCase();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}

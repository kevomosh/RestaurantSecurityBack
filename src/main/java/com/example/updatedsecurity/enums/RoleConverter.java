package com.example.updatedsecurity.enums;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class RoleConverter implements AttributeConverter<Role, String> {
    @Override
    public String convertToDatabaseColumn(Role role) {
        return role.getShortRole();
    }

    @Override
    public Role convertToEntityAttribute(String s) {
        return Role.fromShortRole(s);
    }
}

package com.example.updatedsecurity.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true ,nullable = false)
    private String code;

    @ManyToMany(mappedBy = "userPermissions")
    @JsonIgnore
    private Set<User> users = new HashSet<>();

    public Permission(String code) {
        this.code = code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Permission that = (Permission) o;

        if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) return false;
        return getCode().equals(that.getCode());
    }

    @Override
    public int hashCode() {
        return getCode().hashCode();
    }
}

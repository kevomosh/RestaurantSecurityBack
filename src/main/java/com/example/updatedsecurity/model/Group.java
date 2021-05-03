package com.example.updatedsecurity.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "principle_groups")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true, nullable = false)
    private String code;

    private String name;

    @ManyToMany(mappedBy = "userGroups")
    @JsonIgnore
    private Set<User> users = new HashSet<>();

    public Group(String code, String name) {
        this.code = code;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Group group = (Group) o;

        if (!getId().equals(group.getId())) return false;
        return getCode().equals(group.getCode());
    }

    @Override
    public int hashCode() {
        int result = getId().hashCode();
        result = 31 * result + getCode().hashCode();
        return result;
    }
}


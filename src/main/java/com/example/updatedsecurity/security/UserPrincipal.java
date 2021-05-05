package com.example.updatedsecurity.security;

import com.example.updatedsecurity.Dto.PermissionDTO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserPrincipal implements UserDetails {
    private String id;
    private String email;
    private String password;
    private String name;
    private String role;
    private List<PermissionDTO> permissionDTOList;

    public UserPrincipal(String id, String email,String name,  String password,
                         String role,
                         List<PermissionDTO> permissionDTOList) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.password = password;
        this.role = role;
        this.permissionDTOList = permissionDTOList;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>(permissionDTOList.size());
        if (role.equals("A")) {
            authorities.add(new SimpleGrantedAuthority("ADMIN"));
        }
         else {
            for(PermissionDTO userPermissionDTO : permissionDTOList){
                authorities.add(new SimpleGrantedAuthority(role + "_"  + userPermissionDTO.getCode()));
            }
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return name;
    }

    public String getRole() {return role; }

    public String getEmail(){
        return email;
    }

    public String getId(){
        return id;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

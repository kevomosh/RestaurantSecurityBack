package com.example.updatedsecurity.security;

import com.example.updatedsecurity.Dto.GroupDTO;
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
    private List<GroupDTO> groupDTOList;

    public UserPrincipal(String id, String email,String name,  String password,
                         List<GroupDTO> groupDTOList) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.password = password;
        this.groupDTOList = groupDTOList;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>(groupDTOList.size());

        for(GroupDTO userGroupDTO: groupDTOList){
            authorities.add(new SimpleGrantedAuthority(userGroupDTO.getCode()));
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

package com.example.updatedsecurity.security;

import com.example.updatedsecurity.Dto.GroupDTO;
import com.example.updatedsecurity.repositories.UserRepository;
import com.example.updatedsecurity.services.HelperService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserPrincipalDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    private final HelperService helperService;

    public UserPrincipalDetailsService(UserRepository userRepository,
                                       HelperService helperService) {
        this.userRepository = userRepository;
        this.helperService = helperService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var user = userRepository.loadUserByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("user not found"));

        List<GroupDTO> groupDTOList = new ArrayList<>(0);

        return new UserPrincipal(user.getId().toString(), user.getEmail(),
                user.getName(), user.getPassword(), groupDTOList);
    }

    public UserDetails loadUserById(String idStr) throws UsernameNotFoundException {
        var userList = helperService.authDetailsById(idStr);
        if (userList.isEmpty()){
            throw  new UsernameNotFoundException("User not found");
        }
        var user = userList.get(0);
        return new UserPrincipal("", "", user.getName(), "", user.getGroups());
    }
}

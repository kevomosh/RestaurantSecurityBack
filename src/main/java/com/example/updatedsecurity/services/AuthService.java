package com.example.updatedsecurity.services;

import com.example.updatedsecurity.enums.Role;
import com.example.updatedsecurity.inpDTO.LogInInp;
import com.example.updatedsecurity.inpDTO.RegisterInp;
import com.example.updatedsecurity.model.Group;
import com.example.updatedsecurity.model.User;
import com.example.updatedsecurity.repositories.GroupRepository;
import com.example.updatedsecurity.repositories.UserRepository;
import com.example.updatedsecurity.security.JWTUtility;
import com.example.updatedsecurity.security.UserPrincipal;
import com.example.updatedsecurity.security.UserPrincipalDetailsService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {
    private UserRepository userRepository;
    private AuthenticationManager authenticationManager;
    private PasswordEncoder encoder;
    private JWTUtility jwtUtility;
    private UserPrincipalDetailsService userPrincipalDetailsService;
    private GroupRepository groupRepository;

    public AuthService(UserRepository userRepository,
                       AuthenticationManager authenticationManager,
                       PasswordEncoder encoder, JWTUtility jwtUtility,
                       UserPrincipalDetailsService userPrincipalDetailsService,
                       GroupRepository groupRepository) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.encoder = encoder;
        this.jwtUtility = jwtUtility;
        this.userPrincipalDetailsService = userPrincipalDetailsService;
        this.groupRepository = groupRepository;
    }

    public String register(RegisterInp registerInp) {
        var newUser = new User(registerInp.getName(), registerInp.getEmail(),
                encoder.encode(registerInp.getPassword()));
//
//        var role = registerInp.getRole();
//        if (StringUtils.hasLength(role) && role.equals("sonko")) {
//            newUser.setRole(Role.ADMIN);
//        } else {
//            newUser.setRole(Role.USER);
//        }
        userRepository.save(newUser);
        return "registered";
    }

    public String LogIn(LogInInp logInInp) throws Exception {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            logInInp.getName(),
                            logInInp.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            final UserPrincipal userDetails = (UserPrincipal) authentication.getPrincipal();

            return jwtUtility.generateToken(userDetails);


        } catch (BadCredentialsException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid credentials");
        }
    }

    public Group addGroup(String code, String name) {
        var group = new Group(code, name);
        groupRepository.save(group);

//        var user = userRepository.findUserByName(username).orElseThrow(
//                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "user not there")
//        );
//
//        user.addUserGroups(group);
        return group;
    }

    public String addGroupToUser(String userName, String codeName){
        var group = groupRepository.findGroupByCode(codeName).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "not")
        );
        var user = userRepository.findUserByName(userName).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "user not there")
        );

        user.addUserGroups(group);
        userRepository.save(user);
        return "done";
    }

    public User getUserByName(String name) {
        return userRepository.getUserByName(name).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "not there")
        );
    }
}

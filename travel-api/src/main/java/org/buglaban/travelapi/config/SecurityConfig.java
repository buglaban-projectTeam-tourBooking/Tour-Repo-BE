package org.buglaban.travelapi.config;


import org.buglaban.travelapi.model.Role;
import org.buglaban.travelapi.model.User;
import org.buglaban.travelapi.repository.IRoleRepository;
import org.buglaban.travelapi.repository.IUserRepository;
import org.buglaban.travelapi.util.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;

@Configuration
public class SecurityConfig {
    @Autowired
    private IUserRepository iUserRepository;
    @Autowired
    private IRoleRepository iRoleRepository;

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService(iUserRepository));
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config
    ) throws Exception {
        return config.getAuthenticationManager();
    }
    //user's detail object
    @Bean
    public UserDetailsService userDetailsService(IUserRepository userRepository) {
        return username -> {
            User user = iUserRepository.findByEmail(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            // Load Role eager ngay trong transaction
            String roleName = user.getRole().getRoleName().name();   // vẫn lazy → vẫn lỗi nếu không fetch trước

            // Cách fix: Sử dụng JOIN FETCH trong Repository
            return new org.springframework.security.core.userdetails.User(
                    user.getEmail(),
                    user.getPasswordHash(),
                    AuthorityUtils.createAuthorityList("ROLE_" + roleName)
            );
        };
    }
}

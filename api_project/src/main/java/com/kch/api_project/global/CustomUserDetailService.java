package com.kch.api_project.global;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.kch.api_project.entity.Users;
import com.kch.api_project.repository.AuthRepository;

@Service
public class CustomUserDetailService implements UserDetailsService {

    private final AuthRepository authRepository;

    public CustomUserDetailService(AuthRepository authRepository) { this.authRepository = authRepository; }

    // DB에서 사용자 정보 조회 후 Spring Security의 UserDetails 객체로 반환
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = authRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .build();
    }
}

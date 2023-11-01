package com.zerobase.dividends.service;

import com.zerobase.dividends.model.AuthDto;
import com.zerobase.dividends.persist.MemberRepository;
import com.zerobase.dividends.persist.entity.MemberEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.memberRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("couldn't find user -> " + username));
    }

    public MemberEntity register(AuthDto.SignUp signUp) {
        boolean exist = this.memberRepository.existsByUsername(signUp.getUsername());
        if (exist) {
            throw new RuntimeException("이미 사용 중인 아이디 입니다.");
        }

        signUp.setPassword(this.passwordEncoder.encode(signUp.getPassword()));

        return this.memberRepository.save(signUp.toEntity());
    }

    public MemberEntity authenticate(AuthDto.SignIn signIn) {
        return null;
    }
}

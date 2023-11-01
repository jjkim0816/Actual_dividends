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

    /**
     * 로그인 시 로그인 정보 검증 메서드
     * @param signIn 요청 로그인 데이터
     * @return MemberEntity
     */
    public MemberEntity authenticate(AuthDto.SignIn signIn) {
        MemberEntity user = this.memberRepository.findByUsername(signIn.getUsername())
            .orElseThrow(() -> new RuntimeException("존재하지 않은 유저 입니다."));

        if (this.passwordEncoder.matches(signIn.getPassword(), user.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        return user;
    }
}

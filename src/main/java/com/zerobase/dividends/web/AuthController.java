package com.zerobase.dividends.web;

import com.zerobase.dividends.model.AuthDto;
import com.zerobase.dividends.persist.entity.MemberEntity;
import com.zerobase.dividends.security.TokenProvider;
import com.zerobase.dividends.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/auth")
@RequiredArgsConstructor
public class AuthController {
    private final MemberService memberService;
    private final TokenProvider tokenProvider;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(
        @RequestBody AuthDto.SignUp request
    ) {
        return ResponseEntity.ok(this.memberService.register(request));
    }

    @PostMapping("/signin")
    public ResponseEntity<?> singin(
        @RequestBody AuthDto.SignIn request
    ) {
        MemberEntity member = this.memberService.authenticate(request);
        String token = this.tokenProvider.generateToken(
            member.getUsername(), member.getRoles());
        return ResponseEntity.ok(token);
    }
}

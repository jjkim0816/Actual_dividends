package com.zerobase.dividends.model;

import com.zerobase.dividends.persist.entity.MemberEntity;
import lombok.*;

import java.util.List;

public class AuthDto {
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SignIn {
        private String username;
        private String password;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SignUp {
        private String username;
        private String password;
        private List<String> roles;

        public MemberEntity toEntity() {
            return MemberEntity.builder()
                .username(this.getUsername())
                .password(this.getPassword())
                .roles(this.getRoles())
                .build();
        }
    }
}

package com.newzy.backend.global.auth;

import com.newzy.backend.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;

@AllArgsConstructor
@Getter
public class CustomUserDetails implements UserDetails {
    private final User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList(); // 권한 설정, 필요시 커스텀 권한 설정 가능
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
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
    public boolean isEnabled() { return true; }

    // 추가적인 사용자 정보 접근자 메서드
    public String getNickname() {
        return user.getNickname();
    }

    public LocalDate getBirth() {
        return user.getBirth();
    }

    public String getInfo() {
        return user.getInfo();
    }

    public int getExp() {
        return user.getExp();
    }

    public int getEconomyScore() {
        return user.getEconomyScore();
    }

    public int getSocietyScore() {
        return user.getSocietyScore();
    }

    public int getInternationalScore() {
        return user.getInternationalScore();
    }

    public int getState() {
        return user.getState();
    }
}

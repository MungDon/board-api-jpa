package com.example.boardapi.user;

import com.example.boardapi.entity.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

// record 는 불변 객체이다
// record 를 사용하면 getter, toString(), equals(), hashCode() 메서드를 자동으로 생성 이로인해 보일러플레이트(쓸때없이 반복되는) 코드를 줄일 수 있음
// record 를 사용하면 불변 객체를 만들 때 유용, 유지보수 ↑, 가독성 ↑
public record CustomUserDetail(Member member) implements UserDetails {

    // 사용자의 권한을 Collection<GrantedAuthority> 형태로 반환
    // 시큐리티에 권한을 등록하기 위함
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return member.getRole();
            }
        });
        return collection;
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {      // 계정 만료 여부를 항상 만료 되지않음을 명시(true)
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {       // 계정 잠금 여부를 항상 잠금 되지않음을 명시(true)
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {  // 자격 증명 만료 여부를 항상 만료 되지않음을 명시(true)
        return true;
    }

    @Override
    public boolean isEnabled() {                // 계정 활성화 여부를 항상 활성화 상태로 명시(true)
        return true;
    }
}

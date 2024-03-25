package SublindWay_server;

import SublindWay_server.Domain.User;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

// UserDetailsImpl은 이러한 인증과 권한 부여 과정에서 사용자의 정보를 나타냅니다.
// UserDetailsImpl 클래스는 주로 사용자의 인증 정보를 제공합니다.
// 이 클래스는 사용자의 식별자(username 또는 email), 비밀번호(password),
// 그리고 해당 사용자에게 부여된 권한(authorities) 등을 포함합니다.
// 또한, 필요에 따라 사용자의 계정의 만료 여부, 비밀번호의 만료 여부, 계정의 잠금 여부 등을 포함할 수 있습니다.

//스프링 시큐리티에서 사용자를 정의하는 모델
//스프링 시큐리티가 이해하는 방식으로 사용자를 나타내기 위한 모델
//하나 이상의 권한(GrantedAuthority)을 가진다
public class UserDetailsImpl implements UserDetails, OAuth2User {
    private User user;
    // 카카오에서 조회한 사용자 정보를 담을 컬렉션
    private Map<String, Object> attributes;

    public UserDetailsImpl(User user){
        this.user=user;
    }

    public UserDetailsImpl(User user, Map<String, Object> attributes){
        this.user=user;
        this.attributes=attributes;
    }

    // OAuth2 Client 필수 메서드 재정의
    public Map<String, Object> getAttributes(){
        return this.attributes;
    }
    @Override
    public String getName() {
        return null;
    }


    // Spring Security 필수 메서드 재정의
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return user.getUser_pwd();
    }

    @Override
    public String getUsername() {
        return user.getUser_name();
    }

    @Override // isAccountNonExpired() : 계정 만료 여부 => true : 만료 X
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override // isAccountNonLocked() : 계정 잠김 여부 => true : 잠김 X
    public boolean isAccountNonLocked() {
        return true;
    }

    // 비밀번호가 만료되지 않았는지 반환
    @Override // isCredentialsNonExpired() : 비밀번호 만료 여부 => true : 만료 X
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 계정이 활성화되었는지 반환
    @Override // isEnabled() : 계정 사용 가능 여부 => true : 사용 가능 O
    public boolean isEnabled() {
        return true;
    }
}

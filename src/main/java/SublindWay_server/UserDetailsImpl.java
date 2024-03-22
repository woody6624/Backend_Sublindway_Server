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
public class UserDetailsImpl implements UserDetails, OAuth2User {
    private User user;
    private Map<String, Object> attributes;

    public UserDetailsImpl(User user){
        this.user=user;
    }

    public UserDetailsImpl(User user, Map<String, Object> attributes){
        this.user=user;
        this.attributes=attributes;
    }

    public Map<String, Object> getAttributes(){
        return this.attributes;
    }

    @Override
    public String getName() {
        return null;
    }

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

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    // 비밀번호가 만료되지 않았는지 반환
    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    // 계정이 활성화되었는지 반환
    @Override
    public boolean isEnabled() {
        return false;
    }
}

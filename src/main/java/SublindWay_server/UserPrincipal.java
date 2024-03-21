package SublindWay_server;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import SublindWay_server.Domain.User;
import org.springframework.lang.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class UserPrincipal implements OAuth2User, UserDetails {

    private User user;
    private List<GrantedAuthority> authorities; // 인가
    private Map<String, Object> oauthUserAttributes;

    private UserPrincipal(User user, List<GrantedAuthority> authorities,
                          Map<String, Object> oauthUserAttributes) {
        this.user = user;
        this.authorities = authorities;
        this.oauthUserAttributes = oauthUserAttributes;
    }

    /**
     * OAuth2 로그인시 사용
     */
    public static UserPrincipal create(User user, Map<String, Object> oauthUserAttributes) {
        return new UserPrincipal(user, List.of(() -> "ROLE_USER"), oauthUserAttributes);
    }

    public static UserPrincipal create(User user) {
        return new UserPrincipal(user, List.of(() -> "ROLE_USER"), new HashMap<>());
    }

    @Override
    public String getPassword() {
        return user.getPwd();
    }

    @Override
    public String getUsername() {
        return String.valueOf(user.getEmail());
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
    public boolean isEnabled() {
        return true;
    }

    //우리 서버에서 카카오 access token 을 얻은 다음
    //이 access token 으로 구글한테 사용자 정보를 알려달라고 요청하면
    //구글에서 응답을 하는데
    //이 때 사용자 정보가 attributes 안에 담아져서 온다.
    //attribues가 Map 형식이니까 { "email" : "yelim@gmail.com" } 이런식으로!

    @Override
    public Map<String, Object> getAttributes() {
        return Collections.unmodifiableMap(oauthUserAttributes);
    }

    @Override
    @Nullable
    @SuppressWarnings("unchecked")
    public <A> A getAttribute(String name) {
        return (A) oauthUserAttributes.get(name);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.unmodifiableList(authorities);
    }

    @Override
    public String getName() {
        return String.valueOf(user.getEmail());
    }
}
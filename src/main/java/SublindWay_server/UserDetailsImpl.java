package SublindWay_server;

import SublindWay_server.Domain.User;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;

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
}

package SublindWay_server.Service;

import SublindWay_server.Domain.User;
import SublindWay_server.Dao.UserRepository;
import SublindWay_server.Dto.KakaoMemberInfo;
import SublindWay_server.Dto.OAuth2MemberInfo;
import SublindWay_server.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;


import java.util.Optional;
import java.util.jar.Attributes;


// access token 얻은 후 실행
// access token과 같은 정보들이 oAuth2UserRequest 파라미터에 들어있음
// 유저 정보 DB
@Service
@RequiredArgsConstructor
public class OAuth2UserService extends DefaultOAuth2UserService {
    private final BCryptPasswordEncoder encoder;
    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        OAuth2MemberInfo memberInfo = null;
        System.out.println(oAuth2User.getAttributes());

        // kakao member DTO 생성
        memberInfo = new KakaoMemberInfo(oAuth2User.getAttributes());

        String oauth2Id = memberInfo.getProviderId();
        String username = memberInfo.getName();
        String email = memberInfo.getEmail();
        String role = "ROLE_USER"; //일반 유저

        System.out.println(oAuth2User.getAttributes());

        // DAO에서 검색
        Optional<User> findMember = userRepository.findByOauth2Id(oauth2Id);
        User user = null;
        if (findMember.isEmpty()) { // 찾지 못했다면 (기존회원x)
            user = User.builder()
                    .user_id(oauth2Id)
                    .user_pwd(encoder.encode("password"))
                    .user_name(username)
                    .user_email(email)
                    .user_role(role)
                    .build();
            userRepository.save(user);
        }

        else{ // 찾음 (기존회원o)
            user=findMember.get();
        }

        return new UserDetailsImpl(user, oAuth2User.getAttributes());
    }
}

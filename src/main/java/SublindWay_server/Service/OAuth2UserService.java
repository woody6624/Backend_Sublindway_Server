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

// 사용자 이름으로 검색하는 역할
// 인증을 완료하는데 있어서 반드시 필요한 유일한 작업
// 재정의 후 Bean 등록

// 실제로 인증을 위한 데이터를 가져오는 역할
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

        // 카카오가 전달해준 정보를 바탕으로 회원 정보를 구성한다.
        String oauth2Id = memberInfo.getProviderId();
        String username = memberInfo.getName();
        String email = memberInfo.getEmail();
        String role = "ROLE_USER"; //일반 유저

        System.out.println(oAuth2User.getAttributes());

        // 회원가입이 되어있는 사용자인지 확인한다.
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

        // OAuth2 Client가 UserDetailsImpl에 설정된 정보로
        // Authentication 객체를 SecurityContext에 자동으로 등록한다.
        return new UserDetailsImpl(user, oAuth2User.getAttributes());
    }
}

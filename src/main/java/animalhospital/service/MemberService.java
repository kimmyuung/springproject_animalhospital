package animalhospital.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;

@Service
public class MemberService implements OAuth2UserService<OAuth2UserRequest ,OAuth2User> {
    // UserDetailsService 인터페이스 [ 추상메소드 존재~~ ] : 일반 회원
    // -----> loadUserByUsername 메소드 구현
    // OAuth2UserService<OAuth2UserRequest , OAuth2User> : Oauth2 회원
    // ------> loadUser 메소드 구현

    //  *  oauth2 서비스 제공 메소드
    // OAuth2UserRequest : 인증 결과를 호출 클래스


    @Autowired
    HttpServletRequest request; // 세션 사용을 위한 request 객체 선언


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        return null;
    }
}

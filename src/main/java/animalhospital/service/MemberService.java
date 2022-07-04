package animalhospital.service;

import animalhospital.domain.member.MemberEntity;
import animalhospital.domain.member.MemberRepository;
import animalhospital.domain.member.Role;
import animalhospital.dto.OauthDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;


import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Service
public class MemberService implements OAuth2UserService<OAuth2UserRequest ,OAuth2User> {
    // UserDetailsService 인터페이스 [ 추상메소드 존재~~ ] : 일반 회원
    // -----> loadUserByUsername 메소드 구현
    // OAuth2UserService<OAuth2UserRequest , OAuth2User> : Oauth2 회원
    // ------> loadUser 메소드 구현

    //  *  oauth2 서비스 제공 메소드
    // OAuth2UserRequest : 인증 결과를 호출 클래스


    @Autowired
    HttpSession httpSession;

    @Autowired
    MemberRepository memberRepository;


    public String authenticationget() {
        Authentication authentication // 인가된 객체를 불러옴 시큐리티컨택스트 홀더안에 컨택스트안의 인가 호출
                = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal(); // 인가된 클래스안의 객체 호출
        String mid = null;
        if(principal != null) {
            // 인증(로그인)이 되어 있는 상태
           if(principal instanceof OAuth2User){ // 소셜회원이라면
                Map<String, Object> attributes = ((OAuth2User) principal).getAttributes();
                // 객체속 회원정보가 저장된 속성들의 값 호출
                if(attributes.get("response") != null) { // 네이버 라면
                    Map<String, Object> map = (Map<String, Object>) attributes.get("response");
                    // 네이버는 회원정보가 저장된 객체를 respone으로 설정했기 때문
                    mid = map.get("email").toString();

                } else { // 카카오라면
                    Map<String, Object> map = (Map<String, Object>) attributes.get("kakao_account");
                    // 카카오는  kakao_account로 객체 이름을 설정
                    mid = map.get("email").toString();
                }
            }
            return mid;
        }else {
            // 인증 (로그인)이 안되어 있는 상태
            return null;
        }
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 인증[로그인] 결과 정보 요청
        OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = oAuth2UserService.loadUser( userRequest );

        // 클라이언트 아이디 [ 네이버 vs 카카오 vs 구글 ] : oauth 구분용 으로 사용할 변수
        String registrationId = userRequest.getClientRegistration().getRegistrationId();


        // 회원정보 요청시 사용되는 JSON 키 이름 호출  : 회원정보 호출시 사용되는 키 이름
        String userNameAttributeName = userRequest
                .getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        // 확인
        System.out.println(  " registrationId   " + registrationId   );
        System.out.println(  "userNameAttributeName key name :   " + userNameAttributeName   );
        System.out.println(  "login result  : " + oAuth2User.getAttributes() );

        // oauth2 정보 -> Dto -> entitiy -> db저장
        OauthDto oauthDto = OauthDto.of(  registrationId ,  userNameAttributeName  ,  oAuth2User.getAttributes()  );


        Optional<MemberEntity> optionalMemberEntity
                = memberRepository.findBymemail(oauthDto.getMemail());
        if(! optionalMemberEntity.isPresent()) {
            memberRepository.save(oauthDto.toentity());
        }

        httpSession.setAttribute("login", oauthDto);

        // 반환타입 DefaultOAuth2User ( 권한(role)명 , 회원인증정보 , 회원정보 호출키 )
        // DefaultOAuth2User , UserDetails : 반환시 인증세션 자동 부여 [ SimpleGrantedAuthority : (권한) 필수~  ]
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_MEMBER")),
                oAuth2User.getAttributes() ,
                userNameAttributeName
        );
    }

    public boolean delete(OauthDto oauthDto) {
        Optional<MemberEntity> optional = memberRepository.findBymid(oauthDto.getMid());
        if(optional.isPresent()) {
            MemberEntity memberEntity = optional.get();
            memberRepository.delete(memberEntity);
            return true;
        }
        return false;
    }

    public boolean adminlogin(String mid, String mpassword) {
        if (mid.equals("admin") && mpassword.equals("asdqwe123")) {
           MemberEntity memberEntity = MemberEntity.builder()
                           .mid(mid)
                                   .role(Role.ADMIN)
                                           .build();
            httpSession.setAttribute("admin", memberEntity);
            return true;
        }
        return false;
    }

}
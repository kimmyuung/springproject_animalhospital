package animalhospital.service;

import animalhospital.domain.CountEntity;
import animalhospital.domain.CountingRepository;
import animalhospital.domain.member.*;
import animalhospital.domain.message.MessageEntity;
import animalhospital.domain.message.MessageRepository;
import animalhospital.dto.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class MemberService implements OAuth2UserService<OAuth2UserRequest ,OAuth2User>, UserDetailsService {
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

    @Autowired
    CountingRepository countingRepository;




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
        httpSession.setAttribute("date", LocalDate.now());
        LocalDate nowdate  = (LocalDate) httpSession.getAttribute("date");
        String date = nowdate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        System.out.println("dddd"+date);
        OauthDto csession = (OauthDto) httpSession.getAttribute("login");




       if(httpSession.getAttribute(csession.getMemail()+date)==null){
           CountDto cdto = CountDto.builder()
                   .cnum(csession.getMemail()+date)
                   .count(1)
                   .createdate(LocalDate.now())
                   .build();

           // 엔티티를 이용한 조회수 증가

           // 방문자수 중복방지 [ 세션 생성 ]
           httpSession.setAttribute(csession.getMemail()+date,true);
           System.out.println("중복방지"+httpSession.getAttribute(csession.getMemail()+date));
           httpSession.setMaxInactiveInterval(60*60*24);
           countingRepository.save(cdto.toentity());
       }


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

    /* 관리자 로그인을 userDetailService에서 처리
    public boolean adminlogin(String mid, String mpassword) {
        if (mid.equals("admin") && mpassword.equals("1234")) {
           MemberEntity memberEntity = MemberEntity.builder()
                           .mid(mid)
                   .mname("관리자")
                   .oauth("admin")
                   .mpassword(mpassword)
                   .memail("admin@gmail.com")
                                   .role(Role.ADMIN)
                                           .build();
            httpSession.setAttribute("admin", memberEntity);
            Optional<MemberEntity> optional = memberRepository.findBymid(mid);
            if(! optional.isPresent()) {
                memberRepository.save(memberEntity);
            }
            return  true;
        }
        return false;
    }*/


    // 1. 로그인 서비스 제공 메소드
    // 2. 패스워드 검증 X [ 시큐리티 제공 ]
    // 3. 아이디만 검증 처리
    @Override
    public UserDetails loadUserByUsername(String mid) throws UsernameNotFoundException {
            // 관리자 아이디는 미리 DB에 등록해 놓음
        // 1. 회원 아이디를 찾기

        Optional<MemberEntity> entitiy =  memberRepository.findBymid(mid); // 회원 아이디로 엔티티 찾기

        MemberEntity memberEntity = entitiy.orElse(null);
        // optional.isPresent() : null이 아니면
        // optional.orElse() : 만약에 optional 객체가 비어있으면 반환할 데이터

        // 2. 찾은 회원 엔티티의 권한[키]을 리스트에 담기
        List<GrantedAuthority> authorityList = new ArrayList<>(); // 부여된 인증들을 모아두기
        // GrantedAuthority : 부여된 인증의 클래스

        authorityList.add(new SimpleGrantedAuthority(memberEntity.getrolekey()));
        // 리스트에 인증된 엔티티의 키를 보관

        return new LoginDto(memberEntity, authorityList); // 회원엔티티, 인증된 리스트를 인증세션 부여
    }

    @Autowired
    RequestRepository requestRepository;
    public boolean requestsave(RequestDto requestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        String mid = null;
        if( principal instanceof UserDetails){
            mid = ((UserDetails) principal).getUsername();
        }else if( principal instanceof DefaultOAuth2User){
            Map<String , Object>  map =  ((DefaultOAuth2User) principal).getAttributes();
            if( map.get("response") != null ){
                Map< String , Object> map2  = (Map<String, Object>) map.get("response");
                mid = map2.get("email").toString().split("@")[0];
            }else{
                Map< String , Object> map2  = (Map<String, Object>) map.get("kakao_account");
                mid = map2.get("email").toString().split("@")[0];
            }
        }else{
            return false;
        }
        if( mid != null  ) {
            Optional<MemberEntity> optionalMember = memberRepository.findBymid(mid);
            if (optionalMember.isPresent()) {
                int mno = optionalMember.get().getMno();
                RequestEntity requestEntity = requestDto.toentity();
                requestEntity.setMid(mid);
                requestEntity.setMno(mno);
                requestEntity.setHospital(requestEntity.getHname()+requestEntity.getHdate());
                String uuidfile = null;
                UUID uuid = UUID.randomUUID();
                MultipartFile file = requestDto.getBinimg();
                uuidfile = uuid.toString() + "_" + file.getOriginalFilename().replaceAll("_", "-");
                String dir = "C:\\Users\\504\\Desktop\\springproject_animalhospital\\src\\main\\resources\\static\\upload\\";
                String filepath = dir + uuidfile;
                try {

                    file.transferTo(new File(filepath));
                    requestEntity.setBinimg(uuidfile);
                    requestRepository.save(requestEntity);

                } catch (IOException e) {
                    System.out.println("requestsave error : " + e);
                }

            }else {
                return false;
            }
        }
        return false;
    }

    public JSONArray getbinlist() {
        JSONArray jsonArray = new JSONArray();
        List<RequestEntity> entities = requestRepository.findAll();
        System.out.println(entities);
        for (RequestEntity entity : entities ){
            JSONObject object = new JSONObject();
            object.put("hno", entity.getHno());
            object.put("hname", entity.getHname());
            object.put("hdate", entity.getHdate());
            object.put("mid", entity.getMid());
            object.put("mno", entity.getMno());
            object.put("binimg", entity.getBinimg());
            jsonArray.put(object);
        }
        return jsonArray;
    }

    @Transactional
    public boolean setrole(int mno, String hname, String hdate, String bin) {
        System.out.println(mno + hname + hdate + bin);
        if(bin !=null){
            String hospital = "HOSPITAL";
            MemberEntity memberEntity = memberRepository.findBymno(mno);
            memberEntity.setRole(Role.HOSPITAL);
            memberRepository.save(memberEntity);
            System.out.println(memberEntity.getRole().getKey());
            RequestEntity requestEntity = requestRepository.findBymno(mno);
            requestEntity.setBin(bin);
            requestEntity.setActive(true);
            requestRepository.save(requestEntity);
            return  true;
        }else {
            return false;
        }
    }

    //쪽지
    //메시지 전송
    @Autowired
    private MessageRepository messageRepository;

    @Transactional
    public boolean messagesend(JSONObject object){
        String from = (String) object.get("from");
        String to = (String) object.get("to");
        String msg = (String) object.get("msg");

        MemberEntity fromentity = null;
        Optional<MemberEntity> optionalMember1 = memberRepository.findBymid(from);
        if(optionalMember1.isPresent()){
            fromentity = optionalMember1.get();
        }else {
            return false;
        }
        MemberEntity toentity = null;
        Optional<MemberEntity> optionalMember2 = memberRepository.findBymid(to);
        if(optionalMember2.isPresent()){
            toentity =optionalMember2.get();
        }else {
            return false;
        }

        MessageEntity messageEntity = MessageEntity.builder()
                .msg(msg)
                .fromentity(fromentity)
                .toentity(toentity)
                .build();

        messageRepository.save(messageEntity);

        fromentity.getFromentitylist().add(messageEntity);
        toentity.getToentitylist().add(messageEntity);
        return true;

    }
    public JSONArray todaycount(){
        JSONArray jsonArray = new JSONArray();
        JSONArray child =   new JSONArray();
        List<CountEntity> list = countingRepository.getcount();
        System.out.println(list.toString());
        JSONObject object2 = new JSONObject();
        object2.put("date","1");
        String od = object2.get("date").toString();
        for(CountEntity dto : list) {
            JSONObject object = new JSONObject();
            object.put("date",dto.getCreatedate());
            object.put("count",dto.getCount());
            if((object.get("date").toString().equals(od))) {
                child.put(object);

            } else {
                child = new JSONArray();
                child.put(object);
                jsonArray.put(child);
            }
            //jsonArray.put(object);
            od = object.get("date").toString();

        }
        //System.out.println("ddd"+ jsonArray);
        return jsonArray;
    }



}

package animalhospital.service;

import animalhospital.domain.ReviewEntity;
import animalhospital.domain.ReviewRepository;
import animalhospital.domain.board.BoardEntity;
import animalhospital.domain.board.BoardimgEntity;
import animalhospital.domain.member.MemberEntity;
import animalhospital.domain.member.MemberRepository;
import animalhospital.dto.ReviewDto;
import org.apache.tomcat.util.json.JSONParser;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class MapService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private MemberRepository memberRepository;
    
    public JSONArray map() {
        JSONArray animalhospital =  new JSONArray();
        try {
            JSONArray hospital = null;
            for (int i = 1; i < 4; i++) {

                //공공데이터 포털
                //1. url 가져오기 [URL : java.net패키지]
                URL url = new URL("https://openapi.gg.go.kr/Animalhosptl?&pIndex=" + i + "&pSize=1000&Key=47d367a4e715424e8c25f17ff85a81ea&type=json");
                //url 클래스 : java 에서 http url 객체화
                //2. java 해당 url 읽기 [바이트 스트림]
                //BufferedReader : 외부데이터(자바 외) 읽어올 때 사용되는 클래스
                BufferedReader bf;
                bf = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
                //3. 읽어오기
                String result = bf.readLine(); //스트림에 존재하는 바이트 모두 읽어오기
                //4. 읽어온 스트림을 JSON 변환
                JSONObject object = new JSONObject(result);
                JSONArray Animalhosptl = (JSONArray) object.get("Animalhosptl");
                JSONObject row = (JSONObject) Animalhosptl.get(1);
                hospital = (JSONArray) row.get("row");

                for (int j = 0; j < hospital.length(); j++) {
                    JSONObject hospitalobject = new JSONObject();
                    JSONObject hospitalinfo = (JSONObject) hospital.get(j);
                    String open = (String) hospitalinfo.get("BSN_STATE_NM");
                    if (open.equals("정상")) {
                        hospitalobject.put("name", hospitalinfo.get("BIZPLC_NM"));
                        hospitalobject.put("city", hospitalinfo.get("SIGUN_NM"));
                        hospitalobject.put("opendate", hospitalinfo.get("LICENSG_DE"));
                        hospitalobject.put("addr", hospitalinfo.get("REFINE_ROADNM_ADDR"));
                        hospitalobject.put("tel", hospitalinfo.get("LOCPLC_FACLT_TELNO"));
                        hospitalobject.put("lat", hospitalinfo.get("REFINE_WGS84_LAT"));
                        hospitalobject.put("logt", hospitalinfo.get("REFINE_WGS84_LOGT"));
                        animalhospital.put(hospitalobject);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return animalhospital;
    }

    public JSONArray search (String keyword){
        JSONArray hospital = map();
        JSONArray jsonArray = new JSONArray();
        if(keyword != null) {
            for(int i = 0; i < hospital.length(); i++){
                JSONObject object = (JSONObject) hospital.get(i);
                String name = (String) object.get("name");
                if( name.contains(keyword) ){
                    jsonArray.put(object);
                }
            }
        }
//        System.out.println(jsonArray);
        return jsonArray;
    }

    @Transactional
    public boolean addreview(ReviewDto reviewDto){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        String mid = null;
        if( principal instanceof UserDetails){
            mid = ((UserDetails) principal).getUsername();
        }else if( principal instanceof DefaultOAuth2User){
            Map<String , Object> map =  ((DefaultOAuth2User) principal).getAttributes();
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
            if (optionalMember.isPresent()) { // null 아니면
                ReviewEntity reviewEntity = reviewDto.toentity();
                reviewEntity.setMemberEntity( optionalMember.get() );
                    String uuidfile = null;
                    String uuidfile2 = null;
                    if(reviewDto.getRimg1()!=null) {
                        MultipartFile file = reviewDto.getRimg1();
                        UUID uuid = UUID.randomUUID();
                        uuidfile = uuid.toString() + "_" + file.getOriginalFilename().replaceAll("_", "-");
                        String dir = "C:\\Users\\504\\springproject_animalhospital\\src\\main\\resources\\static\\upload\\";
                        String filepath = dir + uuidfile;
                        try {
                            file.transferTo(new File(filepath));
                            reviewEntity.setRimg1(uuidfile);
                        } catch (Exception e) {
                            System.out.println("파일저장실패 : " + e);
                        }
                    }
                if(reviewDto.getRimg2()!=null) {
                    MultipartFile file2 = reviewDto.getRimg2();
                    UUID uuid2 = UUID.randomUUID();
                    uuidfile2 = uuid2.toString() + "_" + file2.getOriginalFilename().replaceAll("_", "-");
                    String dir2 = "C:\\Users\\504\\springproject_animalhospital\\src\\main\\resources\\static\\upload\\";
                    String filepath2 = dir2 + uuidfile2;

                    try {
                        file2.transferTo(new File(filepath2));
                        reviewEntity.setRimg2(uuidfile2);
                    } catch (Exception e) {
                        System.out.println("파일저장실패 : " + e);
                    }
                }
                reviewRepository.save(reviewEntity);
                return true;

            } else { // 로그인이 안되어 있는경우
                return false;
            }

        }
        return false;
    }

}
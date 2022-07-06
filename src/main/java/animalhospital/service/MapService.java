package animalhospital.service;

import animalhospital.domain.ReviewEntity;
import animalhospital.domain.ReviewRepository;
import animalhospital.domain.member.MemberEntity;
import animalhospital.domain.member.MemberRepository;
import animalhospital.dto.OauthDto;
import animalhospital.dto.ReviewDto;
import org.apache.tomcat.util.json.JSONParser;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class MapService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberService memberService;

    public JSONArray map() {
        JSONArray animalhospital =  new JSONArray();
        try {
            JSONArray hospital = null;
            for (int i = 1; i < 4; i++) {

                URL url = new URL("https://openapi.gg.go.kr/Animalhosptl?&pIndex=" + i + "&pSize=1000&Key=47d367a4e715424e8c25f17ff85a81ea&type=json");
                BufferedReader bf;
                bf = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
                String result = bf.readLine();

                JSONObject object = new JSONObject(result);
                JSONArray Animalhosptl = (JSONArray) object.get("Animalhosptl");
                JSONObject row = (JSONObject) Animalhosptl.get(1);
                hospital = (JSONArray) row.get("row");

                for (int j = 0; j < hospital.length(); j++) {
                    JSONObject hospitalobject = new JSONObject();
                    JSONObject hospitalinfo = (JSONObject) hospital.get(j);
                    String open = (String) hospitalinfo.get("BSN_STATE_NM");
                    if (open.equals("정상")) {
                        hospitalobject.put("name", hospitalinfo.get("BIZPLC_NM")); //병원이름
                        hospitalobject.put("city", hospitalinfo.get("SIGUN_NM"));   //시 이름
                        hospitalobject.put("opendate", hospitalinfo.get("LICENSG_DE")); //인허가일자
                        hospitalobject.put("addr", hospitalinfo.get("REFINE_ROADNM_ADDR")); //도로명주소
                        hospitalobject.put("tel", hospitalinfo.get("LOCPLC_FACLT_TELNO"));  //전화번호
                        hospitalobject.put("lat", hospitalinfo.get("REFINE_WGS84_LAT"));    //위도
                        hospitalobject.put("logt", hospitalinfo.get("REFINE_WGS84_LOGT"));  //경도
                        animalhospital.put(hospitalobject);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return animalhospital;
    }
    //병원 검색
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
        String mid = memberService.authenticationget();
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
                    // 배포용도 필요하다...
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
                   // 배포용도 있어야 한다...
                    //  // String dir = "/home/ec2-user/app/springweb2/build/resources/main/static/upload/";
                    //  // "/home/{유저명}/{폴더명}/{git프로젝트명}/build/resources/main/static/";
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

    @Autowired
    private HttpServletRequest request;

    public JSONObject getreviewlist( String hname,String hdate,int page  ){
        OauthDto loginDto = (OauthDto) request.getSession().getAttribute("login");

        JSONObject jo = new JSONObject();
        Pageable pageable = PageRequest.of( page , 3 , Sort.by( Sort.Direction.DESC , "rno")    ); // SQL : limit 와 동일 한 기능처리
        Page<ReviewEntity> reviewEntity =  reviewRepository.findByrlist(hname, hdate,pageable);
        JSONArray jsonArray = new JSONArray();
        ;
        for (ReviewEntity entity : reviewEntity ) {
            String same = null;
            JSONObject object = new JSONObject();
            if(loginDto == null){
                same =  same="false";
            }
            else if(entity.getMemberEntity().getMid().equals(loginDto.getMid())){
                same="true";
            }
            else{ same="false";}
            object.put("rno", entity.getRno());
            object.put("rcontent", entity.getRcontent());
            object.put("rmodifiedate", entity.getModifiedate());
            object.put("rcreatedate", entity.getCreatedate());
            object.put("rkind", entity.getRkind());
            object.put("rfac", entity.getRfac());
            object.put("rprice", entity.getRprice());
            object.put("rimg1", entity.getRimg1());
            object.put("rimg2", entity.getRimg2());
            object.put("same", same );
            object.put("mid", entity.getMemberEntity().getMid());
            jsonArray.put(object);
        }
        int btncount = 5;
        int startbtn  = ( page / btncount ) * btncount + 1;
        int endbtn = startbtn + btncount -1;
        if( endbtn > reviewEntity.getTotalPages() ) endbtn = reviewEntity.getTotalPages();

        if(endbtn > reviewEntity.getTotalPages()) endbtn = reviewEntity.getTotalPages();
        jo.put("startbtn", startbtn);
        jo.put("endbtn", endbtn);
        jo.put("data", jsonArray);
        jo.put("totalpage", reviewEntity.getTotalPages());
        System.out.println(jo);
        return jo;
    }
    @Transactional
    public boolean rdelete( int rno ){
        System.out.println(rno);
        ReviewEntity reviewEntity =  reviewRepository.findById( rno ).get();
        if( reviewEntity != null ){
            // 해당 엔티티를 삭제
            reviewRepository.delete( reviewEntity );
            return true;
        }else{
            return false;
        }
    }

    public JSONObject getreviewstarlist( String hname,String hdate){
        JSONObject jo = new JSONObject();
        List<ReviewEntity> reviewEntity =  reviewRepository.findBylist(hname, hdate);
        JSONArray jsonArray = new JSONArray();
        for (ReviewEntity entity : reviewEntity ) {
            JSONObject object = new JSONObject();
            object.put("rno", entity.getRno());
            object.put("rcontent", entity.getRcontent());
            object.put("rmodifiedate", entity.getModifiedate());
            object.put("rcreatedate", entity.getCreatedate());
            object.put("rkind", entity.getRkind());
            object.put("rfac", entity.getRfac());
            object.put("rprice", entity.getRprice());
            object.put("rimg1", entity.getRimg1());
            object.put("rimg2", entity.getRimg2());
            object.put("mid", entity.getMemberEntity().getMid());
            jsonArray.put(object);
        }
        jo.put("data", jsonArray);
        System.out.println(jo);
        return jo;
    }
    public JSONObject getreview( int rno ){

        Optional<ReviewEntity> optionalReviewEntity =  reviewRepository.findById(rno );
        ReviewEntity reviewEntity =  optionalReviewEntity.get();

        // 2.  해당 엔티티 -> json 객체 변환
        JSONObject object = new JSONObject();
        // 1. json에 엔티티 필드 값 넣기
        object.put("rno" ,reviewEntity.getRno());
        object.put("rcontent" , reviewEntity.getRcontent());
        object.put("mid" , reviewEntity.getMemberEntity().getMid());
        object.put("rimg1" , reviewEntity.getRimg1());
        object.put("rimg2" , reviewEntity.getRimg2());
        object.put("rkind" , reviewEntity.getRkind());
        object.put("rfac" , reviewEntity.getRfac());
        object.put("rprice" , reviewEntity.getRprice());

        return object;
    }

    @Transactional
    public boolean updatereview( ReviewDto reviewDto ){
        Optional<ReviewEntity> optional
                =  reviewRepository.findById( reviewDto.getRno() );
        ReviewEntity reviewEntity =  optional.get();

        String uuidfile = null;
        String uuidfile2 = null;

        if(reviewDto.getRimg1()!=null) {
            MultipartFile file = reviewDto.getRimg1();
            UUID uuid = UUID.randomUUID();
            uuidfile = uuid.toString() + "_" + file.getOriginalFilename().replaceAll("_", "-");
            // String dir = "C:\\Users\\504\\springproject_animalhospital\\src\\main\\resources\\static\\upload\\";
            String dir = "C:\\Users\\82102\\IdeaProjects\\springproject_animalhospital\\src\\main\\resources\\static\\upload\\";
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
            //String dir2 = "C:\\Users\\82102\\IdeaProjects\\springproject_animalhospital\\src\\main\\resources\\static\\upload\\";
            String filepath2 = dir2 + uuidfile2;

            try {
                file2.transferTo(new File(filepath2));
                reviewEntity.setRimg2(uuidfile2);
            } catch (Exception e) {
                System.out.println("파일저장실패 : " + e);
            }
        }
        reviewEntity.setRcontent(reviewDto.getRcontent());
        reviewEntity.setRkind(reviewDto.getRkind());
        reviewEntity.setRfac(reviewDto.getRfac());
        reviewEntity.setRprice(reviewDto.getRprice());

        return true;
    }
}
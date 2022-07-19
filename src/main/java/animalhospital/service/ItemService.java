package animalhospital.service;

import animalhospital.domain.board.BoardEntity;
import animalhospital.domain.board.BoardimgEntity;
import animalhospital.domain.member.MemberEntity;
import animalhospital.domain.member.MemberRepository;
import animalhospital.domain.shop.*;
import animalhospital.dto.ShopDto;
import animalhospital.dto.OauthDto;
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
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class ItemService {
    @Autowired
    ShopRepository shopRepository;

    @Autowired
    ShopImgRepository shopImgRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    HttpServletRequest request;

    @Transactional
    public boolean itemsave(ShopDto shopDto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        String mid = null;

        if( principal instanceof UserDetails){
            mid = ((UserDetails) principal).getUsername();
        }else if( principal instanceof DefaultOAuth2User){
            Map<String , Object> map =  ((DefaultOAuth2User) principal).getAttributes();
            if( map.get("response") != null ){
                Map< String , Object> map2  = (Map<String, Object>) map.get("response"); // 네이버
                mid = map2.get("email").toString().split("@")[0];
            }else if(map.get("kakao_account") != null){
                Map< String , Object> map2  = (Map<String, Object>) map.get("kakao_account"); // 카카오
                mid = map2.get("email").toString().split("@")[0];
            }else if(map.get("kakao_account") == null && map.get("response") == null ) { // 구글, 깃허브
                mid = map.get("email").toString().split("@")[0];
            }
        }else{
            return false;
        }
        if( mid != null  ) {

            Optional<MemberEntity> optionalMember = memberRepository.findBymid(mid);
            if (optionalMember.isPresent()) { // null 아니면
                ShopEntity shopEntity = shopDto.toentity();
                shopEntity.setMember( optionalMember.get() );
                shopRepository.save(shopEntity);
                String uuidfile = null;
                if (shopDto.getSimg().size() != 0) {
                    for (MultipartFile file : shopDto.getSimg()) {
                        UUID uuid = UUID.randomUUID();

                        uuidfile = uuid.toString() + "_" + file.getOriginalFilename().replaceAll("_", "-");
                        String dir = "C:\\Users\\504\\git\\springproject_-animalhospital\\src\\main\\resources\\static\\upload\\";
                      //  String dir = "C:\\Users\\82104\\git\\springproject_-animalhospital\\src\\main\\resources\\static\\upload\\"; // my notebook

                        String filepath = dir + uuidfile;

                        try {
                            file.transferTo(new File(filepath));
                            System.out.println("저장 들어옴");
                            ShopImgEntity shopImgEntity = ShopImgEntity.builder()
                                    .simg(uuidfile)
                                    .shopEntity(shopEntity)
                                    .build();
                            System.out.println(shopImgEntity.toString());

                            shopImgRepository.save(shopImgEntity);

                            shopEntity.getShopimgEntities().add(shopImgEntity);


                        } catch (Exception e) {
                            System.out.println("파일저장실패 : " + e);
                        }
                    }

                }

                return true;

            } else { // 로그인이 안되어 있는경우
                return false;
            }

        }
        return false;
    }

    public Map< String , List<Map<String , String >>> itemlist(int page ) // 인수
    {



        int itemstatus = 0; // 상품 판매 상태가 판매 중인거만 리스트로 출력하기 위해서
        Pageable pageable = PageRequest.of( page , 5 , Sort.by( Sort.Direction.DESC , "sno")    );
        Page<ShopEntity> shopEntities = shopRepository.findByblist(itemstatus , pageable);

        List<  Map<String , String >  > Maplist = new ArrayList<>();



        int btncount = 5;
        int startbtn  = ( page / btncount ) * btncount + 1;
        int endhtn = startbtn + btncount -1;
        if( endhtn > shopEntities.getTotalPages() ) endhtn = shopEntities.getTotalPages();


        for( ShopEntity entity : shopEntities ){
            // 3. map 객체 생성
            Map<String, String> map = new HashMap<>();
            map.put("sno", entity.getSno()+"" );
            map.put("stitle", entity.getStitle());
            map.put("scontent", entity.getScontent());
            map.put("sprice", entity.getPrice()+"");
//            if(entity.getShopimgEntities().get(0).getSimg().equals("")){
//                map.put("simg", "이미지가 없습니다.");
//            } else {
//                map.put("simg", entity.getShopimgEntities().get(0).getSimg());
//            }
            // 경로가 달라 이미지가 저장되지 않음
            map.put( "startbtn" , startbtn+"" );
            map.put("mid", entity.getMember().getMid());
            map.put("bdate",  entity.getCreatedate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            map.put( "endbtn" , endhtn+"" );
            map.put( "totalpages" , shopEntities.getTotalPages()+"" );
            // 4. 리스트 넣기
            Maplist.add(map);
        }
        Map< String , List<  Map<String , String >  > > object = new HashMap<>();

        object.put( "itemlist" , Maplist );

        return  object;
    }

    public JSONObject getitem(int sno ){

        OauthDto loginDto = (OauthDto) request.getSession().getAttribute("login");
        Optional<ShopEntity> optional =  shopRepository.findById(sno );
        ShopEntity shopEntity =  optional.get();
        String same = null;

        if(loginDto == null){
            same = "false";
        }else if(shopEntity.getMember().getMid().equals(loginDto.getMid())){
            same =  "true";
        }else{
            same =  "false";
        }
        // 2.  해당 엔티티 -> json 객체 변환
        JSONObject object = new JSONObject();
        // 1. json에 엔티티 필드 값 넣기
        object.put("bno" ,shopEntity.getSno());
        object.put("btitle" , shopEntity.getStitle());
        object.put("bcontent" , shopEntity.getScontent());
        object.put("mid" , shopEntity.getMember().getMid());
        object.put("same" , same);
        object.put("price" , shopEntity.getPrice());
        object.put("status" , shopEntity.isItemstatus());
        JSONArray jsonArray = new JSONArray();
        for(  ShopImgEntity temp : shopEntity.getShopimgEntities() ) { //  룸별로 이미지 여러개
            jsonArray.put( temp.getSimg());
        }
        // 3. jsonarray를 json객체 포함
        object.put("bimglist" , jsonArray) ;

        // 3. 반한
        return object;
    }

    @Transactional
    public boolean itemdelete( int sno ){
        ShopEntity shopEntity =  shopRepository.findById( sno ).get();
        if( shopEntity != null ){
            // 해당 엔티티를 삭제
            for(ShopImgEntity temp : shopEntity.getShopimgEntities()){
                shopImgRepository.delete( temp );
            }
            shopRepository.delete( shopEntity );
            return true;
        }else{
            return false;
        }
    }

    @Transactional
    public boolean itemupdate(ShopDto shopDto) {
        Optional<ShopEntity> optional = shopRepository.findById(shopDto.getSno());
        System.out.println(shopDto.toString());
       // String dir = "C:\\Users\\82104\\git\\springproject_-animalhospital\\src\\main\\resources\\static\\upload\\"; // my notebook
          String dir = "C:\\Users\\504\\git\\springproject_-animalhospital\\src\\main\\resources\\static\\upload\\";
        // 배포용 String dir = "/home/ec2-user/app/springproject_-animalhospital/build/resources/main/static/upload/";
        if(optional.isPresent()) {
            ShopEntity shopEntity = optional.get(); // 새로운 내용을 인수로 보내야 함
            if(shopEntity.getShopimgEntities().size() != 0) {
                for (ShopImgEntity temp : shopEntity.getShopimgEntities()) {
                    try{
                        File oldfile = new File(dir + temp.getSimg());
                        if(oldfile.exists()) {
                            oldfile.delete();
                            shopEntity.setShopimgEntities(null);
                            shopImgRepository.deleteById(temp.getSimgno());
                        }
                    }catch(Exception e){e.printStackTrace();}
                }
            }
            shopEntity.setScontent(shopDto.getScontent());
            shopEntity.setPrice(shopDto.getPrice());
            shopEntity.setStitle(shopDto.getStitle());
            String uuidfile = null;

            if (shopDto.getSimg().size() != 0) {
                for (MultipartFile file : shopDto.getSimg()) {

                    UUID uuid = UUID.randomUUID();

                    uuidfile = uuid.toString() + "_" + file.getOriginalFilename().replaceAll("_", "-");

                    String filepath = dir + uuidfile;

                    try {

                        file.transferTo(new File(filepath));

                        ShopImgEntity shopImgEntity = ShopImgEntity.builder()
                                .simg(uuidfile)
                                .shopEntity(shopEntity)
                                .build();

                        shopImgRepository.save(shopImgEntity);

                        shopEntity.getShopimgEntities().add(shopImgEntity);



                    } catch (Exception e) {

                    }
                }

            }
            return true;
        }
        return false;
    }

    public int idcheck(int sno) {
        Optional<ShopEntity> optional = shopRepository.findById(sno);
        if(optional.isPresent()) {
            ShopEntity shopEntity = optional.get();
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Object principal = authentication.getPrincipal();
            String mid = null;
            if( principal instanceof UserDetails){
                mid = ((UserDetails) principal).getUsername();
            }else if( principal instanceof DefaultOAuth2User){
                Map<String , Object> map =  ((DefaultOAuth2User) principal).getAttributes();
                if( map.get("response") != null ){
                    Map< String , Object> map2  = (Map<String, Object>) map.get("response"); // 네이버
                    mid = map2.get("email").toString().split("@")[0];
                }else if(map.get("kakao_account") != null){
                    Map< String , Object> map2  = (Map<String, Object>) map.get("kakao_account"); // 카카오
                    mid = map2.get("email").toString().split("@")[0];
                }else if(map.get("kakao_account") == null && map.get("response") == null ) { // 구글, 깃허브
                    mid = map.get("email").toString().split("@")[0];
                }
            }else{
                return 3; // 아이디 없음(=로그인이 안되어 있음)
            }
            if( mid != null  ) {
                Optional<MemberEntity> optionalMember = memberRepository.findBymid(mid);
                if(optionalMember.isPresent()) {
                    MemberEntity memberEntity = optionalMember.get();
                    Optional<ShopEntity> shopid = shopRepository.findbymnoitem(memberEntity.getMno());
                    if(shopid.isPresent()) {

                        return 1; // 로그인된 회원과 상품을 등록한 회원이 일치
                    }
                    return 2;   // 로그인된 회원과 상품을 등록한 회원이 일치하지 않는 경우
                }

            }

        }
        return 4; // 프로그램 자체 오류
    }

    @Autowired
    ShopLikeRepository shopLikeRepository;

    @Transactional
    public int likesave(int sno) {

        Optional<ShopEntity> optional = shopRepository.findById(sno);
        if(optional.isPresent()) {
            ShopEntity shopEntity = optional.get();
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Object principal = authentication.getPrincipal();
            String mid = null;
            if( principal instanceof UserDetails){
                mid = ((UserDetails) principal).getUsername();
            }else if( principal instanceof DefaultOAuth2User){
                Map<String , Object> map =  ((DefaultOAuth2User) principal).getAttributes();
                if( map.get("response") != null ){
                    Map< String , Object> map2  = (Map<String, Object>) map.get("response"); // 네이버
                    mid = map2.get("email").toString().split("@")[0];
                }else if(map.get("kakao_account") != null){
                    Map< String , Object> map2  = (Map<String, Object>) map.get("kakao_account"); // 카카오
                    mid = map2.get("email").toString().split("@")[0];
                }else if(map.get("kakao_account") == null && map.get("response") == null ) { // 구글, 깃허브
                    mid = map.get("email").toString().split("@")[0];
                }
            }else{
                return 3; // 로그인이 안되어 있음
            }
            if( mid != null  ) {
                Optional<MemberEntity> optionalMember = memberRepository.findBymid(mid);
                if(optionalMember.isPresent()) {
                    MemberEntity memberEntity = optionalMember.get();
                    int mno = memberEntity.getMno();
                    Optional<ShopLikeEntity>  optionalShopLike = shopLikeRepository.existbyMemberandShop(mno, sno);
                    if(optionalShopLike.isPresent()) {
                        ShopLikeEntity shopLikeEntity = optionalShopLike.get();
                        memberEntity.getMemberlikeEntityList().remove(shopLikeEntity);
                        shopEntity.getShopLikeEntityList().remove(shopLikeEntity);
                        shopLikeRepository.delete(shopLikeEntity);
                        System.out.println("관심 상품 제거");
                        return 2; // 관심 상품 제거
                    }
                    ShopLikeEntity shopLikeEntity = ShopLikeEntity.builder()
                                    .memberlike(memberEntity)
                                            .shoplike(shopEntity)
                                                    .build();
                    shopLikeRepository.save(shopLikeEntity);
                    memberEntity.getMemberlikeEntityList().add(shopLikeEntity);
                    shopEntity.getShopLikeEntityList().add(shopLikeEntity);
                    System.out.println("관심 상품 추가");

                    return 1; // 관심 상품 추가
                }
            }
        }
        return 4; // 프로그램 오류
    }


    public int likecheck(int sno) {

        Optional<ShopEntity> optional = shopRepository.findById(sno);
        if(optional.isPresent()) {
            ShopEntity shopEntity = optional.get();
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Object principal = authentication.getPrincipal();
            String mid = null;
            if( principal instanceof UserDetails){
                mid = ((UserDetails) principal).getUsername();
            }else if( principal instanceof DefaultOAuth2User){
                Map<String , Object> map =  ((DefaultOAuth2User) principal).getAttributes();
                if( map.get("response") != null ){
                    Map< String , Object> map2  = (Map<String, Object>) map.get("response"); // 네이버
                    mid = map2.get("email").toString().split("@")[0];
                }else if(map.get("kakao_account") != null){
                    Map< String , Object> map2  = (Map<String, Object>) map.get("kakao_account"); // 카카오
                    mid = map2.get("email").toString().split("@")[0];
                }else if(map.get("kakao_account") == null && map.get("response") == null ) { // 구글, 깃허브
                    mid = map.get("email").toString().split("@")[0];
                }
            }else{
                return 3; // 로그인이 안되어 있음
            }
            if( mid != null  ) {
                Optional<MemberEntity> optionalMember = memberRepository.findBymid(mid);
                if(optionalMember.isPresent()) {
                    MemberEntity memberEntity = optionalMember.get();
                    int mno = memberEntity.getMno();
                    Optional<ShopLikeEntity>  optionalShopLike = shopLikeRepository.existbyMemberandShop(mno, sno);
                    if(optionalShopLike.isPresent()) {
                        return 2; // 관심 상품이 등록 되어 있음
                    }
                    return 1; // 관심 상품에 등록되어 있지 않음
                }
            }
        }
        return 4; // 프로그램 오류
    }
}
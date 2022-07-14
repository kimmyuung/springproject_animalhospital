package animalhospital.service;

import animalhospital.domain.board.BoardEntity;
import animalhospital.domain.board.BoardimgEntity;
import animalhospital.domain.board.ReplyEntity;
import animalhospital.domain.member.MemberEntity;
import animalhospital.domain.member.MemberRepository;
import animalhospital.domain.shop.ShopEntity;
import animalhospital.domain.shop.ShopImgEntity;
import animalhospital.domain.shop.ShopImgRepository;
import animalhospital.domain.shop.ShopRepository;
import animalhospital.dto.BoardDto;
import animalhospital.dto.ItemDto;
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
    public boolean itemsave(ItemDto itemDto) {

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
                ShopEntity shopEntity = itemDto.toentity();
                shopEntity.setMember( optionalMember.get() );
                shopRepository.save(shopEntity);
                String uuidfile = null;
                if (itemDto.getSimg().size() != 0) {
                    for (MultipartFile file : itemDto.getSimg()) {
                        UUID uuid = UUID.randomUUID();

                        uuidfile = uuid.toString() + "_" + file.getOriginalFilename().replaceAll("_", "-");
                        String dir = "C:\\Users\\504\\springproject_animalhospital\\src\\main\\resources\\static\\upload\\";
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

        System.out.println( "페이지 :"+ page );

        Page<ShopEntity> shopEntities = null ;
        Pageable pageable = PageRequest.of( page , 5 , Sort.by( Sort.Direction.DESC , "sno")    );


        List<  Map<String , String >  > Maplist = new ArrayList<>();

        shopEntities =shopRepository.findByblist(page, pageable);
        System.out.println( shopEntities.toString() );

        int btncount = 5;
        int startbtn  = ( page / btncount ) * btncount + 1;
        int endhtn = startbtn + btncount -1;
        if( endhtn > shopEntities.getTotalPages() ) endhtn = shopEntities.getTotalPages();


        for( ShopEntity entity : shopEntities ){
            // 3. map 객체 생성
            Map<String, String> map = new HashMap<>();
            map.put("bno", entity.getSno()+"" );
            map.put("btitle", entity.getStitle());
            map.put("bimg", entity.getShopimgEntities().get(0).getSimg());
            map.put( "startbtn" , startbtn+"" );
            map.put("mid", entity.getMember().getMid());
            map.put("bdate",  entity.getCreatedate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            map.put( "endhtn" , endhtn+"" );
            map.put( "totalpages" , shopEntities.getTotalPages()+"" );
            // 4. 리스트 넣기
            Maplist.add(map);
        }
        Map< String , List<  Map<String , String >  > > object = new HashMap<>();

        object.put( "blists" , Maplist );

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
            shopRepository.delete( shopEntity );
            return true;
        }else{
            return false;
        }
    }

    @Transactional
    public boolean itemupdate(int bno, ItemDto itemDto) {
        Optional<ShopEntity> optional = shopRepository.findById(bno);
        if(optional.isPresent()) {
            ShopEntity shopEntity = optional.get(); // 새로운 내용을 인수로 보내야 함
            shopEntity.setScontent(itemDto.getScontent());
            shopEntity.setPrice(itemDto.getPrice());
            shopEntity.setStitle(itemDto.getStitle());
            shopEntity.setShopimgEntities(itemDto.toentity().getShopimgEntities());
            return true;
        }
        return false;
    }

}

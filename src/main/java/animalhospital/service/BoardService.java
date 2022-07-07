package animalhospital.service;

import animalhospital.domain.board.BoardEntity;
import animalhospital.domain.board.BoardRepository;
import animalhospital.domain.board.BoardimgEntity;
import animalhospital.domain.board.BoardimgRespository;
import animalhospital.domain.member.MemberEntity;
import animalhospital.domain.member.MemberRepository;
import animalhospital.dto.BoardDto;
import animalhospital.dto.CrawlDto;
import animalhospital.dto.LoginDto;
import animalhospital.dto.OauthDto;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.File;
import java.net.URLEncoder;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class BoardService {
    @Autowired
    private HttpServletRequest request;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private BoardimgRespository boardimgRespository;

    @Autowired
    private MemberRepository memberRepository;


    @Transactional
    public boolean save(BoardDto boardDto) {

//        LoginDto loginDto  = (LoginDto) request.getSession().getAttribute("login");

//        MemberEntity memberEntity =  memberRepository.findById( loginDto.getMno() ).get();

        BoardEntity boardEntity = boardDto.toentity();
        boardRepository.save( boardEntity );

        String uuidfile = null;
        if( boardDto.getBimg().size() != 0 ){
            for(MultipartFile file : boardDto.getBimg() ){
                UUID uuid = UUID.randomUUID();

                uuidfile = uuid.toString() +"_"+ file.getOriginalFilename().replaceAll("_","-");
                String dir  = "C:\\Users\\504\\springproject_-animalhospital\\src\\main\\resources\\static\\upload\\";
                String filepath = dir+uuidfile;

                try {
                    file.transferTo( new File(filepath) );

                    BoardimgEntity boardimgEntity =  BoardimgEntity.builder()
                            .bimg( uuidfile )
                            .boardEntity(  boardEntity )
                            .build();

                    boardimgRespository.save(boardimgEntity );

                    boardEntity.getBoardimgEntities().add( boardimgEntity );

                }catch( Exception e ){ System.out.println("파일저장실패 : "+ e);}
            }

        }

        return true;
    }

    @Transactional
    public boolean noticesave(String btitle, String bcontent) {
        MemberEntity memberEntity = memberRepository.findBymid("admin").get();
        BoardEntity boardEntity = BoardEntity.builder()
                        .cno(1)
                                .bcontent(bcontent)
                                        .btitle(btitle)
                                             .memberEntity(memberEntity)
                                                        .build();
        boardRepository.save(boardEntity);
        return true;
    }

    @Transactional
    public boolean noticeupdate(int bno, String btitle, String bcontent) {
                        MemberEntity memberEntity = memberRepository.findBymid("admin").get();
                        Optional<BoardEntity> optional = boardRepository.findById(bno);
                        if(optional.isPresent()) {
                            BoardEntity boardEntity = optional.get();
                            boardEntity.setBtitle(btitle);
                            boardEntity.setBcontent(bcontent);
                            return true;
        }
        return false;
    }


    @Transactional
    public boolean noticedelete(int bno) {
        Optional<BoardEntity> optional = boardRepository.findById(bno);
        if(optional.isPresent()) {
            BoardEntity boardEntity = optional.get(); boardRepository.delete(boardEntity); return true;
        }
        return false;
        }

    public Map< String , List<Map<String , String >>> boardlist(int page ) // 인수
    {

        System.out.println( "페이지 :"+ page );

        Page<BoardEntity> boardEntitylist = null ;
        Pageable pageable = PageRequest.of( page , 5 , Sort.by( Sort.Direction.DESC , "bno")    );

        int cno=2;
        List<  Map<String , String >  > Maplist = new ArrayList<>();

        boardEntitylist =boardRepository.findByblist(cno, pageable);
        System.out.println( boardEntitylist.toString() );

        int btncount = 5;
        int startbtn  = ( page / btncount ) * btncount + 1;
        int endhtn = startbtn + btncount -1;
        if( endhtn > boardEntitylist.getTotalPages() ) endhtn = boardEntitylist.getTotalPages();


        for( BoardEntity entity : boardEntitylist ){
            // 3. map 객체 생성
            Map<String, String> map = new HashMap<>();
            map.put("bno", entity.getBno()+"" );
            map.put("btitle", entity.getBtitle());
            map.put("bimg", entity.getBoardimgEntities().get(0).getBimg());
            map.put( "startbtn" , startbtn+"" );
            map.put( "endhtn" , endhtn+"" );
            map.put( "totalpages" , boardEntitylist.getTotalPages()+"" );
            // 4. 리스트 넣기
            Maplist.add(map);
        }
        Map< String , List<  Map<String , String >  > > object = new HashMap<>();

        object.put( "blists" , Maplist );

        return  object;
    }

    public JSONObject getnoticelist(int page) {
        JSONObject jo = new JSONObject();
        // Pageable : 페이지처리 관련 인터페이스
        // PageRequest : 페이징처리 관련 클래스
        // PageRequest.of(page, size) : 페이징처리 설정
        // page = "현재 페이지" [0부터 시작 ]
        // size = "현재 페이지에 보여줄 게시물 수"
        // sort = "정렬기준" [Sort.Direction.DESC]
        // sort 문제점 : 정렬 필드명에 _인식 불가능 ----> SQL 처리
        Pageable pageable = PageRequest.of(page, 5, Sort.by(Sort.Direction.DESC, "cno")  );
        int cno = 1;
        Page<BoardEntity> boardEntities = boardRepository.findByblist(cno, pageable);
        JSONArray jsonArray = new JSONArray();

            for (BoardEntity entity : boardEntities ) {
                JSONObject object = new JSONObject();
                object.put("bno", entity.getBno());
                object.put("btitle", entity.getBtitle());
                object.put("bcontent", entity.getBtitle());
                object.put("bindate", entity.getCreatedate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")));
                object.put("mid", entity.getMemberEntity().getMid());
                jsonArray.put(object);
            }

        // 페이지에 표시할 총 버튼 개수
        int btncount = 5;
        // 시작 번호의 번호 [ 현재 페이지 / 표시할 버튼 수 ) * 표시할 버튼 수 + 1
        int startbtn = (page / btncount) * btncount + 1;
        // 끝 번호 버튼의 번호 [ 시작버튼 + 표시버튼수 - 1 ]
        int endbtn = startbtn + btncount - 1;
        // 만약에 끝번호가 마지막페이지보다 크면 끝번호는 마지막페이지 번호로 사용
        if(endbtn > boardEntities.getTotalPages()) endbtn = boardEntities.getTotalPages();
        jo.put("startbtn", startbtn);
        jo.put("endbtn", endbtn);
        jo.put("data", jsonArray);
        jo.put("totalpage", boardEntities.getTotalPages()); // 전체 페이지 수
        return jo;
    }

    public JSONObject getboard( int bno ){

        OauthDto loginDto = (OauthDto) request.getSession().getAttribute("login");
        Optional<BoardEntity> optionalRoomEntity =  boardRepository.findById(bno );
        BoardEntity boardEntity =  optionalRoomEntity.get();
        String same = null;
        if(boardEntity.getMemberEntity().getMid().equals(loginDto.getMid())){
            same =  "true";
        }else{
            same =  "false";
        }
        // 2.  해당 엔티티 -> json 객체 변환
        JSONObject object = new JSONObject();
        // 1. json에 엔티티 필드 값 넣기
        object.put("bno" ,boardEntity.getBno());
        object.put("btitle" , boardEntity.getBtitle());
        object.put("bcontent" , boardEntity.getBcontent());
        object.put("mid" , boardEntity.getMemberEntity().getMid());
        object.put("same" , same);

        JSONArray jsonArray = new JSONArray();
        for(  BoardimgEntity boardimgEntity : boardEntity.getBoardimgEntities() ) { //  룸별로 이미지 여러개
            jsonArray.put( boardimgEntity.getBimg());
        }
        // 3. jsonarray를 json객체 포함
        object.put("bimglist" , jsonArray) ;
        // 3. 반한
        return object;
    }

    @Transactional
    public boolean delete( int bno ){
        BoardEntity boardEntity =  boardRepository.findById( bno ).get();
        if( boardEntity != null ){
            // 해당 엔티티를 삭제
            boardRepository.delete( boardEntity );
            return true;
        }else{
            return false;
        }
    }

    public void 크롤링() {
         String inflearnUrl = "https://search.daum.net/search?nil_suggest=btn&w=tot&DA=SBC&q=%EB%82%A8%EC%96%91%EC%A3%BC%EC%8B%9C+%ED%99%94%EB%8F%84%EB%8F%99%EB%AC%BC%EB%B3%91%EC%9B%90";

        Connection conn = Jsoup.connect(inflearnUrl);
        try {
            Document document = conn.get();
            Elements title = document.getElementsByClass("inner_tit").first().select("b");
            Elements score = document.getElementsByClass("f_eb");

            String title2 = title.text().replaceAll(" ","");
            String score2 = score.first().text();
            String link = score.attr("href");

//            System.out.println(title2);
//            System.out.println(score2);
//            System.out.println(link);

//            Elements imageUrlElements = document.getElementsByClass("total_area");
//            Elements e = document.getElementsByClass("total_wrap api_ani_send");
//           // Elements imageUrlElements2 = document.getElementsByClass("dsc_wrap");
//           // System.out.println(document);
//            Elements a = imageUrlElements.first().getElementsByTag("a");
//
//            for (Element element : e) {
//                //System.out.println(element.getElementsByIndexEquals(1).get(5).text());
//               // System.out.println(element.getElementsByIndexEquals(2).text().length());
//
//
//
//                if(element.getElementsByIndexEquals(1).get(5).text().contains("동물병원")) {
//                    System.out.println(element.getElementsByIndexEquals(1).get(5).text());
//                    System.out.println(", "+element.getElementsByIndexEquals(2).text());
//                    System.out.println(","+element.getElementsByTag("a"));
//
//                }
//
//            }
//            for (Element element : imageUrlElements2) {
//                System.out.println(element);
//
//            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public CrawlDto crawling(String city, String name) {
        String code = city+name;
        String inflearnUrl = "https://search.daum.net/search?nil_suggest=btn&w=tot&DA=SBC&q="+code;
        Connection conn = Jsoup.connect(inflearnUrl);
        try {
            Document document = conn.get();
            Elements title = document.getElementsByClass("inner_tit").first().select("b");
            Elements score = document.getElementsByClass("f_eb");
//            String title2 = title.text().replaceAll(" ","");
            String score2 = score.first().text();
            String link = score.attr("href");
            CrawlDto crawlDto = new CrawlDto();
            crawlDto.setScroe(score2);
            crawlDto.setLink(link);
//            String link = score.attr("href");
            return  crawlDto;
          //  System.out.println(code);

//            if(name.equals(title2)) {
//                Elements score = document.getElementsByClass("f_eb");
//                String score2 = score.first().text();
//                String link = score.attr("href");
//            }


//

        } catch (Exception e) {
            e.printStackTrace();
        }
    return  null;
    }


   /* 조회수 증가
   @Transactional
    public JSONObject getboard(int bno) { // 개별조회
        // 조회수 증가처리
        String ip = request.getRemoteAddr(); // 사용자의 ip 가져오기


        Optional<BoardEntity> Optional = boardRepository.findById(bno);
        BoardEntity entitiy = Optional.get();

        // ip와 bno를 합쳐서 세션(서버내 저장소) 부여

        Object com = request.getSession().getAttribute(ip+bno);
        if(com == null) {
            request.getSession().setAttribute(ip+bno, 1);
            request.getSession().setMaxInactiveInterval(60*60*24); // 세션 허용시간 [ 초단위 ]
            // 조회수 증가
            entitiy.setBview(entitiy.getBview()+1);
        }

        JSONObject jo = new JSONObject();
        jo.put("bno", entitiy.getBno());
        jo.put("btitle", entitiy.getBtitle() );
        jo.put("bcontent", entitiy.getBcontent());
        jo.put("bview", entitiy.getBview());
        jo.put("blike", entitiy.getBlike());
        jo.put("bindate" , entitiy.getCreateDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm") ) );
        jo.put("bmodate" , entitiy.getUpdateDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm") ) );
        jo.put("mid", entitiy.getMemberEntity().getMid());
        return jo;
    }
    */
}

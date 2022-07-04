package animalhospital.service;

import animalhospital.domain.board.BoardEntity;
import animalhospital.domain.board.BoardRepository;
import animalhospital.domain.board.BoardimgEntity;
import animalhospital.domain.board.BoardimgRespository;
import animalhospital.dto.BoardDto;
import org.json.JSONArray;
import org.json.JSONObject;
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
import java.util.*;

@Service
public class BoardService {
    @Autowired
    private HttpServletRequest request;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private BoardimgRespository boardimgRespository;

    @Transactional
    public boolean save(BoardDto boardDto) {

        BoardEntity boardEntity = boardDto.toentity();
        boardRepository.save( boardEntity );
        String uuidfile = null;
        if( boardDto.getBimg().size() != 0 ){
            for(MultipartFile file : boardDto.getBimg() ){
                UUID uuid = UUID.randomUUID();

                uuidfile = uuid.toString() +"_"+ file.getOriginalFilename().replaceAll("_","-");
                String dir  = "C:\\Users\\504\\springproject_animalhospital\\src\\main\\resources\\static\\upload\\";
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
    public boolean noticesave(BoardDto boardDto) { BoardEntity boardEntity = boardDto.toentity();
        boardRepository.save(boardEntity); return true; }

    @Transactional
    public boolean noticeupdate(int bno, BoardDto boardDto) {
                        Optional<BoardEntity> optional = boardRepository.findById(bno);
                        if(optional.isPresent()) {
                            BoardEntity boardEntity = optional.get();
                            boardEntity.setBtitle(boardDto.getBtitle());
                            boardEntity.setBcontent(boardDto.getBcontent());
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
        Page<BoardEntity> boardEntities = null ;
        Pageable pageable = PageRequest.of( page , 5 , Sort.by( Sort.Direction.DESC , "bno")    );
        int cno=2;
        List<  Map<String , String >  > Maplist = new ArrayList<>();
//        int btncount = 5;
//        int startbtn  = ( page / btncount ) * btncount + 1;
//        int endhtn = startbtn + btncount -1;
//        if( endhtn > boardEntities.getTotalPages() ) endhtn = boardEntities.getTotalPages();

        Page<BoardEntity> boardEntitylist =boardRepository.findByblist(cno, pageable);
        for( BoardEntity entity : boardEntitylist ){
            // 3. map 객체 생성
            Map<String, String> map = new HashMap<>();
            map.put("bno", entity.getBno()+"" );
            map.put("btitle", entity.getBtitle());
            map.put("bimg", entity.getBoardimgEntities().get(0).getBimg());
//                    object.put( "startbtn" , startbtn );
//        object.put( "endhtn" , endhtn );
//        object.put( "totalpages" , boardEntities.getTotalPages() );
            // 4. 리스트 넣기
            Maplist.add(map);
        }
        Map< String , List<  Map<String , String >  > > object = new HashMap<>();

        object.put( "blists" , Maplist );

        return  object;
    }

    public JSONObject getboard( int bno ){

        Optional<BoardEntity> optionalRoomEntity =  boardRepository.findById(bno );
        BoardEntity boardEntity =  optionalRoomEntity.get();
        // 2.  해당 엔티티 -> json 객체 변환
        JSONObject object = new JSONObject();
        // 1. json에 엔티티 필드 값 넣기
        object.put("bno" ,boardEntity.getBno());
        object.put("btitle" , boardEntity.getBtitle());
        object.put("bcontent" , boardEntity.getBcontent());
        object.put("mid" , boardEntity.getMemberEntity().getMid());

        JSONArray jsonArray = new JSONArray();
        for(  BoardimgEntity boardimgEntity : boardEntity.getBoardimgEntities() ) { //  룸별로 이미지 여러개
            jsonArray.put( boardimgEntity.getBimg());
        }
        // 3. jsonarray를 json객체 포함
        object.put("bimglist" , jsonArray) ;
        // 3. 반한
        return object;
    }

    public JSONArray getnoticelist(int page) {
        JSONArray array = new JSONArray();
        Page<BoardEntity> boardEntities = null;
        Pageable pageable = PageRequest.of(page, 5, Sort.by(Sort.Direction.DESC, "cno")  );
        int cno = 1;
        List<BoardEntity> boardEntityList = boardRepository.findbynoticelist(cno, pageable);
        for(BoardEntity temp : boardEntityList) {
            JSONObject jo = new JSONObject();
            jo.put("bno", temp.getBno());
            jo.put("btitle", temp.getBtitle());
            jo.put("bcontent", temp.getBcontent());
            jo.put("createdate", temp.getCreatedate());
            array.put(jo);
        }
        return array;
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


}

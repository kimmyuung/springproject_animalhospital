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
import java.util.UUID;

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

//        LoginDto loginDto  = (LoginDto) request.getSession().getAttribute("login");

//        MemberEntity memberEntity =  memberRepository.findById( loginDto.getMno() ).get();

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


    public JSONObject boardlist(int page){
        int cno=2;
        JSONObject object = new JSONObject();
        System.out.println(page);
        Page<BoardEntity> boardEntities = null ;
        Pageable pageable = PageRequest.of( page , 5 , Sort.by( Sort.Direction.DESC , "bno")    ); // SQL : limit 와 동일 한 기능처리

        boardEntities = boardRepository.findByblist( cno ,  pageable );
        System.out.println(boardEntities);

        int btncount = 5;
        int startbtn  = ( page / btncount ) * btncount + 1;
        int endhtn = startbtn + btncount -1;
        if( endhtn > boardEntities.getTotalPages() ) endhtn = boardEntities.getTotalPages();


        JSONArray jsonArray = new JSONArray();
        for( BoardEntity entity : boardEntities ){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("bno", entity.getBno());
            jsonObject.put("btitle", entity.getBtitle());
            jsonObject.put("modifiedate", entity.getModifiedate());
            jsonObject.put("mno", entity.getMno());
            jsonArray.put(jsonObject);
        }

        object.put( "startbtn" , startbtn );
        object.put( "endhtn" , endhtn );
        object.put( "totalpages" , boardEntities.getTotalPages() );
        object.put( "data" , jsonArray );

        return object;
    }


}

package animalhospital.conrtroller;

import animalhospital.dto.BoardDto;
import animalhospital.service.BoardService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/board")
public class BoardController {

    @Autowired
    private BoardService boardService;

    @GetMapping("/list")
    public String list(){ return "board/mypetlist";}



    @PostMapping("/write")
    @ResponseBody
    public boolean write_save( BoardDto boardDto ){
        boardDto.setCno(2);
        boardService.save( boardDto );

        return true;
    }

    @PostMapping("/blist")
    @ResponseBody
    public Map< String , List<Map<String , String >>>
    blist(HttpServletResponse response, @RequestParam("page") int page ){
        return boardService.boardlist(page);
    }

    @GetMapping("/getboard")
    public void getboard( @RequestParam("bno") int bno ,
                          HttpServletResponse response  ){
        try{
            JSONObject object =  boardService.getboard( bno );
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.getWriter().print( object );
        }catch( Exception e ){ System.out.println( e ); }

    }

    @DeleteMapping("/bdelete")
    @ResponseBody
    public boolean delete(@RequestParam("bno") int bno ){
        System.out.println(bno);
        return boardService.delete( bno );
    }

}
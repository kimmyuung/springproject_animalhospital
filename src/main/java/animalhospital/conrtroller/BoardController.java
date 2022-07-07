package animalhospital.conrtroller;

import animalhospital.dto.BoardDto;
import animalhospital.service.BoardService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
        System.out.println("컨트롤러"+boardDto);
        boolean result = boardService.save( boardDto );

        return result;
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
//            JSONObject reply = boardService.getreply(bno);
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.getWriter().print( object );
//            response.getWriter().print( reply );
        }catch( Exception e ){ System.out.println( e ); }

    }

    @DeleteMapping("/bdelete")
    @ResponseBody
    public boolean delete(@RequestParam("bno") int bno ){
        System.out.println(bno);
        return boardService.delete( bno );
    }

    @PostMapping("/replysave")
    @ResponseBody
    public boolean replysave(@RequestParam("bno") int bno, @RequestParam("reply") String reply){
        return boardService.replysave( bno, reply );
    }

    @GetMapping("/getreply")
    @ResponseBody
    public void getreply(@RequestParam("bno")int bno, HttpServletResponse response){
        try {
            JSONArray jsonArray = boardService.getreply(bno);
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.getWriter().print(jsonArray);
        } catch (IOException e) {
            System.out.println("getreply error : "+e);
        }
    }

    @GetMapping("/replydelete")
    @ResponseBody
    public boolean replydelete(@RequestParam("rno") int rno){
        System.out.println("replydelete : " + rno);
        return boardService.replydelete(rno);
    }
    @GetMapping("/replyupdate")
    @ResponseBody
    public void replyupdate(@RequestParam("rno") int rno,HttpServletResponse response){
        try {
            JSONObject object = boardService.replyupdate(rno);
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.getWriter().print(object);
//            System.out.println("replyupdate : " + object);
        } catch (IOException e) {
            System.out.println("replyupdate error : "+e);
        }
    }
    @PostMapping("/reupdate")
    @ResponseBody
    public boolean reupdate(@RequestParam("rno") int rno, @RequestParam("reply") String reply){
        return boardService.reupdate( rno, reply );
    }
}
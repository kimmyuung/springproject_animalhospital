package animalhospital.conrtroller;

import animalhospital.dto.BoardDto;
import animalhospital.service.BoardService;
import org.json.JSONArray;
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

    @GetMapping("/notice")
    public String notice(){ return "board/adminnotice";}

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

    @PostMapping("/getnotice")
    @ResponseBody
    public JSONArray getnoticelist(HttpServletResponse response, @RequestParam("page") int page ) {
        return boardService.getnoticelist(page);
    }
    @PostMapping("/noticesave")
    @ResponseBody
    public JSONArray noticesave(HttpServletResponse response, @RequestParam("page") int page ) {
        return boardService.getnoticelist(page);
    }
    @DeleteMapping("/deletenotice")
    @ResponseBody
    public boolean noticedelete(@RequestParam("bno") int bno) {return boardService.noticedelete(bno);}

    @PutMapping("/updatenotice")
    @ResponseBody
    public boolean noticeupdate(@RequestParam("bno") int bno, BoardDto boardDto) {return boardService.noticeupdate(bno, boardDto);}


}
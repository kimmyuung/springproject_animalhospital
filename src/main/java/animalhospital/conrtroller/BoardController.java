package animalhospital.conrtroller;

import animalhospital.dto.BoardDto;
import animalhospital.service.BoardService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/board")
public class BoardController {

    @Autowired
    private HttpServletRequest request;

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
    public boolean list(HttpServletResponse response, @RequestParam("page") int page ){
        System.out.println("asss");
//        JSONObject json = boardService.boardlist(page);
//        try {
//            response.setContentType("application/json");
//            response.setCharacterEncoding("UTF-8");
//            response.getWriter().print(json);
//        }catch( Exception e ){ System.out.println( e );}
        return false;
    }

}
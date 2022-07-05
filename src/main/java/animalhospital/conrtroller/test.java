package animalhospital.conrtroller;

import animalhospital.dto.BoardDto;
import animalhospital.service.BoardService;
import animalhospital.service.MapService;
import animalhospital.service.MemberService;
import org.json.JSONArray;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class test { // 인덱스 컨트롤러 및 관리자 컨트롤러로 사용
    @Autowired
    BoardService boardService;
    @Autowired
    MapService mapService;

    @Autowired
    MemberService memberService;

    @GetMapping("/")
    public String main( Model model ){
        return "main";
    }

    @GetMapping("/admin/adminlogin")
    public String adminlogin() {return "admin/adminlogin";}

    @GetMapping("/board/notice")
    public String notice(){ return "board/adminnotice";}

    @PostMapping("/admin/adminlogin")
    @ResponseBody
    public boolean adminlogin(@RequestParam("mid") String mid, @RequestParam("mpassword") String mpassword) {
        return memberService.adminlogin(mid, mpassword);
    }

    @PostMapping("/admin/getnotice")
    @ResponseBody
    public JSONArray getnoticelist(HttpServletResponse response, @RequestParam("page") int page ) {
        return boardService.getnoticelist(page);
    }
    @PostMapping("/admin/noticesave")
    @ResponseBody
    public JSONArray noticesave(HttpServletResponse response, @RequestParam("page") int page ) {
        return boardService.getnoticelist(page);
    }
    @DeleteMapping("/admin/deletenotice")
    @ResponseBody
    public boolean noticedelete(@RequestParam("bno") int bno) {return boardService.noticedelete(bno);}

    @PutMapping("/admin/updatenotice")
    @ResponseBody
    public boolean noticeupdate(@RequestParam("bno") int bno, BoardDto boardDto) {return boardService.noticeupdate(bno, boardDto);}



    @GetMapping("/getlist")
    @ResponseBody
    public void getlist(@RequestParam("codenamelist") JSONArray codenamelist){
       //  String result = code +" "+ name;
        System.out.println(  "json : " + codenamelist.getJSONObject(0).get("code"));


        //String codename[] = new String[count];
        //System.out.println(count);
         //  codename[0] = result;
         //  System.out.println(result);

    }


    @GetMapping("/map")
    @ResponseBody
    public void gethlist(HttpServletResponse response)  {
        JSONArray list =  mapService.map();

               try{
                   response.setContentType("application/json");
                   response.setCharacterEncoding("UTF-8");
                   response.getWriter().print(list);
               } catch(Exception e) {}


    }


}

package animalhospital.conrtroller;

import animalhospital.domain.member.MemberEntity;
import animalhospital.dto.BoardDto;
import animalhospital.service.BoardService;
import animalhospital.service.MemberService;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.URI;

@Controller
public class test { // 인덱스 컨트롤러 및 관리자 컨트롤러로 사용
    @Autowired
    BoardService boardService;

    @Autowired
    MemberService memberService;

    @Autowired
    HttpServletRequest request;

    @Autowired
    HttpServletResponse response;

    @GetMapping("/")
    public String main( Model model ){
        return "main";
    }

    @GetMapping("/board/notice")
    public String notice(){ return "admin/adminnotice";}

    /* 관리자 로그인을 UserDetailService에서 처리 예정
    @PostMapping("/member/adminlogincontroller")
    @ResponseBody
    public ResponseEntity adminlogin(@RequestParam("mid") String mid, @RequestParam("mpassword") String mpassword) {
        boolean result = memberService.adminlogin(mid, mpassword);
        if(result == true) {
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create("/"));
            return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
        }
        else {
            return null;
        }

    }
    */

    @PostMapping("/board/getnotice")
    @ResponseBody
    public JSONArray getnoticelist(@RequestParam("page") int page ) {
        JSONArray js = boardService.getnoticelist(page);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        System.out.println(js.toString());
        return js;
    }
    @PostMapping("/admin/noticesave")
    @ResponseBody
    public boolean noticesave(@RequestParam("btitle") String btitle, @RequestParam("bcontent") String bcontent ) {
        return boardService.noticesave(btitle, bcontent);
    }
    @DeleteMapping("/admin/deletenotice")
    @ResponseBody
    public boolean noticedelete(@RequestParam("bno") int bno) {return boardService.noticedelete(bno);}

    @PutMapping("/admin/updatenotice")
    @ResponseBody
    public boolean noticeupdate(@RequestParam("bno") int bno, BoardDto boardDto) {return boardService.noticeupdate(bno, boardDto);}



}

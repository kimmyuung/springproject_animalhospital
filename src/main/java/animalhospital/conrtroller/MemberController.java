package animalhospital.conrtroller;

import animalhospital.dto.OauthDto;
import animalhospital.dto.RequestDto;
import animalhospital.service.MemberService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RequestMapping("/member")
@Controller
public class MemberController {

    @Autowired
    MemberService memberService;

    @GetMapping("/login")
    public String login() {return "member/login";}

   @DeleteMapping("/delete")
   @ResponseBody
    public boolean delete(HttpServletRequest request) {
        OauthDto oauthDto = (OauthDto)request.getSession().getAttribute("login");
       return memberService.delete(oauthDto);
    }
    @GetMapping("/memberinfo")
    public String memberinfo(){
        return "member/memberinfo";
    }
    @GetMapping("/request")
    public String request() {return "member/request";}


//    @PostMapping("/requestsave")
//    @ResponseBody
//    public boolean requestsave (String hname, String hdate) {
//        System.out.println("save");
//        System.out.println(hname+ hdate);
//        memberService.requestsave(hname, hdate);
//        return true;
//    }


    @PostMapping("/requestsave")
    @ResponseBody
    public int requestsave (RequestDto requestDto, HttpServletResponse response) {

            int result = memberService.requestsave(requestDto);
            System.out.println("result : "+result);
            return result;
    }

    @GetMapping("/getinfo")
    @ResponseBody
    public void getinfo(HttpServletRequest request, HttpServletResponse response){
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.getWriter().println(memberService.getinfo());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @GetMapping("/message")
    public String message(){
        return "member/message";

    }
    @GetMapping("/gettomsglist")
    @ResponseBody
    public void gettomsglist(HttpServletResponse response, @RequestParam("type") int type){
        try {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().print(memberService.gettomsglist(type));
        }catch (Exception e){
            System.out.println(e);
        }
    }

    @GetMapping("/getfrommsglist")
    public void getfrommsglist(HttpServletResponse response, @RequestParam("type") int type){
        try {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().print(memberService.getfrommsglist(type));
        }catch (Exception e){
            System.out.println(e);
        }
    }

    @GetMapping("/getmid")
    public void getmid(HttpServletResponse response){
        try {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().print(memberService.getmid());
        }catch (Exception e){
            System.out.println(e);
        }
    }

    @PutMapping("/isread") //5.
    @ResponseBody
    public boolean isread(@RequestParam ("msgno") int msgno){
        return memberService.isread(msgno);
    }

    @DeleteMapping("/msgdelete")
    @ResponseBody
    public boolean msgdelete(@RequestBody List<Integer> deletelist){
        return memberService.msgdelete(deletelist);
    }



}

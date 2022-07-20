package animalhospital.conrtroller;

import animalhospital.dto.OauthDto;
import animalhospital.dto.RequestDto;
import animalhospital.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
    public boolean requestsave (RequestDto requestDto) {
        System.out.println(requestDto.toString());
        boolean result = memberService.requestsave(requestDto);

        return result;
    }

    @GetMapping("/getmid")
    @ResponseBody
    public void getmid(HttpServletRequest request, HttpServletResponse response){
        try {
            OauthDto oauthDto = (OauthDto)request.getSession().getAttribute("login");
            String mid = oauthDto.getMid();
            response.getWriter().println(mid);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

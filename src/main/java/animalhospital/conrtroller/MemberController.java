package animalhospital.conrtroller;

import animalhospital.dto.OauthDto;
import animalhospital.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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



}

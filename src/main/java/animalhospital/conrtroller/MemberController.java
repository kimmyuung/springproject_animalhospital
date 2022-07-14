package animalhospital.conrtroller;

import animalhospital.dto.OauthDto;
import animalhospital.service.ItemService;
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

    @Autowired
    ItemService itemService;

    @GetMapping("/login")
    public String login() {return "member/login";}

   @DeleteMapping("/delete")
   @ResponseBody
    public boolean delete(HttpServletRequest request) {
        OauthDto oauthDto = (OauthDto)request.getSession().getAttribute("login");
       return memberService.delete(oauthDto);
    }

    @GetMapping("/shop")
    public String shop() {return "member/shop";}

    @GetMapping("/getitem")
    @ResponseBody
    public void getitemlist(HttpServletResponse response) {
        try{
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.getWriter().print("test");
        }catch(Exception e){e.printStackTrace();}
    }
    @PostMapping("/itemsave")
    @ResponseBody
    public boolean itemsave(@RequestParam("stitle") String stitle,
                            @RequestParam("scontent") String scontent,
                             @RequestParam("sprice") int sprice
    ) {
        System.out.println(stitle + "  " + scontent + "  " + sprice);
        return false;
    }

    @PutMapping("/itemupdate")
    @ResponseBody
    public boolean itemupdate( @RequestParam("sno") int sno,
            @RequestParam("stitle") String stitle,
            @RequestParam("scontent") String scontent,
            @RequestParam("sprice") int sprice
                              )
    {
        System.out.println(stitle + "  " + scontent + "  " + sprice);
        return false;
    }

    @DeleteMapping("/deleteitem")
    @ResponseBody
    public boolean deleteitem(@RequestParam("sno") int sno) {
        return false;
    }



}

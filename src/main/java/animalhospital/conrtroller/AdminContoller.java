package animalhospital.conrtroller;

import animalhospital.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/admin")
public class AdminContoller {

    @Autowired
    MemberService memberService;

    @GetMapping("/binlist")
    public String binlist(){
        return "admin/binlist";
    }

    @GetMapping("/getbinlist")
    public void getbinlist(HttpServletResponse response){
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        try {
            response.getWriter().print(memberService.getbinlist());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/setrole")
    public void setrole(HttpServletResponse response, @RequestParam("mno") int mno,@RequestParam("hname") String hname,@RequestParam("hdate") String hdate,@RequestParam("bin") String bin){
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.getWriter().print(memberService.setrole(mno, hname, hdate,bin));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

package animalhospital.conrtroller;

import animalhospital.dto.ShopDto;
import animalhospital.dto.OauthDto;
import animalhospital.service.ItemService;
import animalhospital.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

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
    public void getitem(HttpServletResponse response,  @RequestParam("sno") int sno) {
        try{
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.getWriter().print(itemService.getitem(sno));
        }catch(Exception e){e.printStackTrace();}
    }

    @GetMapping("/getitemlist")
    @ResponseBody
    public Map<String, List<Map<String, String>>> getitemlist(@RequestParam("page") int page) {
             return itemService.itemlist(page);
    }

    @PostMapping("/itemsave")
    @ResponseBody
    public boolean itemsave(ShopDto shopDto) {
        System.out.println(shopDto.getStitle() +" :  "+ shopDto.getScontent());
        return itemService.itemsave(shopDto);
    }

    @PutMapping("/itemupdate")
    @ResponseBody
    public boolean itemupdate( ShopDto shopDto)
    {
        return itemService.itemupdate(shopDto);
    }

    @DeleteMapping("/deleteitem")
    @ResponseBody
    public boolean deleteitem(@RequestParam("sno") int sno) {
        return itemService.itemdelete(sno);
    }

    @GetMapping("/itemview{sno}")
    public String itemview(@PathVariable("sno") String sno) {return "member/itemview";}

    @GetMapping("/idcheck")
    @ResponseBody
    public boolean idcheck(@RequestParam("sno") int sno) {
        return itemService.idcheck(sno);
    }


}

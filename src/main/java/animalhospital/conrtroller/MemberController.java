package animalhospital.conrtroller;

import animalhospital.dto.ItemDto;
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
    public void getitem(HttpServletResponse response,  @RequestParam("sno") int sno) {
        try{
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.getWriter().print(itemService.getitem(sno));
        }catch(Exception e){e.printStackTrace();}
    }

    @GetMapping("/getitemlist")
    @ResponseBody
    public void getitemlist(HttpServletResponse response, @RequestParam("page") int page) {
        try{
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.getWriter().print(itemService.itemlist(page));
        }catch(Exception e){e.printStackTrace();}
    }

    @PostMapping("/itemsave")
    @ResponseBody
    public boolean itemsave(ItemDto itemDto) {
        System.out.println(itemDto.getStitle() +" :  "+ itemDto.getScontent());
        return itemService.itemsave(itemDto);
    }

    @PutMapping("/itemupdate")
    @ResponseBody
    public boolean itemupdate( @RequestParam("sno") int sno, ItemDto itemDto )
    {
        return itemService.itemupdate(sno, itemDto);
    }

    @DeleteMapping("/deleteitem")
    @ResponseBody
    public boolean deleteitem(@RequestParam("sno") int sno) {
        return itemService.itemdelete(sno);
    }



}

package animalhospital.conrtroller;

import animalhospital.service.BoardService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/map")
public class MapController {


    @Autowired
    private HttpServletRequest request;     // 1. 세션 호출을 위한 request 객체 생성

    @Autowired
    BoardService boardService;

    @GetMapping("/infopage/{city}/{name}")
    public String list(@PathVariable("city") String city, @PathVariable("name") String name){
        request.getSession().setAttribute("city", city);
        request.getSession().setAttribute("name", name);
        return "hospitalinfo";
    }


    @GetMapping("/view")
    @ResponseBody
    public String view(HttpServletResponse response, @RequestParam("hname") String hname, @RequestParam("hdate") String hdate ){
        request.getSession().setAttribute("hname", hname);
        request.getSession().setAttribute("hdate", hdate);
        return hname;
    }

    @PostMapping("/info")
    @ResponseBody
    public void info(HttpServletResponse response ){
        String hname =  (String) request.getSession().getAttribute("hname");
        String hdate =  (String) request.getSession().getAttribute("hdate");
        System.out.println(hname);
        JSONObject object = new JSONObject();
        object.put("hname",hname);
        object.put("hdate",hdate);
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.getWriter().print(object);
        }catch( Exception e ){
            System.out.println(  e   );
        }
    }

@GetMapping("/infoh")
@ResponseBody
public void infoh(HttpServletResponse response) {
String city = (String)request.getSession().getAttribute("city");
String name = (String)request.getSession().getAttribute("name");
    System.out.println("크롤링에서뽑아온이름"+boardService.crawling(city,name));
try{
    response.setCharacterEncoding("UTF-8");
    response.getWriter().print(boardService.crawling(city,name));
    response.getWriter().print(boardService.crawling(city,name));
}catch (Exception e) {System.out.println(e);}


}



}
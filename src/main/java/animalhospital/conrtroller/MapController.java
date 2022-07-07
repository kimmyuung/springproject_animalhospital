package animalhospital.conrtroller;

import animalhospital.dto.CrawlDto;
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


    @GetMapping("/infopage")
    public String list(){ return "hospitalinfo";}


    @GetMapping("/view")
    @ResponseBody
    public String view(HttpServletResponse response, @RequestParam("hname") String hname, @RequestParam("hdate") String hdate,@RequestParam("hcity") String hcity ){
        request.getSession().setAttribute("hname", hname);
        request.getSession().setAttribute("hdate", hdate);
        request.getSession().setAttribute("hcity",hcity);
        return hname;
    }

    @PostMapping("/info")
    @ResponseBody
    public void info(HttpServletResponse response ){
        String hname =  (String) request.getSession().getAttribute("hname");
        String hdate =  (String) request.getSession().getAttribute("hdate");
        String hcity = (String) request.getSession().getAttribute("hcity");
        System.out.println("크롤링에서뽑아온이름"+boardService.crawling(hcity,hname));
        CrawlDto crawlDto = boardService.crawling(hcity,hname);
        //String score = boardService.crawling(hcity,hname);
        System.out.println(hname);
        JSONObject object = new JSONObject();
        object.put("hname",hname);
        object.put("hdate",hdate);
        object.put("hcity",hcity);
        object.put("score",crawlDto.getScroe());
        object.put("link",crawlDto.getLink());


        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.getWriter().print(object);
        }catch( Exception e ){
            System.out.println(  e   );
        }
    }

//@GetMapping("/infoh")
//@ResponseBody
//public void infoh(HttpServletResponse response) {
//String city = (String)request.getSession().getAttribute("city");
//String name = (String)request.getSession().getAttribute("name");
//    System.out.println("크롤링에서뽑아온이름"+boardService.crawling(city,name));
//try{
//    response.setCharacterEncoding("UTF-8");
//    response.getWriter().print(boardService.crawling(city,name));
//    response.getWriter().print(boardService.crawling(city,name));
//}catch (Exception e) {System.out.println(e);}
//
//
//}



}
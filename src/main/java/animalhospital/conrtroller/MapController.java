package animalhospital.conrtroller;

import animalhospital.dto.CrawlDto;
import animalhospital.dto.ReviewDto;
import animalhospital.service.BoardService;
import animalhospital.service.MapService;
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
    MapService mapService;

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

    @GetMapping("/search")
    public void search(HttpServletResponse response,
                       @RequestParam("keyword") String keyword){

        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application.json");
            response.getWriter().println(mapService.search(keyword));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @PostMapping("/addreview" )
    @ResponseBody
    public boolean addreview(HttpServletResponse response, ReviewDto reviewDto){
        System.out.println("Dddd");
        String hname =  (String) request.getSession().getAttribute("hname");
        String hdate =  (String) request.getSession().getAttribute("hdate");
        reviewDto.setHname(hname);
        reviewDto.setHdate(hdate);
        System.out.println("sssss"+reviewDto);
        boolean result = mapService.addreview(reviewDto);
        return result;
    }

    @PostMapping("/getreviewlist")
    @ResponseBody
    public void getreviewlist(HttpServletResponse response, @RequestParam("hname") String hname , @RequestParam("hdate") String hdate  , @RequestParam("page") int page  ){
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.getWriter().println(mapService.getreviewlist(hname,hdate, page ));
        }catch( Exception e ){ System.out.println( e ); }
    }

    @GetMapping("/getreview")
    public void getreview(HttpServletResponse response , @RequestParam("rno") int rno){
        System.out.println(rno);
        try{
            JSONObject object =  mapService.getreview( rno );
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.getWriter().print( object );
        }catch( Exception e ){ System.out.println( e ); }
    }


    @DeleteMapping("/rdelete")
    @ResponseBody
    public boolean rdelete(@RequestParam("rno") int rno ){
        System.out.println(rno);
        return mapService.rdelete( rno );
    }

    @PostMapping("/updatereview" )
    @ResponseBody
    public boolean updatereview(HttpServletResponse response, ReviewDto reviewDto){
        System.out.println("sssss"+reviewDto);
        boolean result = mapService.updatereview(reviewDto);
        return result;
    }

}
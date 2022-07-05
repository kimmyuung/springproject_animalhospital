package animalhospital.conrtroller;

import animalhospital.service.MapService;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;

@Controller
public class test {

    @Autowired
    MapService mapService;
    @GetMapping("/")
    public String main( Model model ){

        return "main";
    }

    @GetMapping("/map")
    public void map( HttpServletResponse response){

        try{
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.getWriter().print(mapService.map());
        }catch( Exception e){
            System.out.println(e);
        }

    }


}

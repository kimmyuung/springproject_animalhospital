package animalhospital.service;

import org.apache.tomcat.util.json.JSONParser;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

@Service
public class MapService {
    public JSONArray map() {
        JSONArray animalhospital =  new JSONArray();
        try {
            JSONArray hospital = null;
            for (int i = 1; i < 4; i++) {

                //공공데이터 포털
                //1. url 가져오기 [URL : java.net패키지]
                URL url = new URL("https://openapi.gg.go.kr/Animalhosptl?&pIndex=" + i + "&pSize=1000&Key=47d367a4e715424e8c25f17ff85a81ea&type=json");
                //url 클래스 : java 에서 http url 객체화
                //2. java 해당 url 읽기 [바이트 스트림]
                //BufferedReader : 외부데이터(자바 외) 읽어올 때 사용되는 클래스
                BufferedReader bf;
                bf = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
                //3. 읽어오기
                String result = bf.readLine(); //스트림에 존재하는 바이트 모두 읽어오기
                //4. 읽어온 스트림을 JSON 변환
                JSONObject object = new JSONObject(result);
                JSONArray Animalhosptl = (JSONArray) object.get("Animalhosptl");
                JSONObject row = (JSONObject) Animalhosptl.get(1);
                hospital = (JSONArray) row.get("row");

                for (int j = 0; j < hospital.length(); j++) {
                    JSONObject hospitalobject = new JSONObject();
                    JSONObject hospitalinfo = (JSONObject) hospital.get(j);
                    String open = (String) hospitalinfo.get("BSN_STATE_NM");
                    if (open.equals("정상")) {
                        hospitalobject.put("name", hospitalinfo.get("BIZPLC_NM"));
                        hospitalobject.put("city", hospitalinfo.get("SIGUN_NM"));
                        hospitalobject.put("opendate", hospitalinfo.get("LICENSG_DE"));
                        hospitalobject.put("addr", hospitalinfo.get("REFINE_ROADNM_ADDR"));
                        hospitalobject.put("tel", hospitalinfo.get("LOCPLC_FACLT_TELNO"));
                        hospitalobject.put("lat", hospitalinfo.get("REFINE_WGS84_LAT"));
                        hospitalobject.put("logt", hospitalinfo.get("REFINE_WGS84_LOGT"));
                        animalhospital.put(hospitalobject);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return animalhospital;
    }

    public JSONArray search (String keyword){
        JSONArray hospital = map();
        JSONArray jsonArray = new JSONArray();
        if(keyword != null) {
            for(int i = 0; i < hospital.length(); i++){
                JSONObject object = (JSONObject) hospital.get(i);
                String name = (String) object.get("name");
                if( name.contains(keyword) ){
                    jsonArray.put(object);
                }
            }
        }
//        System.out.println(jsonArray);
        return jsonArray;
    }

}
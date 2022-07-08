//let list;
//let list2 = new Array();
//var mapContainer = document.getElementById('map'), // 지도를 표시할 div
//    mapOption = {
//        center: new kakao.maps.LatLng(37.63457, 127.33838), // 지도의 중심좌표
//        level: 5, // 지도의 확대 레벨
//        mapTypeId : kakao.maps.MapTypeId.ROADMAP // 지도종류
//    };
//// 지도를 생성한다
//var map = new kakao.maps.Map(mapContainer, mapOption);
//
//let html ="";
//var markers = [];
//kakao.maps.event.addListener(map, 'idle', function() {
//    html = "";
//        console.log(list2);
//        // 지도 영역정보를 얻어옵니다
//        var bounds = map.getBounds();
//
//        // 영역정보의 남서쪽 정보를 얻어옵니다
//        var swLatlng = bounds.getSouthWest();
//    //    console.log(swLatlng);
//    //    console.log(swLatlng.La);
//        // 영역정보의 북동쪽 정보를 얻어옵니다
//        var neLatlng = bounds.getNorthEast();
//    //    console.log(neLatlng);
//         for(let i = 0; i < list2.length; i++) {
//            if(swLatlng.La < list2[i].logt
//                    && swLatlng.Ma < list2[i].lat
//                    && neLatlng.La > list2[i].logt
//                    && neLatlng.Ma > list2[i].lat
//                ) {
//                    // 지도에 마커를 생성하고 표시한다
//                    var marker = new kakao.maps.Marker({
//                        position: new kakao.maps.LatLng(list2[i].lat, list2[i].logt), // 마커의 좌표
//                        map: map // 마커를 표시할 지도 객체
//                    });
//                     // 마커에 클릭 이벤트를 등록한다 (우클릭 : rightclick)
//                    kakao.maps.event.addListener(marker, 'click', function() {
//                        alert(list2[i].name);
//                    });
//                    html +=
//                        '<div class="hospital-box">'+
//                            '<div>'+list2[i].name+'</div>'+
//                            '<div>'+list2[i].raddress+'</div>'+
//                        '</div>';
//                }//if end
//         }
//         $("#sidebar").html( html );
//
//}); //event end
////병원데이터 가져오기
//    for(let i = 1; i <4; i++){
//
//            $.ajax({
//                url : "https://openapi.gg.go.kr/Animalhosptl?" ,
//                data :{"KEY" :"47d367a4e715424e8c25f17ff85a81ea","type":"json","pIndex":i,"pSize": "1000" },
//                async : false,
//                dataType : "json",
//                success: function(re) {
//                        list = re
//                    for(let i = 0; i < re.Animalhosptl[1].row.length; i++){
//                        if(re.Animalhosptl[1].row[i]["BSN_STATE_NM"] == '정상') {
//                                let data = new Object();
//                                 data.code =  re.Animalhosptl[1].row[i]["SIGUN_NM"]
//                                data.name = re.Animalhosptl[1].row[i]["BIZPLC_NM"]
//                                data.lat  = re.Animalhosptl[1].row[i]["REFINE_WGS84_LAT"];
//                                data.logt = re.Animalhosptl[1].row[i]["REFINE_WGS84_LOGT"];
//                                data.raddress = re.Animalhosptl[1].row[i]["REFINE_ROADNM_ADDR"]
//                                list2.push(data);
//                                //mapevevt(list2);
//                        }//if end
//                    }//for end
//
//                }//success end
//            }); //ajax end
////            console.log(markers);
//
//    }//for end
////    markers = [];
//
////var clusterer = new kakao.maps.MarkerClusterer({
////    map: map,
////    markers: markers,
////    gridSize: 60,
////    averageCenter: true,
////    minLevel: 3,
////    disableClickZoom: true,
////});
////console.log( clusterer);
//
////지도이동
//function panTo(lat, logt) {
//    // 이동할 위도 경도 위치를 생성합니다
//    var moveLatLon = new kakao.maps.LatLng(lat, logt);
//
//    // 지도 중심을 부드럽게 이동시킵니다
//    // 만약 이동할 거리가 지도 화면보다 크면 부드러운 효과 없이 이동합니다
//    map.panTo(moveLatLon);
//}
let list;

$.ajax({
    url : "/map",
    async : false,
    success: function(result) {
    console.log(result)
        list = result;
    }
})


var mapContainer = document.getElementById('map'), // 지도를 표시할 div
    mapOption = {
        center: new kakao.maps.LatLng(37.63457, 127.33838), // 지도의 중심좌표
        level: 5, // 지도의 확대 레벨
        mapTypeId : kakao.maps.MapTypeId.ROADMAP // 지도종류
    };
// 지도를 생성한다
var map = new kakao.maps.Map(mapContainer, mapOption);

let html ="";
var markers = [];
kakao.maps.event.addListener(map, 'idle', function() {
    html = "";
        // 지도 영역정보를 얻어옵니다
        var bounds = map.getBounds();

        // 영역정보의 남서쪽 정보를 얻어옵니다
        var swLatlng = bounds.getSouthWest();
    //    console.log(swLatlng);
    //    console.log(swLatlng.La);
        // 영역정보의 북동쪽 정보를 얻어옵니다
        var neLatlng = bounds.getNorthEast();
    //    console.log(neLatlng);
         for(let i = 0; i < list.length; i++) {

            if(swLatlng.La < list[i].logt
                    && swLatlng.Ma < list[i].lat
                    && neLatlng.La > list[i].logt
                    && neLatlng.Ma > list[i].lat
                ) {
                    // 지도에 마커를 생성하고 표시한다
                    var marker = new kakao.maps.Marker({
                        position: new kakao.maps.LatLng(list[i].lat, list[i].logt), // 마커의 좌표
                        map: map // 마커를 표시할 지도 객체
                    });
                     // 마커에 클릭 이벤트를 등록한다 (우클릭 : rightclick)
                    kakao.maps.event.addListener(marker, 'click', function() {
                      $.ajax({
                           url: "/map/view",
                           method: "GET",
                           data: {"hname":list[i].name , "hdate": list[i].opendate, "hcity" : list[i].city},
                            success: function(re){
                                 alert(re);
                                 location.href = "/map/infopage";
                            }
                       });

                    });
                    html +=
                        '<div class="hospital-box" onclick="hview('+i+')" >'+
                            '<div >'+list[i].name+'</div>'+
                            '<div>'+list[i].addr+'</div>'+
                        '</div>';
                }//if end
         }
         $("#sidebar").html( html );
});

function panTo(lat, logt) {
    // 이동할 위도 경도 위치를 생성합니다
    var moveLatLon = new kakao.maps.LatLng(lat, logt);

    // 지도 중심을 부드럽게 이동시킵니다
    // 만약 이동할 거리가 지도 화면보다 크면 부드러운 효과 없이 이동합니다
    map.panTo(moveLatLon);
}
//let city;
//function hsave(i) {
//city = list[i].city;
//name = list[i].name;
//hview(city,name);
//}

let searchresult;
function search(){
    let keyword = $("#searchbar").val();
    $.ajax({
        url: "/map/search",
        data: {"keyword" : keyword},
        success: function(result) {
            searchresult = result;
            let searchlist= "";
            if(result.length == 0){
                searchlist = '<div>일치하는 병원이 없습니다.</div>'
            }else {
                for (let i = 0; i<result.length; i++){
                    hname = result[i].name;
                    hdate = result[i].opendate;
                    hcity = result[i].city;
                    searchlist +=
                        '<div Onclick="infopage('+i+')" style="cursor:pointer" >'+result[i].city+' '+result[i].name+'</div>';
                }
            }
            $("#searchlist").html(searchlist);
        }
    });
}

function hview(i){

    $.ajax({
       url: "/map/view",
       method: "GET",
       data: {"hname":list[i].name , "hdate": list[i].opendate, "hcity" : list[i].city},
        success: function(re){
             alert(re);
             location.href = "map/infopage";
        }
   });
}

function infopage(i){
    alert(searchresult[i].name+"hdate"+searchresult[i].opendate+"hcity"+searchresult[i].city);
    $.ajax({
        url: "/map/view",
        method: "GET",
        data: {"hname":searchresult[i].name , "hdate": searchresult[i].opendate, "hcity" : searchresult[i].city},
        success: function(re){
        location.href = "/map/infopage";
        }
    });

}
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
});


var mapContainer = document.getElementById('map'), // 지도를 표시할 div
    mapOption = {
        center: new kakao.maps.LatLng(37.63457, 127.33838), // 지도의 중심좌표
        level: 6, // 지도의 확대 레벨
        mapTypeId : kakao.maps.MapTypeId.ROADMAP // 지도종류
    };
// 지도를 생성한다
var map = new kakao.maps.Map(mapContainer, mapOption);

var clusterer = new kakao.maps.MarkerClusterer({
            map: map,
            averageCenter: true, // 클러스터에 포함된 마커들의 평균 위치를 클러스터 마커 위치로 설정
            minLevel: 10, // 클러스터 할 최소 지도 레벨
            styles: [{
                width : '53px', height : '52px',
                background: 'url(cluster.png) no-repeat',
                color: '#000',
                textAlign: 'center',
                lineHeight: '54px'
            }]
    });

kakao.maps.event.addListener(map, 'idle', function() {


let html ="";
clusterer.clear(); // 클러스터 클리어


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

                       let marker = new kakao.maps.Marker({
                       position : new kakao.maps.LatLng(list[i].lat, list[i].logt),
                       map : map
                          });




                     // 마커에 클릭 이벤트를 등록한다 (우클릭 : rightclick)
                    kakao.maps.event.addListener(marker, 'click', function() {
                        alert(list[i].name);
                    });
                    html +=
                        '<div class="hospital-box">'+
                            '<div>'+list[i].name+'</div>'+
                            '<div>'+list[i].addr+'</div>'+
                        '</div>';


            clusterer.addMarker(marker);
         } //if end
         }
         console.log( clusterer );
         $("#sidebar").html( html );



});

function panTo(lat, logt) {
    // 이동할 위도 경도 위치를 생성합니다
    var moveLatLon = new kakao.maps.LatLng(lat, logt);

    // 지도 중심을 부드럽게 이동시킵니다
    // 만약 이동할 거리가 지도 화면보다 크면 부드러운 효과 없이 이동합니다
    map.panTo(moveLatLon);
}

let hname;
let hdate;
function search(){
    let keyword = $("#searchbar").val();
    $.ajax({
        url: "/map/search",
        data: {"keyword" : keyword},
        success: function(result) {
            let searchlist= "";
            if(result.length == 0){
                searchlist = '<div>일치하는 병원이 없습니다.</div>'
            }else {
                for (let i = 0; i<result.length; i++){
                    hname = result[i].name;
                    hdate = result[i].opendate;
                    searchlist +=
                        '<div Onclick="infopage()" style="cursor:pointer" >'+result[i].city+' '+result[i].name+'</div>';
                }
            }
            $("#searchlist").html(searchlist);
        }
    });
}
function infopage(){
    console.log(hname+" "+hdate);
    $.ajax({
        url: "/map/view",
        method: "GET",
        data: {"hname":hname , "hdate": hdate},
         success: function(re){
           location.href = "/map/infopage";
         }
    });

}
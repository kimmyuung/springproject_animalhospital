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
        level: 5, // 지도의 확대 레벨
        mapTypeId : kakao.maps.MapTypeId.ROADMAP // 지도종류
    };
// 지도를 생성한다
var map = new kakao.maps.Map(mapContainer, mapOption);

var clusterer = new kakao.maps.MarkerClusterer({
            map: map,
            averageCenter: true, // 클러스터에 포함된 마커들의 평균 위치를 클러스터 마커 위치로 설정
            minLevel: 8, // 클러스터 할 최소 지도 레벨
            styles: [{
                width : '53px', height : '52px',
                background: 'url(cluster.png) no-repeat',
                color: '#000',
                textAlign: 'center',
                lineHeight: '54px'
            }]
    });


let html ="";
var markers = [];
kakao.maps.event.addListener(map, 'idle', function() {
    let j =0;
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

                    if(j<10){
                        html +=
                            '<div class="hospital-box" onclick="hview('+i+')" >'+
                                '<div >'+list[i].name+'</div>'+
                                '<div>'+list[i].addr+'</div>'+
                            '</div>';
                        j++;
                    }
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
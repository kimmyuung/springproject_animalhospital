
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
let hcity;

function search(){
    let keyword = $("#searchbar").val();
    if(keyword == "") {
    alert("검색어를 입력해주세요"); return;

    }
    if(keyword == "동물병원") {
    alert("검색되는 숫자가 너무 많습니다. 다른 검색어로 검색해주세요"); return;
    $("#searchbar").val("");
    }
    var pr = /^[가-힣]{3,}$/      //한글 3글자 이상
    if(pr.test(keyword)) {
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
                        hcity = result[i].city;
                        searchlist +=
                            '<div Onclick="infopage()" style="cursor:pointer" >'+result[i].city+' '+result[i].name+'</div>';
                    }
                }
                $("#searchlist").html(searchlist);
                $("#searchbar").val("");
            }
        });
    }
    else {
    alert("검색어는 한글로 최소 3자 이상 입력해야 합니다.");
    $("#searchbar").val("");
    return;
    }


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

function infopage(){
    console.log(hname+" "+hdate);
    $.ajax({
        url: "/map/view",
        method: "GET",
        data: {"hname":hname , "hdate": hdate ,"hcity" : hcity},
         success: function(re){
           location.href = "/map/infopage";
         }
    });

}







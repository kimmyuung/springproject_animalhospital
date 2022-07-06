
let list;
$.ajax({
    url : "/map",
    async : false,
    success: function(result) {
//    console.log(result)
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
                        function panTo(list[i].lat, list[i].logt) {
                            // 이동할 위도 경도 위치를 생성합니다
                            var moveLatLon = new kakao.maps.LatLng(list[i].lat, list[i].logt);

                            // 지도 중심을 부드럽게 이동시킵니다
                            // 만약 이동할 거리가 지도 화면보다 크면 부드러운 효과 없이 이동합니다
                            map.panTo(moveLatLon);
                        }
                        alert(list[i].name);
                    });

                    html +=
                        '<div class="hospital-box">'+
                            '<div>'+list[i].name+'</div>'+
                            '<div>'+list[i].addr+'</div>'+
                        '</div>';
                }//if end
         }
         $("#sidebar").html( html );
});


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
        url: "/map/infopage",
        method: "GET",
        data: {"hname":hname , "hdate": hdate},
         success: function(re){
           location.href = "/map/infopage";
         }
    });
}
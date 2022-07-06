let list;

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
//병원데이터 가져오기
    for(let i = 1; i <4; i++){


            $.ajax({
                url : "https://openapi.gg.go.kr/Animalhosptl?" ,
                data :{"KEY" :"47d367a4e715424e8c25f17ff85a81ea","type":"json","pIndex":i,"pSize": "1000" },
                dataType : "json",
                success: function(re) {
                        list = re
                    for(let i = 0; i < re.Animalhosptl[1].row.length; i++){
                        let hname = re.Animalhosptl[1].row[i]["BIZPLC_NM"];

                        let lat = re.Animalhosptl[1].row[i]["REFINE_WGS84_LAT"];
                        let logt = re.Animalhosptl[1].row[i]["REFINE_WGS84_LOGT"];
                        if(re.Animalhosptl[1].row[i]["BSN_STATE_NM"] == "정상"
                            && swLatlng.La < logt
                            && swLatlng.Ma < lat
                            && neLatlng.La > logt
                            && neLatlng.Ma > lat
                        ) {

                            // 지도에 마커를 생성하고 표시한다
                            var marker = new kakao.maps.Marker({
                                position: new kakao.maps.LatLng(lat, logt), // 마커의 좌표
                                map: map // 마커를 표시할 지도 객체
                            });
//                            if(markers[0] == null){
//                                markers.push(marker);
//                            }else{
//                                for (var j = 0; j < markers.length; j++) {
//                                    if (markers[j].n.La != marker.n.La && markers[j].n.Ma != marker.n.Ma ) {
//                                        console
//                                        markers.push(marker);
//                                    }
//                                }
//                            }
                             // 마커에 클릭 이벤트를 등록한다 (우클릭 : rightclick)
                            kakao.maps.event.addListener(marker, 'click', function() {
                                markerclick(hname);

                            });
                            html +=
                                '<div class="hospital-box">'+
                                    '<div>'+hname+'</div>'+
                                    '<div>'+re.Animalhosptl[1].row[i]["REFINE_ROADNM_ADDR"]+'</div>'+
                                '</div>';
                        }//if end
                    }//for end
                    //사이드바에 html 추가
                    $("#sidebar").html( html );
                    ctest();

                        // 데이터를 가져오기 위해 jQuery를 사용합니다
                        // 데이터를 가져와 마커를 생성하고 클러스터러 객체에 넘겨줍니다
//                        $.get(re, function(data) {
//                            // 데이터에서 좌표 값을 가지고 마커를 표시합니다
//                            // 마커 클러스터러로 관리할 마커 객체는 생성할 때 지도 객체를 설정하지 않습니다
//                            var markers = $(data.positions).map(function(i, position) {
//                                return new kakao.maps.Marker({
//                                    position : new kakao.maps.LatLng(position.lat, position.lng)
//                                });
//                            });

                            // 클러스터러에 마커들을 추가합니다
//                            clusterer.addMarkers(markers);
//                        });
                }//success end
            }); //ajax end
//            console.log(markers);

    }//for end
//    markers = [];
});
//var clusterer = new kakao.maps.MarkerClusterer({
//    map: map,
//    markers: markers,
//    gridSize: 60,
//    averageCenter: true,
//    minLevel: 3,
//    disableClickZoom: true,
//});
//console.log( clusterer);

//지도이동
function panTo(lat, logt) {
    // 이동할 위도 경도 위치를 생성합니다
    var moveLatLon = new kakao.maps.LatLng(lat, logt);

    // 지도 중심을 부드럽게 이동시킵니다
    // 만약 이동할 거리가 지도 화면보다 크면 부드러운 효과 없이 이동합니다
    map.panTo(moveLatLon);
}
function markerclick(hname){
    alert(hname);
}

var clusterer = new kakao.maps.MarkerClusterer({
    map: map,
    markers: markers,
    gridSize: 60,
    averageCenter: true,
    minLevel: 6,
    disableClickZoom: true,
    styles: [{
        width : '53px', height : '52px',
        background: '#46a6fa',
        color: '#fff',
        textAlign: 'center',
        lineHeight: '54px'
    }]
});

function mdelete() {
if(confirm('회원탈퇴를 정말 하시겠습니까?')){

    $.ajax({
        url : '/member/delete',
        method : 'DELETE',
        success : function(re) {
                if(re == true) {
                    alert("회원 탈퇴가 완료되었습니다.")
                    location.href = '/member/logout';
                }
                else { alert("오류 발생"); }
            }
        });
    }
}
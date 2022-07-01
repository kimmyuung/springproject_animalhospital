
var mapContainer = document.getElementById('map'), // 지도를 표시할 div
    mapOption = {
        center: new kakao.maps.LatLng(37.63457, 127.33838), // 지도의 중심좌표
        level: 5, // 지도의 확대 레벨
        mapTypeId : kakao.maps.MapTypeId.ROADMAP // 지도종류
    };

// 지도를 생성한다
var map = new kakao.maps.Map(mapContainer, mapOption);

for(let i = 1; i <4; i++){
        $.ajax({
                url : "https://openapi.gg.go.kr/Animalhosptl?" ,
                data :{"KEY" :"47d367a4e715424e8c25f17ff85a81ea","type":"json","pIndex":i,"pSize": "1000" },
                dataType : "json",
                success: function(re) {

                    for(let i = 0; i < re.Animalhosptl[1].row.length; i++){
                        if(re.Animalhosptl[1].row[i]["BSN_STATE_NM"] == "정상") {
                            let hname = re.Animalhosptl[1].row[i]["BIZPLC_NM"];
                            console.log(hname);

                            let lat = re.Animalhosptl[1].row[i]["REFINE_WGS84_LAT"];
                            let logt = re.Animalhosptl[1].row[i]["REFINE_WGS84_LOGT"];


                            //클러스터[ 마커 집합 ]  변수
                            var clusterer = new kakao.maps.MarkerClusterer({
                                map: map, // 마커들을 클러스터로 관리하고 표시할 지도 객체
                                averageCenter: true, // 클러스터에 포함된 마커들의 평균 위치를 클러스터 마커 위치로 설정
                                minLevel: 6, // 클러스터 할 최소 지도 레벨
                                disableClickZoom: true // 클러스터 마커를 클릭했을 때 지도가 확대되지 않도록 설정한다
                            });


                        // 지도에 마커를 생성하고 표시한다
                        var marker = new kakao.maps.Marker({
                            position: new kakao.maps.LatLng(lat, logt), // 마커의 좌표
                            map: map // 마커를 표시할 지도 객체
                        });


                            // 마커에 클릭 이벤트를 등록한다 (우클릭 : rightclick)
                            kakao.maps.event.addListener(marker, 'click', function() {
                                alert(hname);
                            });

                        }
                    }
        //        let x = re["X_CRDNT_VL"]
        //        for(let i = 0  i<re.length; i++) {
        //
        //        }
                console.log(typeof re);
                console.log( re );
                }
            });
    }
//클러스터[ 마커 집합 ]  변수
var clusterer = new kakao.maps.MarkerClusterer({
    map: map, // 마커들을 클러스터로 관리하고 표시할 지도 객체
    averageCenter: true, // 클러스터에 포함된 마커들의 평균 위치를 클러스터 마커 위치로 설정
    minLevel: 6, // 클러스터 할 최소 지도 레벨
    disableClickZoom: true // 클러스터 마커를 클릭했을 때 지도가 확대되지 않도록 설정한다
});

// 지도에 마커를 생성하고 표시한다
var marker = new kakao.maps.Marker({
    position: new kakao.maps.LatLng(37.63644, 127.34138), // 마커의 좌표
    map: map // 마커를 표시할 지도 객체
});

// 마커에 클릭 이벤트를 등록한다 (우클릭 : rightclick)
kakao.maps.event.addListener(marker, 'click', function() {
    alert('마커를 클릭했습니다!');
});
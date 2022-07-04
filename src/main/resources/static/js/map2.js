let list;
let list2 = new Array();


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
//병원데이터 가져오기
    for(let i = 1; i <4; i++){

            $.ajax({
                url : "https://openapi.gg.go.kr/Animalhosptl?" ,
                data :{"KEY" :"47d367a4e715424e8c25f17ff85a81ea","type":"json","pIndex":i,"pSize": "1000" },
                dataType : "json",
                async : false,
                success: function(re) {
                        list = re
                        console.log(list)
                    for(let i = 0; i < re.Animalhosptl[1].row.length; i++){
                        if(re.Animalhosptl[1].row[i]["BSN_STATE_NM"] == '정상') {
                            let hname = re.Animalhosptl[1].row[i]["BIZPLC_NM"];
                            let hcode = re.Animalhosptl[1].row[i]["SIGUN_NM"];
                            let lat = re.Animalhosptl[1].row[i]["REFINE_WGS84_LAT"];
                            let logt = re.Animalhosptl[1].row[i]["REFINE_WGS84_LOGT"];

//                            let cjson = {
//                            name : re.Animalhosptl[1].row[i]["BIZPLC_NM"],
//                            ccode : re.Animalhosptl[1].row[i]["SIGUN_NM"]
//
//                            } // 이거 밖으로 빼서  아래 아작스에 태워서 보내야됨
                                let data = new Object();
                                 data.code =  re.Animalhosptl[1].row[i]["SIGUN_NM"]
                                data.name = re.Animalhosptl[1].row[i]["BIZPLC_NM"]

                                list2.push(data);


                                // 지도에 마커를 생성하고 표시한다
                                var marker = new kakao.maps.Marker({
                                    position: new kakao.maps.LatLng(lat, logt), // 마커의 좌표
                                    map: map // 마커를 표시할 지도 객체
                                });
                                markers.push(marker);
                                // 마커에 클릭 이벤트를 등록한다 (우클릭 : rightclick)
                                kakao.maps.event.addListener(marker, 'click', function() {
                                    alert(hname);
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

                }//success end
            }); //ajax end

    }//for end
    console.log(list2);


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

                              $.ajax({
                                url : "/getlist",
                                data : {"codenamelist" : JSON.stringify(list2)},
                               async : false,
                                success: function(res) {
                                    //alert("데이터보내기")

                                    console.log(typeof(res))
                                }
                            })

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
let current_page = 0;
let updatebno = 0;
getitemlist(0);

function getitemlist(page) {

this.current_page = page;


$.ajax({
    url : '/member/getitemlist',
    data : {"page" : page },
    success : function(json) {

    let html = "<tr> <th> 상품 이름 </th> <th> 상품 설명 </th> <th> 등록일 </th> <th> 가격 </th><th>판매자</th> <th>비고</th> </tr>";
    let html2 = '';
    console.log(json);


    if(json.itemlist.length == 0) {
                                html += '<tr>' +
                                '<td colspan="5"> 검색 결과가 존재하지 않습니다. </td> ' +
                                '</tr>';
                                }else{
       for(let i = 0; i < json.itemlist.length; i++) {
            html += '<tr>' +
            '<td>'+ json.itemlist[i].stitle +'</td>' +
            '<td>'+ json.itemlist[i].scontent + '</td>' +
            '<td>'+ json.itemlist[i].bdate + '</td>' +
            '<td>'+ json.itemlist[i].sprice + '</td>' +
            '<td>'+ json.itemlist[i].mid + '</td>' +
            '<td> <button type="button" onclick="itemview('+json.itemlist[i].sno+')">상품 상세보기</button>' +
             '</td></tr>';
             }
               ////////////////////////////////////// 이전 /////////////////////////////////////////////
             if( page == 0 )
              {
              html2 += ' <div class="page-item col-md-2"><a class="page-link" onclick="getitemlist('+ (page)+')">Previous</a></div>' ;
              } else{
                 html2 +=
                   ' <div class="page-item col-md-2"><a class="page-link" onclick="getitemlist('+(page-1)+')">Previous</a></div>' ;
                    }
                               ////////////////////////////////////// 페이징 ////////////////////////////////////////////
              for( let i = json.itemlist[0].startbtn ; i <=  json.itemlist[0].endbtn; i++) {
                    html2 += '<div class="page-item col-md-2"><button class="btn btn-primary mx-1" onclick="getitemlist(' + i +')">'
                    + (i) + '</button></div>';
                     }
                             ////////////////////////////////////// 이후 버튼 //////////////////////////////////////////
                             if(page == json.itemlist[0].totalpage -1){
                             html2 +=
                              ' <div class="page-item col-md-2"><a class="page-link" onclick="getitemlist('+ (page) +')">Next</a></div>' ;
                             }
                             else {
                              html2 +=
                              ' <div class="page-item col-md-2"><a class="page-link" onclick="getitemlist('+(page+1)+')">Next</a></div>' ;
                              }


        }
        $("#itemtable").html(html);
        $("#btnbox").html( html2 );// 페이징버튼 html 넣기
    }
});

}

function itemsave() {

if($("#stitle").val() == "") {alert("상품 제목을 입력해주세요"); return;}
if($("#scontent").val() == "") {alert("상품에 대한 설명을 입력해주세요"); return;}
let form = $("#saveform")[0]; // [0] : 폼내 입력 데이터 //
    //[0]을 안하면 데이터 + 설정값까지 같이 보내지게 됨
    let formData = new FormData(form);

$.ajax({
            url : '/member/itemsave',
            type: "POST",
            data : formData,
            contentType : false, // 첨부파일 전송시 사용되는 속성
            processData : false,
            success : function(re) {
            console.log(re);
           if(re == true) {alert("등록 성공"); getitemlist(0);
           $("#sprcie").val('');
           $("#stitle").val('');
           $("#scontent").val('');
           }
           else { alert("등록 실패");}
           }
    });

}

function itemview(sno) {

location.href = '/member/itemview?sno='+ sno;
}
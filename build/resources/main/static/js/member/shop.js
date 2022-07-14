let current_page = 0;
let updatebno = 0;
getitem(0);

function getitem(page) {

this.current_page = page;


$.ajax({
    url : '/member/getitem',
    data : {"page" : page },
    success : function(json) {
    let html = "<tr> <th> 상품 이름 </th> <th> 상품 설명 </th><th> 가격 </th> <th>판매자</th> <th>비고</th> </tr>";
    let html2 = '';
    console.log(json);
    if(json.data.length == 0) {
                                html += '<tr>' +
                                '<td colspan="5"> 검색 결과가 존재하지 않습니다. </td> ' +
                                '</tr>';
                                }else{
       for(let i = 0; i < json.data.length; i++) {
            html += '<tr>' +
            '<td>'+ json.data[i]["btitle"] +'</td>' +
            '<td>'+ json.data[i]["bcontent"] + '</td>' +
            '<td>'+ json.data[i]["bindate"] + '</td>' +
            '<td> <button type="button" onclick="bnosave('+json.data[i]["bno"]+')" data-bs-toggle="modal" data-bs-target="#myModal2")">상품 상세보기</button>' +
             '</td></tr>';
             }
               ////////////////////////////////////// 이전 /////////////////////////////////////////////
             if( page == 0 )
              {
              html2 += ' <div class="page-item col-md-2"><a class="page-link" onclick="getitem('+ (page)+')">Previous</a></div>' ;
              } else{
                 html2 +=
                   ' <div class="page-item col-md-2"><a class="page-link" onclick="getitem('+(page-1)+')">Previous</a></div>' ;
                    }
                               ////////////////////////////////////// 페이징 ////////////////////////////////////////////
              for( let i = json.startbtn-1 ; i <= json.endbtn - 1; i++) {
                    html2 += '<div class="page-item col-md-2"><button class="btn btn-primary mx-1" onclick="getitem(' + i +')">'
                    + (i+1) + '</button></div>';
                     }
                             ////////////////////////////////////// 이후 버튼 //////////////////////////////////////////
                             if(page == json.totalpage-1){
                             html2 +=
                              ' <div class="page-item col-md-2"><a class="page-link" onclick="getitem('+ (page) +')">Next</a></div>' ;
                             }
                             else {
                              html2 +=
                              ' <div class="page-item col-md-2"><a class="page-link" onclick="getitem('+(page+1)+')">Next</a></div>' ;
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
           if(re == true) {alert("등록 성공"); getnotice(0);}
           else { alert("등록 실패");}
           }
    });
    $("#btitle").val("");
    $("#bcontent").val("");
}


function bnosave(bno) {
updatebno = bno;
console.log(updatebno);
}
function itemupdate(updatebno) {
console.log(updatebno);
$.ajax({
            url : '/member/itemupdate',
            type: "PUT",
            data : {"sno" : updatebno, "stitle" : $("#stitle2").val(),"scontent" : $("#scontent2").val()} ,
            success : function(re) {
            console.log(re);
           if(re == true) {alert("수정 성공"); getitem(0);}
           else { alert("수정 실패");}
           }
    });
        $("#btitle2").val() = "";
        $("#bcontent2").val() = "";
}

function itemdelete(sno) {
if(confirm("정말 상품을 삭제하시겠습니까?")) {
$.ajax({
            url : '/member/deleteitem',
            type: "DELETE",
            data : {"sno" : sno } ,
            success : function(re) {
            console.log(re);
           if(re == true) {alert("삭제 성공"); getitem(0);}
           else { alert("삭제 실패");}
           }
    });
}
}
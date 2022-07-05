let current_page = 0;
let updatebno = 0;
getnotice(0);

function getnotice(page) {

this.current_page = page;

$.ajax({
    url : '/board/getnotice',
    type : 'post',
    data : {"page" : current_page },
    success : function(json) {
    let html = "<tr> <th> 제목 </th> <th> 내용 </th><th> 작성날짜 </th><th>비고</th> </tr>";
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
            '<td> <button type="button" onclick="bnosave('+json.data[i]["bno"]+')" data-bs-toggle="modal" data-bs-target="#myModal2")">공지사항 수정</button>' +
                 '<button type="button" onclick="noticedelete('+json.data[i]["bno"]+')">공지사항 삭제</button>' +
             + '</td></tr>';
             }
               ////////////////////////////////////// 이전 /////////////////////////////////////////////
             if( page == 0 )
              {
              html2 += ' <li class="page-item"><a class="page-link" onclick="getnotice('+ (page)+')">Previous</a></li>' ;
              } else{
                 html2 +=
                   ' <li class="page-item"><a class="page-link" onclick="getnotice('+(page-1)+')">Previous</a></li>' ;
                    }
                               ////////////////////////////////////// 페이징 ////////////////////////////////////////////
              for( let i = json.startbtn-1 ; i <= json.endbtn - 1; i++) {
                    html2 += '<li class="page-item"><button class="btn btn-primary mx-1" onclick="getnotice(' + i +')">'
                    + (i+1) + '</button></li>';
                     }
                             ////////////////////////////////////// 이후 버튼 //////////////////////////////////////////
                             if(page == json.totalpage-1){
                             html2 +=
                              ' <li class="page-item"><a class="page-link" onclick="getnotice('+ (page) +')">Next</a></li>' ;
                             }
                             else {
                              html2 +=
                              ' <li class="page-item"><a class="page-link" onclick="getnotice('+(page+1)+')">Next</a></li>' ;
                              }


        }
        $("#noticetable").html(html);
        $("#pagebtnbox").html( html2 );// 페이징버튼 html 넣기
    }
});

}

function noticesave() {

if($("#btitle").val() == "") {alert("공지사항 제목을 입력해주세요"); return;}
if($("#bcontent").val() == "") {alert("공지사항 내용을 입력해주세요"); return;}
$.ajax({
            url : '/admin/noticesave',
            type: "POST",
            data : {"btitle" : $("#btitle").val(),"bcontent" : $("#bcontent").val()} ,
            success : function(re) {
            console.log(re);
           if(re == true) {alert("등록 성공"); getnotice(0);}
           else { alert("등록 실패");}
           }
    });
}


function bnosave(bno) {
updatebno = bno;
console.log(updatebno);
}
function noticeupdate() {
console.log(updatebno);
$.ajax({
            url : '/admin/updatenotice',
            type: "PUT",
            data : {"bno" : updatebno, "btitle" : $("#btitle2").val(),"bcontent" : $("#bcontent2").val()} ,
            success : function(re) {
            console.log(re);
           if(re == true) {alert("수정 성공"); getnotice(0);}
           else { alert("수정 실패");}
           }
    });
}

function noticedelete(bno) {
if(confirm("정말 공지사항을 삭제하시겠습니까?")) {
$.ajax({
            url : '/admin/deletenotice',
            type: "DELETE",
            data : {"bno" : bno } ,
            success : function(re) {
            console.log(re);
           if(re == true) {alert("삭제 성공"); getnotice(0);}
           else { alert("삭제 실패");}
           }
    });
}
}
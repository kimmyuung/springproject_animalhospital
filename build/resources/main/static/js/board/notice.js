let page;

getnotice();

function getnotice() {
if(page == null) {
page = 0;
}
page = 0;
$.ajax({
    url : '/board/getnotice',
    type : 'post',
    data : {"page" : page},
    success : function(json) {
    let html = "<tr> <th> 제목 </th> <th> 내용 </th><th>비고</th> </tr>";
    console.log(json);
    alert(json);
       for(let i = 0; i < json.length; i++) {
            html += '<tr>' +
            '<td>'+ json[i]["btitle"] +'</td>' +
            '<td>'+ json[i]["bcontent"] + '</td>' +
            '<td> <button type="button" onclick="noticeupdate('+json[i]["bno"]+')">공지사항 수정</button>' +
                 '<button type="button" onclick="noticedelete('+json[i]["bno"]+')">공지사항 삭제</button>' +
             + '</td></tr>';
        }
        $("#noticetable").html(html);
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
           if(re == true) {alert("등록 성공");}
           else { alert("등록 실패");}
           }
    });
}

function noticeupdate(bno) {
alert(bno);
}

function noticedelete(bno) {alert(bno);}
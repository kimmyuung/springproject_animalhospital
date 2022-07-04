getnotice();

function getnotice() {

let page = 0;

$.ajax({
    url : '/board/getnotice',
    type : 'post',
    data : {"page" : page},
    dataType : "json",
    success : function(json) {
    console.log(json);

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


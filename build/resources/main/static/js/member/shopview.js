var sno = getParameterByName('sno');
getitem(sno);
idcheck(sno);
likecheck();
let pass;
let seller;
function getitem(sno) {
$.ajax({
url : '/member/getitem',
data : {"sno" : sno},
success : function(re) {
    seller = re.mid;
    console.log(re);
    let html = '';
    html += '<div>상품 이름 : '+re.btitle+'</div>';
    html += '<div>상품 설명 : '+re.bcontent+'</div>';
    html += '<div>상품 이미지 : '+re.bimglist+'</div>';
     html += '<div>판매자 : '+re.mid+'</div>';
    html += '<div>상품 가격 : '+re.price+'</div>';
    if(! re.status) {
    html += '<div>상품 상태 : 판매 중</div>';
    }else {
      html += '<div>상품 상태 : 판매 완료 </div>';
    }
    $("#itembox").html(html);
}

});
}





function getParameterByName(sno) {
    sno = sno.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
    var regex = new RegExp("[\\?&]" + sno + "=([^&#]*)"),
        results = regex.exec(location.search);
    return results === null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
}

function likesave() {

$.ajax({
    url : '/member/likesave',
    data : {"sno" : sno},
    success : function(re) {
    if(re == 1) {
    alert("관심 상품 추가");
    likecheck();
    }
    else if(re == 2){
    alert("관심 상품 삭제");
    likecheck();
    }
    else if(re == 3){
    alert("로그인 후에 관심 상품 기능 이용이 가능합니다.");
    }
    else if(re == 4){
    alert("프로그램 오류 : 관리자에게 문의");
    }
    }
});
}
function likecheck() {
$.ajax({
    url : '/member/likecheck',
    data : {"sno" : sno},
    success : function(re) {
    if(re == 1) {
    $("#likesave").css("display", "none");
    $("#unlikesave").css("display", "block");
    }
    else if(re == 2){
    $("#likesave").css("display", "block");
    $("#unlikesave").css("display", "none");
    }
    else if(re == 3 || re == 4){ }
    }
});

}



function itemdelete() {


if(confirm("정말 상품을 삭제하시겠습니까?")) {

$.ajax({
            url : '/member/deleteitem',
            type: "DELETE",
            data : {"sno" : sno } ,
            success : function(re) {
            console.log(re);
           if(re == true) {alert("삭제 성공"); location.href = '/member/shop';}
           else { alert("삭제 실패");}
           }
    });
}


}

function itemupdate() {

$("#sno").val(sno);


let form = $("#saveform")[0]; // [0] : 폼내 입력 데이터 //
    //[0]을 안하면 데이터 + 설정값까지 같이 보내지게 됨
    let formData = new FormData(form);
console.log(formData);
$.ajax({
            url : '/member/itemupdate',
            type: "PUT",
            data : formData,
            contentType : false, // 첨부파일 전송시 사용되는 속성
            processData : false,
            success : function(re) {
            console.log(re);
           if(re == true) {alert("수정 성공");
                    $("#stitle").val('')
                    $("#scontent").val('')
                    $("#sprcie").val('')
                    $("#simg").val('')
            getitem(sno);}
           else { alert("수정 실패");}
           }
    });

}

function idcheck(sno) {
$.ajax({
            url : '/member/idcheck',
            data : {"sno" : sno } ,
            success : function(re) {
           if(re == 1) {
           $("#itemupdate").css("display", "block");
           $("#itemdelete").css("display", "block");
           pass = true;
           }
           else if(re == 2) {
            $("#itemupdate").css("display", "none");
            $("#itemdelete").css("display", "none");
            pass = true;
           }
           else if(re == 3 || re == 4) {pass = false;}
           }
    });
}

$(document).ready(function(){

      let mid ="";
        $.ajax({
            url: '/member/getmid',
            async : false,
            success: function(result){
                mid= result;
                console.log(mid);
            }
        });

           $("#sendmsg").click(function(){
             if(pass) {
                   let msg = $("#msg").val();
                   if(msg == '') {
                   alert("메시지 내용을 입력해주세요");
                   return;
                   }
                      let from = mid.replace(/\n|\r|\s*/g, "");
                      let to = seller;
                      alert(from);
                      let jsonmsg = {
                      "from" : from,
                      "to" : to ,
                      "msg" :msg,
                      "type" : 1
                      }
                      console.log(jsonmsg);
                      send(jsonmsg);
                      msg = "";
                   }
                   else {
                   alert("로그인 후에 이용이 가능합니다."); return;
                   }
            });

            let msgwebsocket = new WebSocket("ws://ec2-3-37-55-156.ap-northeast-2.compute.amazonaws.com:8082/ws/message/"+mid);
            msgwebsocket.onopen = onOpen;
            msgwebsocket.onclose = onClose;
            msgwebsocket.onmessage = onMessage;

             function onOpen() { }
             function onClose() { }
             function onMessage() { }
             function send(jsonmsg){  msgwebsocket.send(JSON.stringify(jsonmsg));  }
    });


gettomsglist();
let msg;
function gettomsglist(){
    $.ajax({
        url: '/member/gettomsglist',
        success: function(object){
            msg = object;
            let html =
                    '<tr>'+
                        '<th>보낸사람</th><th>내용</th><th>받은 날짜/시간</th>'+
                    '</tr>';
            if(object.length == 0){
                html +=
                    '<tr>'+
                        '<th colspan="3">받은 쪽지가 없습니다.</th>'+
                    '</tr>';
            }
            for(let i=0; i<object.length; i++){

                let color ="";
                let msgtitle ="";
                if(object[i].msg.length > 20 ){
                   msgtitle = object[i].msg.substr(0, 20)+"..."
                }else{
                    msgtitle = object[i].msg
                }
                html +=
                    '<tr data-bs-toggle="modal" data-bs-target="#exampleModal" href="#exampleModal">'+
                        '<td >'+object[i].from+'</td><td onclick="msgread('+i+')" >'+msgtitle+'</td><td>'+object[i].date+'</td>'+
                    '</tr>';
            }
        $("#tomsgtable").html(html);
        }
    });
}
let fromid = "";
function msgread( i ){
    fromid = msg[i].from;
    let html =
        '<div><span>보낸사람</span><span> '+msg[i].from+'</span></div>'+
        '<div><span>받은시간</span><span> '+msg[i].date+'</span></div>'+
        '<hr>'+
        '<div>'+msg[i].msg+'</div>';

    $("#msgcontent").html(html)

}

let mid = "";
$.ajax({
    url: '/member/getmid',
    success: function(result){
        mid = result+"";
    }
});



$(document).ready(function(){

     $("#answer").click(function(){
        let msg = $("#msginput").val();
        let to = fromid;
        let jsonmsg = {
            "from" : mid,
            "to" : to ,
            "msg" : msg
        }
        console.log(jsonmsg);
        send(jsonmsg);
        msg = "";
        $(".btn-close").trigger("click");
    });

   // 1. 웹소켓 객체 생성
   let msgwebsocket = new WebSocket("ws://localhost:8082/ws/answer/"+mid);

   // 2. 웹소켓 객체에 구현된 메소드 저장
   msgwebsocket.onopen = onOpen;
   msgwebsocket.onclose = onClose;
   msgwebsocket.onmessage = onMessage;

   // 3. 각 메소드 구현
   function onOpen(){
        alert("입장");
   }

   function onClose(){
        alert("퇴장");
   }

   function onMessage(){
        alert("메시지");
   }

   function send(jsonmsg){
        msgwebsocket.send(JSON.stringify(jsonmsg));
   }

});
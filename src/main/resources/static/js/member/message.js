//쪽지 메소드
$(document).ready(function(){
    let mid = "";
    let hname = "";
    let hdate = "";

    $.ajax({
        url: '/member/getinfo',
        success: function(result){
             mid= result;
             hname = result.hname;
             hdate = result.hdate;
            console.log(result);
        }
    });

     $("#sendmsg").click(function(){

        let from = mid;
        let to = info.hname+info.hdate;
        let msg = $("#msginput").val();
        let jsonmsg = {
            "from" : mid,
            "to" : hname+hdate ,
            "msg" :msg,
            "type" : "2"
        }
        console.log(jsonmsg);
        send(jsonmsg);
        msg = "";
        $("#close").trigger("click");
    });

   // 1. 웹소켓 객체 생성
   let msgwebsocket = new WebSocket("ws://ec2-3-37-55-156.ap-northeast-2.compute.amazonaws.com:8082/ws/message/"+mid);

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
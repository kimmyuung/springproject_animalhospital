//쪽지 메소드
$(document).ready(function(){
    let mid = "";
    let hospital = "";

     $("#sendmsg").click(function(){
         $.ajax({
            url: '/member/findhospital',
            data :{"hospital" : hospital},
            success: function(result){
                console.log(result);
                if(!result){
                    alert("병원계정이 등록되어있지 않은 병원입니다");
                }
            }
        });
        let from = mid;
        let to = hospital;
        let msg = $("#msginput").val();
        console.log(msg);
        let jsonmsg = {
            "from" : mid,
            "to" : hospital,
            "msg" :msg,
            "type" : 2
        }
        console.log(jsonmsg);
        send(jsonmsg);
        $("#exampleModal").load(window.location.href + "#exampleModal");
        $("#close").trigger("click");
        alert("전송완료");
    });


    $.ajax({
        url: '/member/getinfo',
        success: function(result){
            mid= result.mid;
            hospital = result.hospital;
            console.log(result);
        }
    });

   let msgwebsocket = new WebSocket("ws://localhost:8082/ws/message/"+mid);

   msgwebsocket.onopen = onOpen;
   msgwebsocket.onclose = onClose;
   msgwebsocket.onmessage = onMessage;

   function onOpen(){}
   function onClose(){}
   function onMessage(){}

   function send(jsonmsg){
        msgwebsocket.send(JSON.stringify(jsonmsg));
   }

});
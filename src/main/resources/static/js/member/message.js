
$(document).ready(function(){
    let mid = "";
    let hname = "";
    let hdate = "";
    $.ajax({
        url: '/member/getinfo',
        success: function(result){
            console.log(result);
            mid = result.mid;
            hname = result.hname;
            hdate = result.hdate;
        }
    });

     $("#sendmsg").click(function(){
     console.log(mid);
     console.log(hname);
        let msg = $("#msginput").val();
        let jsonmsg = {

            "from" : mid,
            "to" : hname+hdate ,
            "msg" :msg
        }
        console.log(jsonmsg);
        send(jsonmsg);
        msg = "";
        $("#close").trigger("click");
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

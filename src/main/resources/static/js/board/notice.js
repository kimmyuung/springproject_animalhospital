getnotice();

function getnotice() {
$.ajax({
    url : '/board/getnotice',
    type : 'post',
    success : function(json) {
    console.log(json);
    alert("ㅇㅇ");
    }
});

}


function save(){
     let form = $("#saveform")[0];
        let formdata = new FormData( form);
        $.ajax({
            url: "/board/noticesave",
            method: "POST",
            data : formdata ,
            contentType: false,
            processData: false ,
            success: function( re ){
                alert("java와 통신성공");
            }
        });
}
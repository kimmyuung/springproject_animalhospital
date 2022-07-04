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



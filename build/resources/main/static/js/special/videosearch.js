function videosearch() {
let search = $("#searchbox").val();
let searchtext = $("#searchtext").val();
if(search == "") {
alert("검색어를 입력해주세요"); return;
}
if(searchtext == "") {
    $.ajax({
    url : '/videosearch',
    data : {"search" : search},
    type : "POST",
    success : function(json) {
        console.log(json);
        alert('ㅇㅇ');
        $('#searcharea').load(location.href+' #searcharea');
        }
    });
}else {
    $.ajax({
        url : '/videosearch',
        data : {"search" : searchtext},
        type : "POST",
        success : function(json) {
        console.log(json);
            alert('ㅇㅇ');
            $('#searcharea').load(location.href+' #searcharea');
            }
        });
    }
}

$("#searchbox").on("change", function(){
    if($("#searchbox option:selected").val() == "직접 입력" )
    {$("#searchtext").css("display", "block");
     }else{$("#searchtext").css("display", "none");}
});

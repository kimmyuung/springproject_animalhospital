function video() {
let search = $("#searchbox").val();
let searchtext = $("#searchtext").val();

if(search == "") {
alert("검색어를 입력해주세요"); return;
}

if(searchtext == "") {

    $.ajax({
    url : '/videosearch',
    data : {"search" : search},
    success : function(json) {
        console.log(json);
        let html2 = '';

         for(let i=0; i<json.items.length; i++) {
                html2 +=  '<div class="col-md-6">' +
                           '<iframe id="ytplayer" type="text/html" width="640" height="360"' +
                          'src="https://www.youtube.com/embed/'+json.items[i].id.videoId+'?rel=0&modestbranding=1"' +
                          '></iframe>' +
                       '</div>';
                        }

  $("#videobox").html(html2);
                    }

    });
}else {

    $.ajax({
        url : '/videosearch',
        data : {"search" : searchtext},
        success : function(json) {
        console.log(json);
        let html = '';
        for(let i=0; i<json.items.length; i++) {
        html +=  '<div class="col-md-6">' +
                   '<iframe id="ytplayer" type="text/html" width="640" height="360"' +
                  'src="https://www.youtube.com/embed/'+json.items[i].id.videoId+'rel=0&modestbranding=1"' +
                  '></iframe>' +
               '</div>';
                }

             $("#videobox").html(html);
            }
        });
    }

}

$("#searchbox").on("change", function(){
    if($("#searchbox option:selected").val() == "직접 입력" )
    { $("#searchtext").css("display", "block"); }
    else{$("#searchtext").css("display", "none");}
});

function enter() {
    if(e.keyCode == 13){
     video();
           }

}
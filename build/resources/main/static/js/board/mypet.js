function save(){
     let form = $("#saveform")[0];
        let formdata = new FormData( form);
        $.ajax({
            url: "/board/write",
            method: "POST",
            data : formdata ,
            contentType: false,
            processData: false ,
            success: function( re ){
                alert("java와 통신성공");
            }
        });
}



function boardlist( page){
             $.ajax({
    		 url: "/board/blist",
    		 method: "POST",
    		 data: {"page":this.current_page},
    		 success: function(boardlist){
                 	console.log(boardlist);
                 		 html = ' <tr> <th>번호</th><th>제목</th> </tr>';
                           if( boardlist.blists.length == 0 ){ // 검색 결과가 존재하지 않으면
                                    html +=
                                     '<tr>'+
                                     '<td colspan="5">검색 결과가 존재하지 않습니다.</td> '+
                                      '</tr>';
                                       }else{
                                       for( let i = 0 ; i<boardlist.blists.length ; i++ ){
                                       html +=
                                      '<tr>'+
                                      '<td>'+boardlist.blists[i].bno+'</td> '+
                                      '<td>'+boardlist.blists[i].btitle+'</td> '+
                                      '<td><img src="/upload/'+boardlist.blists[i].bimg+'"></td> '+
                                      '</tr>';
                                        }
                                 }
                                  let pagehtml = "";
 $("#table").html(html);
                $("#pagebtnbox").html( pagehtml);
    		}
    	});
}




$(function() {
    // Multiple images preview in browser
    var imagesPreview = function(input, placeToInsertImagePreview) {

        if (input.files) {
            var filesAmount = input.files.length;
               $(".preview").html("");
            for (i = 0; i < 1; i++) {
                var reader = new FileReader();

                reader.onload = function(event) {
                    $($.parseHTML('<img>')).attr('src', event.target.result).appendTo(placeToInsertImagePreview);
                     $($.parseHTML('<img>')).attr('style', 'width:80%');
                }

                reader.readAsDataURL(input.files[i]);
            }
        }

    };

    $('#bimg').on('change', function() {
        imagesPreview(this, 'div.preview');
    });
});
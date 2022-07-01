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

let current_page = 0;
boardlist(current_page);


function boardlist( page){
     this.current_page = page;
     console.log(this.current_page);
    $.ajax({
    		url: "/board/blist",
    		 method: "POST",
    		 data: {"page":this.current_page},
    		success: function(re){
    		alert("dd");
//    		console.log(boardlist);
//    		    html = ' <tr> <th>번호</th><th>제목</th> </tr>';
//                if( boardlist.data.length == 0 ){ // 검색 결과가 존재하지 않으면
//                                          html +=
//                                                '<tr>'+
//                                                        '<td colspan="5">검색 결과가 존재하지 않습니다.</td> '+
//                                                 '</tr>';
//                                }else{
//                                        for( let i = 0 ; i<boardlist.data.length ; i++ ){
//                                            html +=
//                                                    '<tr>'+
//                                                            '<td>'+boardlist.data[i].bno+'</td> '+
//                                                            '<td><a href="/board/view/'+boardlist.data[i].bno+'">'+boardlist.data[i].btitle+'<a></td> '+
//                                                     '</tr>';
//                                        }
//                    }
//                     let pagehtml = "";
//                     if( page == 0 ){
//                            pagehtml +=
//                             '<li class="page-item"> '+
//                                         '<button class="page-link" onclick="boardlist('+ (page)  +')"> 이전 </button>'+
//                              '</li>';
//                     }else{
//                         pagehtml +=
//                            '<li class="page-item"> '+
//                                        '<button class="page-link" onclick="boardlist('+ (page-1)  +')"> 이전 </button>'+
//                             '</li>';
//                      }
//
//                     for( let i = boardlist.startbtn ; i<=boardlist.endhtn ; i++ ){
//                        pagehtml +=
//                              '<li class="page-item"> '+
//                                '<button class="page-link" onclick="boardlist('+(i-1)+')"> '+i+' </button>'+
//                              '</li>';
//                     }
//
//                    if( page == boardlist.totalpages -1 ){
//                         pagehtml +=
//                                '<li class="page-item"> '+
//                                            '<button class="page-link" onclick="boardlist('+ (page)  +')"> 다음 </button>'+
//                                 '</li>';
//                    }else{
//                         pagehtml +=
//                            '<li class="page-item"> '+
//                                        '<button class="page-link" onclick="boardlist('+ (page+1)  +')"> 다음 </button>'+
//                             '</li>';
//                    }
//                $("#table").html(html);
//                $("#pagebtnbox").html( pagehtml);
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
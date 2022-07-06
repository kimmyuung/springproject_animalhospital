function save(){
        if($("#bimg").val()==""){
            alert("이미지를 하나 이상 등록해주세요");
        }else if($("#btitle").val()==""){
                     alert("제목을 입력해주세요");
          }else{
     let form = $("#saveform")[0];
        let formdata = new FormData( form);
        $.ajax({
            url: "/board/write",
            method: "POST",
            data : formdata ,
            contentType: false,
            processData: false ,
            success: function( re ){
                alert("등록완료");
                location.reload();
            }
        });
        }
}
let current_page = 0;
boardlist(0);


function boardlist( page){
     this.current_page = page;
//     console.log(this.current_page);
    $.ajax({
    		url: "/board/blist",
    		 method: "POST",
    		 data: {"page":this.current_page},
    		success: function(boardlist){
//    		console.log(boardlist);
    		    html =
    		        '<tr>'+
                    '   <th>번호</th>'+
                    '   <th>제목</th>'+
                    '   <th>이미지</th>'+
                    '</tr>';
//    		    '<div><div>번호</div><div>제목</div><div>이미지</div></div>';
                if( boardlist.blists.length == 0 ){ // 검색 결과가 존재하지 않으면
                                          html +=
                                                '<div>'+
                                                        '<div colspan="5">검색 결과가 존재하지 않습니다.</div> '+
                                                 '</div>';
                                }else{
                                        for( let i = 0 ; i<boardlist.blists.length ; i++ ){
                                            html +=
                                                '<tr type="button" data-bs-toggle="modal" data-bs-target="#myModal2" onclick="bview('+boardlist.blists[i].bno+')">'+
                                                '   <td>'+boardlist.blists[i].bno+'</td>'+
                                                '   <td>'+boardlist.blists[i].btitle+'</td>'+
                                                '   <td><img src="/upload/'+boardlist.blists[i].bimg+'"></td>'+
                                                '</tr>';
//                                                    '<div type="button" data-bs-toggle="modal" data-bs-target="#myModal2" onclick="bview('+boardlist.blists[i].bno+')">'+
//                                                            '<div>'+boardlist.blists[i].bno+'</div> '+
//                                                            '<div>'+boardlist.blists[i].btitle+'</div> '+
//                                                            '<div><img src="/upload/'+boardlist.blists[i].bimg+'"></div> '+
//                                                     '</div>';
                                        }
                    }
                     let pagehtml = "";
                     if( page == 0 ){
                            pagehtml +=
                             '<li class="page-item"> '+
                                         '<button class="page-link" onclick="boardlist('+ (page)  +')"> 이전 </button>'+
                              '</li>';
                     }else{
                         pagehtml +=
                            '<li class="page-item"> '+
                                        '<button class="page-link" onclick="boardlist('+ (page-1)  +')"> 이전 </button>'+
                             '</li>';
                      }
                        alert(boardlist.blists[0].endbtn);
                     for( let i = boardlist.blists[0].startbtn ; i<=boardlist.blists[0].endbtn; i++ ){
                        pagehtml +=
                              '<li class="page-item"> '+
                                '<button class="page-link" onclick="boardlist('+(i-1)+')"> '+i+' </button>'+
                              '</li>';
                     }

                    if( page == boardlist.totalpages -1 ){
                         pagehtml +=
                                '<li class="page-item"> '+
                                            '<button class="page-link" onclick="boardlist('+ (page)  +')"> 다음 </button>'+
                                 '</li>';
                    }else{
                         pagehtml +=
                            '<li class="page-item"> '+
                                        '<button class="page-link" onclick="boardlist('+ (page+1)  +')"> 다음 </button>'+
                             '</li>';
                    }
                $("#table").html(html);
                $("#pagebtnbox").html( pagehtml);
    		}
    	});

}
let bno = 0;
let bnum;
function bview(bno){

        bnum = bno;
        console.log(bnum);
        $.ajax({
            url : "/board/getboard" ,
            method : "GET",
            data : { "bno" : bno } ,
            success: function( board ){
                let imgtag = "";
                // 응답받은 데이터를 모달에 데이터 넣기
                console.log( board );

                for( let i = 0 ; i<board.bimglist.length ; i++ ){
                     if( i == 0 ){  // 첫번째 이미지만 active 속성 추가
                        imgtag +=
                                     '<div class="carousel-item active">'+
                                         '<img src="/upload/'+board.bimglist[i]+'" class="d-block w-100" alt="...">'+
                                    '</div>';
                     }else{
                        imgtag +=
                                 '<div class="carousel-item">'+
                                     '<img src="/upload/'+board.bimglist[i]+'" class="d-block w-100" alt="...">'+
                                '</div>';
                     }
                }
                if(board.same="true"){
                    $("#deletebutton").html(
                                        '<button type="button" class="btn btn-primary" onclick="bdelete('+board.bno+')">삭제</button>'
                                     );
                }
                $("#bwiter").html( board.mid );
                 $("#btitl").html( board.btitle );
                 $("#bcontent").html( board.bcontent );
                $("#modalimglist").html( imgtag );
                // 모달 띄우기
                $("#modalbtn").click();
            }
        });
}

function bdelete(bno){
      $.ajax({
                 url : "/board/bdelete" ,
                 method : "Delete",
                 data : { "bno" : bno } ,
                 success: function( board ){
                    alert("삭제완료");
                 }
        });
}


$(function() {
    // Multiple images preview in browser
    var imagesPreview = function(input, placeToInsertImagePreview) {

        if (input.files) {
            var filesAmount = input.files.length;
//               $(".preview").html("");
            for (i = 0; i < 1; i++) {
                var reader = new FileReader();

                reader.onload = function(event) {
                  $($("#img_preview")).attr('src', event.target.result).appendTo(placeToInsertImagePreview);
//                    $($.parseHTML('<img>')).attr('src', event.target.result).appendTo(placeToInsertImagePreview);
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

function replysave(){
    let reply = $("#reply").val();
    console.log("reply : "+reply);
    console.log("bnum : "+bnum);
    $.ajax({
        url:"/board/replysave",
        method : "POST",
        data : {"reply": reply, "bno": bnum},
        success : function(result){
            console.log(result);
             $('#viewcontent').load(location.href+' #viewcontent');
        }
    });

}
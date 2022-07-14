
gettip();

function gettip(){
            $.ajax({
                url : "/board/gettipboard" ,
                method : "GET",
                success: function( board ){
                    let imgtag = "";
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
                         if(board.same="true"){
                                 $("#deletebutton").html(
                                 '<button type="button" class="btn btn-primary" onclick="bdelete('+board.bno+')">삭제</button>'+
                                 '<button type="button" class="btn btn-primary" onclick="bupdate('+board.bno+')"   data-bs-toggle="modal" data-bs-target="#myModal">수정</button>'
                              );
                    }

                    }
                    $("#bwiter").html( board.mid );
                     $("#getbtitle").html( board.btitle );
                     $("#getbcontent").html( board.bcontent );
                    $("#getbimg").html( imgtag );
                    $("#bdate").html(  board.modifiedate );
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

function bupdate(bno){
    $.ajax({
                    url : "/board/gettipboard" ,
                    method : "GET",
                    success: function( board ){
                        let imgtag = "";
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
                         $("#btitle").val( board.btitle );
                         $("#getbcontent").html( board.bcontent );
                        $("#preview").html( imgtag );
                    }
                });
}

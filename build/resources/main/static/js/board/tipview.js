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

                    }
                     if(board.same=="true"){
                             $("#deletebutton").html(
                             '<button type="button" class="btn btn-primary" onclick="bdelete('+board.bno+')">삭제</button>'+
                             '<button type="button" class="btn btn-primary" onclick="bupdate('+board.bno+')"   data-bs-toggle="modal" data-bs-target="#myModal">수정</button>'
                          );

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
                                                 '<img id="img_preview" src="/upload/'+board.bimglist[i]+'" class="d-block w-100" alt="...">'+
                                            '</div>';
                             }else{
                                imgtag +=
                                         '<div class="carousel-item">'+
                                             '<img id="img_preview" src="/upload/'+board.bimglist[i]+'" class="d-block w-100" alt="...">'+
                                        '</div>';
                             }
                        }
                         $("#btitle").val( board.btitle );
                         $("#bcontent").html( board.bcontent );
                        $("#preview").html( imgtag );
                    }
                });
}

function tipupdate(){
    let form = $("#saveform")[0];
            let formdata = new FormData( form);
            $.ajax({
                url: "/board/tipupdate",
                method: "PUT",
                data : formdata ,
                contentType: false,
                processData: false ,
                success: function( re ){
                    if(re){
                    location.reload();
                    }else{
                        alert("로그인 후 이용해주세요!")
                    }
                }
            });
}
$(function() {
    // Multiple images preview in browser
    var imagesPreview = function(input, placeToInsertImagePreview) {

        if (input.files) {
            var filesAmount = input.files.length;
//               $(".preview").html("");
        console.log(filesAmount);
            for (i = 0; i < filesAmount; i++) {
                var reader = new FileReader();

                reader.onload = function(event) {
                  $($("#img_preview")).attr('src', event.target.result).appendTo(placeToInsertImagePreview);
//
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
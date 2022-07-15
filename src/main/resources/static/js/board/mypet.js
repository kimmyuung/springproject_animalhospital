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
                if(re){
                     location.href ="list"

                }else{
                    alert("로그인 후 이용해주세요!")
                }
            }
        });
        }
}

let current_page = 0;
boardlist(0);


function boardlist( page){
    this.current_page = page;
    console.log(this.current_page);
    $.ajax({
    		url: "/board/blist",
    		 method: "POST",
    		 data: {"page":this.current_page},
    		success: function(boardlist){
    		console.log(boardlist);
    		    html ="";
//    		    '<div><div>번호</div><div>제목</div><div>이미지</div></div>';
                if( boardlist.blists.length == 0 ){ // 검색 결과가 존재하지 않으면
                      html +=
                        '<div>'+
                            '<div colspan="5">검색 결과가 존재하지 않습니다.</div> '+
                        '</div>';
                      }else{
                                for( let i = 0 ; i<boardlist.blists.length ; i++ ){
                                    if(i%3 ==0){

                                         html +=
                                            '<div class="row">'+
                                                '<div class="card" style="width: 18rem;" >'+
                                                '  <img src="/upload/'+boardlist.blists[i].bimg+'" class="card-img-top"  data-bs-toggle="modal" data-bs-target="#myModal2" onclick="bview('+boardlist.blists[i].bno+')">'+
                                                '  <div class="card-body">'+
                                                '    <h5 class="card-title">'+boardlist.blists[i].btitle+'</h5>'+
                                                '    <p class="card-text d-flex justify-content-end">'+boardlist.blists[i].bdate+'</p>'+
//                                                '    <p class="card-text">'+boardlist.blists[i].mid+boardlist.blists[i].bdate+'</p>'+
//                                                '    <a href="#" class="btn btn-primary">Go somewhere</a>'+
                                                '  </div>'+
                                                '</div>';

                                    }else if(i %3 ==1){
                                        html +=
                                            '<div class="card" style="width: 18rem;" >'+
                                                '  <img src="/upload/'+boardlist.blists[i].bimg+'" class="card-img-top"  data-bs-toggle="modal" data-bs-target="#myModal2" onclick="bview('+boardlist.blists[i].bno+')">'+
                                                '  <div class="card-body">'+
                                                '    <h5 class="card-title">'+boardlist.blists[i].btitle+'</h5>'+
                                                '    <p class="card-text d-flex justify-content-end">'+boardlist.blists[i].bdate+'</p>'+
//                                                '    <p class="card-text">'+boardlist.blists[i].mid+boardlist.blists[i].bdate+'</p>'+
//                                                '    <a href="#" class="btn btn-primary">Go somewhere</a>'+
                                                '  </div>'+
                                                '</div>';
                                    }else if(i %3 ==2){
                                        html +=
                                                '<div class="card" style="width: 18rem;" >'+
                                                '  <img src="/upload/'+boardlist.blists[i].bimg+'" class="card-img-top"  data-bs-toggle="modal" data-bs-target="#myModal2" onclick="bview('+boardlist.blists[i].bno+')">'+
                                                '  <div class="card-body">'+
                                                '    <h5 class="card-title">'+boardlist.blists[i].btitle+'</h5>'+
                                                '    <p class="card-text d-flex justify-content-end">'+boardlist.blists[i].bdate+'</p>'+
//                                                '    <p class="card-text">'+boardlist.blists[i].mid+boardlist.blists[i].bdate+'</p>'+
//                                                '    <a href="#" class="btn btn-primary">Go somewhere</a>'+
                                                '  </div>'+
                                                '</div>';
                                            '</div>';
                                    }
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
                      console.log(boardlist.blists);
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
        $.ajax({
            url : "/board/getboard" ,
            method : "GET",
            data : { "bno" : bno } ,
            success: function( board ){
                let imgtag = "";
                // 응답받은 데이터를 모달에 데이터 넣기
                console.log( board );
                getreply();
                for( let i = 0 ; i<board.bimglist.length ; i++ ){
                     if( i == 0 ){  // 첫번째 이미지만 active 속성 추가
                        imgtag +=
                                     '<div class="carousel-item active">'+
                                         '<img id="preview" src="/upload/'+board.bimglist[i]+'" class="d-block w-100" alt="...">'+
                                    '</div>';
                     }else{
                        imgtag +=
                                 '<div class="carousel-item">'+
                                     '<img  id="preview" src="/upload/'+board.bimglist[i]+'" class="d-block w-100" alt="...">'+
                                '</div>';
                     }
                }
                if(board.same=="true"){
                    $("#deletebutton").html(
                    '<button type="button" class="btn btn-primary" onclick="bdelete('+board.bno+')">삭제</button>'+
                    '<button type="button" class="btn btn-primary" onclick="bupdate('+board.bno+')"   data-bs-toggle="modal" data-bs-target="#myModal3">수정</button>'
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

function bupdate(bno){
alert(bno);
    $.ajax({
            url : "/board/getboard" ,
            method : "GET",
            data: {"bno":bno},
            success: function( board ){
            console.log(board);
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
                 $("#btitle2").val( board.btitle );
                 $("#bcontent2").html( board.bcontent );
                $("#preview2").html( imgtag );
            }
    })
}
function tipupdate(){
    let form = $("#updateform")[0];
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

    $('#bimg2').on('change', function() {
        imagesPreview(this, 'div.preview');
    });
});



function replysave(){
    let reply = $("#reply").val();
    $.ajax({
        url:"/board/replysave",
        method : "POST",
        data : {"reply": reply, "bno": bnum},
        success : function(result){
            if(result){
                $('#reply').val('');
                getreply();
            }else{
                alert("로그인 후 이용해주세요!")
            }
        }
    });
}



function getreply(){
    let replyhtml = "";
    $.ajax({
        url:"/board/getreply",
        data : { "bno": bnum },
        success : function(result){
            for(let i = 0; i <result.length; i++){
                if(result[i].rindex == 0){
                    if(result[i].same == true){
                        replyhtml +=
                            '<div>'+
                                '<div class="row">'+
                                    '<div class="col-md-6">'+result[i].mid+'</div>'+
                                    '<div class="col-md-6 d-flex justify-content-end">'+result[i].createdate+'</div>'+
                                '</div>'+
                                '<div>'+result[i].rcontent+'</div>'+
                                '<div id="repltbtn">'+
                                    '<button type="button" onclick="rereplyinput('+result[i].rno+')">답글</button><button type="button" onclick="replyupdate('+result[i].rno+')">수정</button><button type="button" onclick="replydelete('+result[i].rno+')">삭제</button>'+
                                '</div>'+
                                '<div id = "'+result[i].rno+'"></div>'+
                            '</div>';
                    }else{
                        replyhtml +=
                            '<div>'+
                                '<div class="row">'+
                                    '<div class="col-md-6">'+result[i].mid+'</div>'+
                                    '<div class="col-md-6 d-flex justify-content-end">'+result[i].createdate+'</div>'+
                                '</div>'+
                                '<div>'+result[i].rcontent+'</div>'+
                                '<div id="repltbtn">'+
                                    '<button type="button" onclick="rereply('+result[i].rno+')">답글</button>'+
                                '</div>'+
                                '<div id="'+result[i].rno+'"></div>'+
                            '</div>';
                    }
                }else{
                    getrereply(result[i].rindex);
                }

            }
             $('#replytable').html(replyhtml);
        }
    });
}

function replydelete(rno) {
    $.ajax({
        url: '/board/replydelete',
        data : { "rno": rno },
        success : function(result){
            getreply();
        }
    });
}
function replyupdate(rno) {
    $.ajax({
        url: '/board/replyupdate',
        data : { "rno": rno },
        success : function(result){
            let html =
                '<input type="text" id="reply" value="'+result.rcontent+'">'+
                '<button type="button" onclick="reupdate('+rno+')">수정</button>';
            $("#replyinput").html(html);
        }
    });
}

function reupdate(rno){
    let reply = $("#reply").val();
        $.ajax({
            url:'/board/reupdate',
            method : "POST",
            data : {"rno": rno,"reply": reply},
            success : function(result){
                $('#reply').val('');
                getreply();
            }
        });
}
function rereplyinput(rno){
        let html =
            '<input type="text" id="reply"">'+
            '<button type="button" onclick="rereply('+rno+')">답글</button>';
        $("#replyinput").html(html);
}

function rereply(rno){

    let reply = $("#reply").val();
    let rindex = rno;
    $.ajax({
        url:"/board/rereply",
        method : "POST",
        data : {"reply": reply, "bno": bnum, "rindex":rindex},
        success : function(result){
            if(result){
                $('#reply').val('');
                getreply();
            }else{
                alert("로그인 후 이용해주세요!")
            }
        }

    });
}
function getrereply(rno){
    let rindex = rno;
    let rereplyhtml = "";
    $.ajax({
        url: "/board/getrereply" ,
        data : { "rindex" : rindex , "bno": bnum } ,
        success : function(result){
            console.log(result);
            for(let i = 0; i <result.length; i++){
                if(result[i].same == true){
                    rereplyhtml +=
                        '<div>'+
                            '<div class="row">'+
                                '<div class="col-md-6">'+result[i].mid+'</div>'+
                                '<div class="col-md-6 d-flex justify-content-end">'+result[i].createdate+'</div>'+
                            '</div>'+
                            '<div>'+result[i].rcontent+'</div>'+
                            '<div id="repltbtn">'+
                                '<button type="button" onclick="replyupdate('+result[i].rno+')">수정</button><button type="button" onclick="replydelete('+result[i].rno+')">삭제</button>'+
                            '</div>'+
                        '</div>';
                }else{
                    rereplyhtml +=
                        '<div>'+
                            '<div class="row">'+
                                '<div class="col-md-6">'+result[i].mid+'</div>'+
                                '<div class="col-md-6 d-flex justify-content-end">'+result[i].createdate+'</div>'+
                            '</div>'+
                            '<div>'+result[i].rcontent+'</div>'+
                        '</div>';
                }
            }
            console.log(rereplyhtml);
            $("#"+ rindex).html(rereplyhtml);
        }
    });
}
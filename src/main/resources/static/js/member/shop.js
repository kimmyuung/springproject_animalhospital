let current_page = 0;
let updatebno = 0;
getitemlist(0);

function getitemlist(page) {

this.current_page = page;


$.ajax({
    url : '/member/getitemlist',
    data : {"page" : this.current_page },
    success : function(json) {

    let html = "";
    let html2 = '';
    console.log(json);

    if(json.itemlist.length == 0) {
                                html += '<div>' +
                                '검색 결과가 존재하지 않습니다. ' +
                                '</div>';
                                }else{
       for(let i = 0; i < json.itemlist.length; i++) {
       html+=
               '<div class="item grid-group-item col-xs-4 col-lg-4">' +
                   '<div class="thumbnail">' ;
                   if(json.itemlist[i].simg != null) {
                     html +=  '<img class="group list-group-image d-block w-100" src="/shopupload/'+json.itemlist[i].simg+'"/>' ;
                     }
                   html +=  '<div class="caption">' +
                           '<h4 class="group inner list-group-item-heading">상품 이름 : ' +
                               json.itemlist[i].stitle + '</h4>' +
                           '<p class="group inner list-group-item-text">상품 설명 : ' +
                              json.itemlist[i].scontent + '</p>' +
                           '<div class="row">' +
                               '<div class="col-xs-12 col-md-6">' +
                                   '<p class="lead">가격 : ' +
                                       + json.itemlist[i].sprice +
                                       '</p>' +
                               '</div>' +
                               '<div class="col-xs-12 col-md-6">' +
                                   '<a class="btn btn-success" onclick="itemview('+json.itemlist[i].sno+')">상세 보기</a>' +
                               '</div>' +
                           '</div>' +
                       '</div>' +
                   '</div>' +
               '</div>';
             }
               ////////////////////////////////////// 이전 /////////////////////////////////////////////
             if( page == 0 )
              {
              html2 += ' <div class="page-item col-md-2"><a class="page-link" onclick="getitemlist('+ (page)+')">Previous</a></div>' ;
              } else{
                 html2 +=
                   ' <div class="page-item col-md-2"><a class="page-link" onclick="getitemlist('+(page-1)+')">Previous</a></div>' ;
                    }
                               ////////////////////////////////////// 페이징 ////////////////////////////////////////////
              for( let i = json.itemlist[0].startbtn ; i <=  json.itemlist[0].endbtn; i++) {
                    html2 += '<div class="page-item col-md-2"><button class="btn btn-primary mx-1" onclick="getitemlist(' + (i-1) +')">'
                    + (i) + '</button></div>';
                     }
                             ////////////////////////////////////// 이후 버튼 //////////////////////////////////////////
                             if(page == json.itemlist[0].totalpage -1 ){
                             html2 +=
                              ' <div class="page-item col-md-2"><a class="page-link" onclick="getitemlist('+ (page) +')">Next</a></div>' ;
                             }
                             else {
                              html2 +=
                              ' <div class="page-item col-md-2"><a class="page-link" onclick="getitemlist('+(page+1)+')">Next</a></div>' ;
                              }


        }
        $("#products").html(html);
        $("#btnbox").html( html2 );// 페이징버튼 html 넣기
    }
});

}

function itemsave() {

if($("#stitle").val() == "") {alert("상품 제목을 입력해주세요"); return;}
if($("#scontent").val() == "") {alert("상품에 대한 설명을 입력해주세요"); return;}
let check = /^[0-9]+$/;

alert( $("#price").val() );

if (! check.test($("#price").val() ) ) {   alert("가격은 숫자만 입력해야 합니다."); return;}


let form = $("#saveform")[0]; // [0] : 폼내 입력 데이터 //
    //[0]을 안하면 데이터 + 설정값까지 같이 보내지게 됨
    let formData = new FormData(form);

$.ajax({
            url : '/member/itemsave',
            type: "POST",
            data : formData,
            contentType : false, // 첨부파일 전송시 사용되는 속성
            processData : false,
            success : function(re) {
            console.log(re);
           if(re == true) {alert("등록 성공");
           getitemlist(0);
           $("#price").val('');
           $("#stitle").val('');
           $("#scontent").val('');
           $('#myModal').modal('hide')
           }
           else { alert("등록 실패");}
           }
    });

}

function itemview(sno) {

location.href = '/member/itemview?sno='+ sno;
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

    $('#simg').on('change', function() {
        imagesPreview(this, 'div.preview');
    });
});


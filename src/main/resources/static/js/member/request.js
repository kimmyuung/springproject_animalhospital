
function search(){
    let keyword = $("#searchbar").val();
    $.ajax({
        url: "/map/search",
        data: {"keyword" : keyword},
        success: function(result) {
            searchresult = result;
            let searchlist= "";
            if(result.length == 0){
                searchlist = '<div>일치하는 병원이 없습니다.</div>'
            }else {
                for (let i = 0; i<result.length; i++){
                    hname = result[i].name;
                    hdate = result[i].opendate;
                    hcity = result[i].city;
                    searchlist +=
                        '<div Onclick="hnamesave('+i+')" style="cursor:pointer" >'+result[i].city+' '+result[i].name+'</div>';
                }
            }
            $("#searchlist").html(searchlist);
        }
    });
}

function hnamesave(i){
//alert(searchresult[i].name+"hdate"+searchresult[i].opendate+"hcity"+searchresult[i].city);
    $("#hname").val(searchresult[i].name);
    $("#hdate").val(searchresult[i].opendate);

    $("#closebtn").trigger('click');
}

function request(){
//    let hname = $("#hname").val();
//    let hdate = $("#hdate").val();
    let binimg = $("#binimg").val();
    alert(hname + hdate+binimg);
    let formData = new FormData($('#requestform')[0]);
    $.ajax({
        url: '/member/requestsave',
        method: "POST",
        enctype: 'multipart/form-data',
//        data: {"hname": hname, "hdate": hdate},
//        data: {"hname": hname, "hdate": hdate,"binimg": binimg},
        data: formData,
        contentType: false,
        processData: false ,
        success: function(result){
            alert("완료");
        }
    });
}
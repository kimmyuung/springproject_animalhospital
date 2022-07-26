
function search(){
    let keyword = $("#searchbar").val();
    if(keyword == "") {
        alert("검색어를 입력해주세요"); return;
    }
    if(keyword == "동물병원" || keyword == "동물" || keyword == "병원") {
        alert("검색되는 숫자가 너무 많습니다. 다른 검색어로 검색해주세요"); return;
        $("#searchbar").val("");
    }
    var pr = /^[가-힣0-9]{2,20}$/      //한글 3글자 이상 20글자 이하
    if(pr.test(keyword)) {
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
                $("#searchbar").val("");
            }
        });
    }else {
        alert("검색어는 한글로 최소 2자 이상 입력해야 합니다.");
        $("#searchbar").val("");
        return;
    }
}

function hnamesave(i){
    $("#hname").val(searchresult[i].name);
    $("#hospital").val(searchresult[i].name + searchresult[i].opendate);
    $("#searchlist").val("");
    $("#closebtn").trigger('click');
}

function request(){
    let hospital = $("#hospital").val();
    let binimg = $("#binimg").val();
    if(hospital == "" || binimg == ""){
        alert("모두 입력해주세요!");
    }else {
        let formData = new FormData($("#requestform")[0]);

        $.ajax({
            url: '/member/requestsave',
            method: "POST",
            enctype: 'multipart/form-data',
            async: false,
            data: formData,
            contentType: false,
            processData: false ,
            success: function(result){
                if(result == 1){
                    alert("신청완료");
                }else if (result == 2){
                    alert("이미 등록된 병원입니다");
                }else{
                    alert("신청실패");
                }
            }
        });
    }

}
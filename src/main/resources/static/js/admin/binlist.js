list();
let html =
    '<tr>'+
    '    <th>회원아이디</th> <th>병원(이름+인허가일자)</th> <th>사업자번호</th><th>사업자 등록증</th><th>권한변경</th>'+
    '</tr>';
let blist = "";
function list(){
    console.log("list");
    $.ajax({
        url: "/admin/getbinlist",
        success: function(result){
        blist = result;
            for(let i=0; i<result.length; i++){
                html +=
                    '<tr>'+
                    '    <td>'+result[i].mid+'</td><td>'+result[i].hname+result[i].hdate+'</td><td><input type="text" id="bin" name="bin"></td><td><img style="width:100px;" src="/upload/'+result[i].binimg+'"></td><td><button type="button" onclick="setrole('+i+')">허가</button></td>'+
                    '</tr>';
            }
            console.log(result);
            $("#binlisttable").html(html);
        }
    });
}

function setrole(i){
    let mno = blist[i].mno;
    let hname = blist[i].hname;
    let hdate = blist[i].hdate;
    let bin = $("#bin").val();
    alert(bin);
    $.ajax({
        url:"/admin/setrole",
        data: {"mno" : mno, "hname" : hname, "hdate" : hdate, "bin" : bin},
        success: function(result){
            if(result){
                alert("변경완료");
            }else{
                alert("변경실패")
            }
        }
    });
}
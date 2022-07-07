info();
function info(){
    $.ajax({
        url: "/map/info",
        method: "POST",
        contentType : 'application/json' ,
        success: function(re){
            $("#hname").html(re.hname);
      }
    });
}

function modal(){

    let kind="";
    let fac="";
    let price="";

    for(let i=1; i<6;i++){
   kind +=
       '<img class="star" id="star'+i+'" src="/img/star2.png" onclick="kind('+i+')">';
             }
     for(let i=1; i<6;i++){
        fac +=
            '<img class="star" id="fac'+i+'" src="/img/star2.png" onclick="fac('+i+')">';
      }
      for(let i=1; i<6;i++){
         price +=
              '<img class="star" id="price'+i+'" src="/img/star2.png" onclick="price('+i+')">';
                  }
    $("#kind").html(kind);
    $("#fac").html(fac);
    $("#price").html(price);

}

let ckind=5;
let cfac=5;
let cprice=5;
let cavg=5;
function kind(i){
    ckind=i;
    if(i==1){
    document.getElementById("star"+1).src = "/img/star2.png";
    document.getElementById("star"+2).src = "/img/star1.png";
    document.getElementById("star"+3).src = "/img/star1.png";
    document.getElementById("star"+4).src = "/img/star1.png";
    document.getElementById("star"+5).src = "/img/star1.png";
    }
    else if(i==2){
        document.getElementById("star"+1).src = "/img/star2.png";
        document.getElementById("star"+2).src = "/img/star2.png";
        document.getElementById("star"+3).src = "/img/star1.png";
        document.getElementById("star"+4).src = "/img/star1.png";
        document.getElementById("star"+5).src = "/img/star1.png";
    }
    else if(i==3){
        document.getElementById("star"+1).src = "/img/star2.png";
        document.getElementById("star"+2).src = "/img/star2.png";
        document.getElementById("star"+3).src = "/img/star2.png";
        document.getElementById("star"+4).src = "/img/star1.png";
         document.getElementById("star"+5).src = "/img/star1.png";
    }
    else if(i==4){
        document.getElementById("star"+1).src = "/img/star2.png";
        document.getElementById("star"+2).src = "/img/star2.png";
        document.getElementById("star"+3).src = "/img/star2.png";
        document.getElementById("star"+4).src = "/img/star2.png";
        document.getElementById("star"+5).src = "/img/star1.png";
    }
    else if(i==5){
        document.getElementById("star"+1).src = "/img/star2.png";
        document.getElementById("star"+2).src = "/img/star2.png";
        document.getElementById("star"+3).src = "/img/star2.png";
        document.getElementById("star"+4).src = "/img/star2.png";
        document.getElementById("star"+5).src = "/img/star2.png";
    }
    avg();
}
function fac(i){
    cfac=i;
    if(i==1){
    document.getElementById("fac"+1).src = "/img/star2.png";
    document.getElementById("fac"+2).src = "/img/star1.png";
    document.getElementById("fac"+3).src = "/img/star1.png";
    document.getElementById("fac"+4).src = "/img/star1.png";
    document.getElementById("fac"+5).src = "/img/star1.png";
    }
    else if(i==2){
        document.getElementById("fac"+1).src = "/img/star2.png";
        document.getElementById("fac"+2).src = "/img/star2.png";
        document.getElementById("fac"+3).src = "/img/star1.png";
        document.getElementById("fac"+4).src = "/img/star1.png";
        document.getElementById("fac"+5).src = "/img/star1.png";
    }
    else if(i==3){
        document.getElementById("fac"+1).src = "/img/star2.png";
        document.getElementById("fac"+2).src = "/img/star2.png";
        document.getElementById("fac"+3).src = "/img/star2.png";
        document.getElementById("fac"+4).src = "/img/star1.png";
         document.getElementById("fac"+5).src = "/img/star1.png";
    }
    else if(i==4){
        document.getElementById("fac"+1).src = "/img/star2.png";
        document.getElementById("fac"+2).src = "/img/star2.png";
        document.getElementById("fac"+3).src = "/img/star2.png";
        document.getElementById("fac"+4).src = "/img/star2.png";
        document.getElementById("fac"+5).src = "/img/star1.png";
    }
    else if(i==5){
        document.getElementById("fac"+1).src = "/img/star2.png";
        document.getElementById("fac"+2).src = "/img/star2.png";
        document.getElementById("fac"+3).src = "/img/star2.png";
        document.getElementById("fac"+4).src = "/img/star2.png";
        document.getElementById("fac"+5).src = "/img/star2.png";
    }
    avg();
}

function price(i){
    cprice=i;
    if(i==1){
    document.getElementById("price"+1).src = "/img/star2.png";
    document.getElementById("price"+2).src = "/img/star1.png";
    document.getElementById("price"+3).src = "/img/star1.png";
    document.getElementById("price"+4).src = "/img/star1.png";
    document.getElementById("price"+5).src = "/img/star1.png";
    }
    else if(i==2){
        document.getElementById("price"+1).src = "/img/star2.png";
        document.getElementById("price"+2).src = "/img/star2.png";
        document.getElementById("price"+3).src = "/img/star1.png";
        document.getElementById("price"+4).src = "/img/star1.png";
        document.getElementById("price"+5).src = "/img/star1.png";
    }
    else if(i==3){
        document.getElementById("price"+1).src = "/img/star2.png";
        document.getElementById("price"+2).src = "/img/star2.png";
        document.getElementById("price"+3).src = "/img/star2.png";
        document.getElementById("price"+4).src = "/img/star1.png";
         document.getElementById("price"+5).src = "/img/star1.png";
    }
    else if(i==4){
        document.getElementById("price"+1).src = "/img/star2.png";
        document.getElementById("price"+2).src = "/img/star2.png";
        document.getElementById("price"+3).src = "/img/star2.png";
        document.getElementById("price"+4).src = "/img/star2.png";
        document.getElementById("price"+5).src = "/img/star1.png";
    }
    else if(i==5){
        document.getElementById("price"+1).src = "/img/star2.png";
        document.getElementById("price"+2).src = "/img/star2.png";
        document.getElementById("price"+3).src = "/img/star2.png";
        document.getElementById("price"+4).src = "/img/star2.png";
        document.getElementById("price"+5).src = "/img/star2.png";
    }
    avg();
}

avg();
function avg(){
    cavg=((ckind+cfac+cprice)/3).toFixed(1);
    $("#avg").html(cavg);
    if(cavg<=1){
        document.getElementById("avg1").src =  "/img/star2.png";
        document.getElementById("avg2").src = "/img/star1.png";
        document.getElementById("avg3").src =  "/img/star1.png";
        document.getElementById("avg4").src = "/img/star1.png";
        document.getElementById("avg5").src = "/img/star1.png";
        }
        else if(cavg<=2){
            document.getElementById("avg1").src =  "/img/star2.png";
            document.getElementById("avg2").src = "/img/star2.png";
           document.getElementById("avg3").src =  "/img/star1.png";
            document.getElementById("avg4").src = "/img/star1.png";
           document.getElementById("avg5").src = "/img/star1.png";
        }
        else if(cavg<=3){
            document.getElementById("avg1").src =  "/img/star2.png";
            document.getElementById("avg2").src = "/img/star2.png";
           document.getElementById("avg3").src =  "/img/star2.png";
            document.getElementById("avg4").src = "/img/star1.png";
             document.getElementById("avg5").src = "/img/star1.png";
        }
        else if(cavg<=4){
           document.getElementById("avg1").src = "/img/star2.png";
            document.getElementById("avg2").src = "/img/star2.png";
            document.getElementById("avg3").src = "/img/star2.png";
            document.getElementById("avg4").src = "/img/star2.png";
            document.getElementById("avg5").src = "/img/star1.png";
        }
        else if(cavg<=5){
            document.getElementById("avg1").src = "/img/star2.png";
            document.getElementById("avg2").src = "/img/star2.png";
            document.getElementById("avg3").src = "/img/star2.png";
            document.getElementById("avg4").src = "/img/star2.png";
            document.getElementById("avg5").src = "/img/star2.png";
        }
}
function file1(){
    document.all.file1.click();
      $(function() {
                     $("#file1").on('change', function(){
                     readURL(this);
                     });
                 });
                 function readURL(input) {
                     if (input.files && input.files[0]) {
                         var reader = new FileReader();
                         reader.onload = function (e) {
                         $('#preview1').attr('src', e.target.result);
                         }
                         reader.readAsDataURL(input.files[0]);
                     }
                 }


}
function file2(){
    document.all.file2.click();
     $(function() {
         $("#file2").on('change', function(){
         readURL(this);
         });
     });
     function readURL(input) {
         if (input.files && input.files[0]) {
             var reader = new FileReader();
             reader.onload = function (e) {
             $('#preview2').attr('src', e.target.result);
             }
             reader.readAsDataURL(input.files[0]);
         }
     }
}

function addreview(){

        let formData = new FormData();
        formData.append('rcontent',document.getElementById('rcontent').value);
        if(document.getElementById('file1').files[0]==null){

        }else{
            formData.append('rimg1',document.getElementById('file1').files[0]);
        }

        if(document.getElementById('file2').files[0]==null){

        }else{
            formData.append('rimg2',document.getElementById('file2').files[0]);
        }

        formData.append('rkind',ckind);
        formData.append('rfac',cfac);
        formData.append('rprice',cprice);

       $.ajax({
               url: "/map/addreview",
               method: "POST",
               data : formData ,
               contentType: false,
               processData: false ,
               success: function(re){
                alert(re);
             }
           });
}

getreviewlist();

function getreviewlist(){

}

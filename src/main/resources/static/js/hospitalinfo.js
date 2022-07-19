info();

function info(){
    $.ajax({
        url: "/map/info",
        method: "POST",
        contentType : 'application/json' ,
        success: function(re){
        console.log(re)

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
    int i;
    if(i==1){
        document.getElementById("avg1").src =  "/img/star2.png";
        document.getElementById("avg2").src = "/img/star1.png";
        document.getElementById("avg3").src =  "/img/star1.png";
        document.getElementById("avg4").src = "/img/star1.png";
        document.getElementById("avg5").src = "/img/star1.png";
        }
        else if(i==2){
            document.getElementById("avg1").src =  "/img/star2.png";
            document.getElementById("avg2").src = "/img/star2.png";
           document.getElementById("avg3").src =  "/img/star1.png";
            document.getElementById("avg4").src = "/img/star1.png";
           document.getElementById("avg5").src = "/img/star1.png";
        }
        else if(i==3){
            document.getElementById("avg1").src =  "/img/star2.png";
            document.getElementById("avg2").src = "/img/star2.png";
           document.getElementById("avg3").src =  "/img/star2.png";
            document.getElementById("avg4").src = "/img/star1.png";
             document.getElementById("avg5").src = "/img/star1.png";
        }
        else if(i==4){
           document.getElementById("avg1").src = "/img/star2.png";
            document.getElementById("avg2").src = "/img/star2.png";
            document.getElementById("avg3").src = "/img/star2.png";
            document.getElementById("avg4").src = "/img/star2.png";
            document.getElementById("avg5").src = "/img/star1.png";
        }
        else if(i==5){
            document.getElementById("avg1").src = "/img/star2.png";
            document.getElementById("avg2").src = "/img/star2.png";
            document.getElementById("avg3").src = "/img/star2.png";
            document.getElementById("avg4").src = "/img/star2.png";
            document.getElementById("avg5").src = "/img/star2.png";
        }
}
function file1(){
    document.all.file1.click();
        var imagesPreview = function(input, placeToInsertImagePreview) {
            if (input.files) {
                var filesAmount = input.files.length;
                for (i = 0; i < 1; i++) {
                    var reader = new FileReader();

                    reader.onload = function(event) {
                      $($("#preview1")).attr('src', event.target.result).appendTo(placeToInsertImagePreview);
                    }

                    reader.readAsDataURL(input.files[i]);
                }
            }

        };

        $('#preview1').on('change', function() {
            imagesPreview(this, 'preview');
        });

}
function file2(){
    document.all.file2.click();
}


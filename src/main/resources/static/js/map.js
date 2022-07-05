  $.ajax({
                url : "https://openapi.gg.go.kr/Animalhosptl?" ,
                data :{"KEY" :"47d367a4e715424e8c25f17ff85a81ea","type":"json" ,"pSize" : 2500},
                async : false,
                dataType : "json",
                success: function(re) {
                     console.log(re)
                                //mapevevt(list2);
                        }//if end


                //success end
            }); //ajax end
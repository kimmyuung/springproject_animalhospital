function save(){
     let form = $("#saveform")[0];
        let formdata = new FormData( form);
        $.ajax({
            url: "/board/write",
            method: "POST",
            data : formdata ,
            contentType: false,
            processData: false ,
            success: function( re ){
                alert("java와 통신성공");
            }
        });
}



function boardlist(){
     let current_page = 0;

     $.ajax({
    		url: "/board/blist",
    		method: "POST",
    		data: {"page": current_page },
    		success: function(re){
    		alert("dd");
    		}
    	});

}
$(function() {
    // Multiple images preview in browser
    var imagesPreview = function(input, placeToInsertImagePreview) {

        if (input.files) {
            var filesAmount = input.files.length;
               $(".preview").html("");
            for (i = 0; i < 1; i++) {
                var reader = new FileReader();

                reader.onload = function(event) {
                    $($.parseHTML('<img>')).attr('src', event.target.result).appendTo(placeToInsertImagePreview);
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
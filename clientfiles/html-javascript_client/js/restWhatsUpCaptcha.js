$(function(){
    var degrees = 0


    function getWhatsUpcaptcha(){
        const getCaptchaUrl = $.ajax({
            type: "GET",
            url: 'api/captchaImg?captchaType=WHATS_UP',
            success: function (data) {
                EuCaptchaToken = getCaptchaUrl.getResponseHeader("x-jwtString");
                const jsonData = JSON.parse(data);
                $("#captchaImage").attr("src", "data:image/png;base64," + jsonData.captchaImg);
                $("#captchaImage").attr("captchaId", jsonData.captchaId);

            }
        });

    }

    function rotate(captchaElement,orientation , ...options){
        degrees += 45*orientation;
        console.log(degrees)

        $($('#captchaImage').get(0)).css({
            'transform': 'rotate(' + degrees + 'deg)',
            '-ms-transform': 'rotate(' + degrees + 'deg)',
            '-moz-transform': 'rotate(' + degrees + 'deg)',
            '-webkit-transform': 'rotate(' + degrees + 'deg)',
            '-o-transform': 'rotate(' + degrees + 'deg)'
        });

    }

    $('#btnToLeft').click(function (e){
        rotate(e.target , -1)
    })

    $('#btnToRight').click(function (e){

        rotate(e.target , 1)
    })

     getWhatsUpcaptcha();
})
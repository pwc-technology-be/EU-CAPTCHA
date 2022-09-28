let EuCaptchaToken;

function getLanguage(){
    let language = sessionStorage.getItem("language")
    if(language){
        return language;
    } else{
        return "en-GB";
    }
}
function getLastSelectedValue(){
    const language = sessionStorage.getItem("language");
    if(language) {
        document.getElementById('dropdown-language').value = language
    } else {
        sessionStorage.setItem("language", "en-GB");
        document.getElementById('dropdown-language').value = "en-GB";
    }
}
function initializeSlider() {}

function getSlidingcaptcha(){
    const getCaptchaUrl = $.ajax({
        type: "GET",
        url: 'api/captchaImg?captchaType=SLIDING&locale='+ getLanguage(),
        success: function (data) {
            console.log(getLanguage())
            EuCaptchaToken = getCaptchaUrl.getResponseHeader("x-jwtString");
            const jsonData = JSON.parse(data);
            sessionStorage.setItem("captchaId", jsonData.captchaId);
            const question = jsonData.captchaQuestion;
            const div = document.getElementById('captchaQuestion');
            div.innerText = question;
            sessionStorage.setItem("Max", jsonData.max);
            sessionStorage.setItem("Min", jsonData.min);
        }
    });
}
function reloadSlidingCaptcha(){
    const reloadCaptchaUrl = $.ajax({
        type: "GET",
        url: 'api/reloadCaptchaImg/' + sessionStorage.getItem("captchaId") + "?captchaType=SLIDING&locale="+ getLanguage(),
        beforeSend: function (xhr) {
            xhr.setRequestHeader("Accept", "application/json");
            xhr.setRequestHeader("Content-Type", "application/json");
            xhr.setRequestHeader("x-jwtString", EuCaptchaToken);
        },
        success: function (data) {
            EuCaptchaToken = reloadCaptchaUrl.getResponseHeader("x-jwtString");
            const jsonData = JSON.parse(data);
            sessionStorage.setItem("captchaId", jsonData.captchaId);
            const question = jsonData.captchaQuestion;
            const div = document.getElementById('captchaQuestion');
            div.innerText = question;
            sessionStorage.setItem("Max", jsonData.max);
            sessionStorage.setItem("Min", jsonData.min);
        }
    });
}
function validateSlidingCaptcha(){
    const validateCaptcha = $.ajax({
        type: "POST",
        contentType: 'application/json; charset=utf-8',
        url: "api/validateCaptcha/" + sessionStorage.getItem("captchaId"),
        beforeSend: function (xhr) {
            xhr.setRequestHeader("Accept", "application/json");
            xhr.setRequestHeader("Content-Type", "application/json");
            xhr.setRequestHeader("x-jwtString", EuCaptchaToken);
        },
        data: jQuery.param({
            captchaAnswer: $("#captchaAnswer").val()+"",
            useAudio: false,
            captchaType : 'SLIDING'
        }),
        contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
        cache: false,
        timeout: 600000,
        success: function (data) {
            $("input").css({"border": ""});
            obj = JSON.parse(data);
            if ('success' === obj.responseCaptcha) {
                $("#success").css("visibility", "visible");
                $("#fail").css("visibility", "hidden");
            } else {
                $("#fail").css("visibility", "visible");
                $("#success").css("visibility", "hidden");
                reloadSlidingCaptcha();
            }
        },
        error: function (e) {
            console.log("error" + e)
        }
    });
}

$(function(){
    $("#captchaReload").click(function(){
        $("#fail").css("visibility", "hidden");
        $("#success").css("visibility", "hidden");
        reloadSlidingCaptcha();
    });
    $("#captchaSubmit").click(function(){
        validateSlidingCaptcha();
    });

    getSlidingcaptcha();
})

$(document).ready(function () {
    $("#dropdown-language").change(function () {
        const selectedOption = $('#dropdown-language').val();
        $('#dropdown-language').val(selectedOption);
        if (selectedOption !== '') {
            sessionStorage.setItem("language", selectedOption);
            window.location.replace('?lang=' + selectedOption);
        }
    });

    let slider = document.getElementById("ex14");
    let output = document.getElementById("captcha-range-value");
    slider.oninput = function() {
        $("#captchaAnswer").val(slider.value)
        output.innerHTML = slider.value;
    }

    let min = sessionStorage.getItem("Min");
    let max = sessionStorage.getItem("Max");
    new Slider("#ex14", {
        ticks: [min - 15, min, max/2, max, max * 2],
        ticks_positions: [0, 20, 50, 65, 100],
        ticks_labels: [min - 15, min, max/2, max, max * 2],
        ticks_snap_bounds: 30
    });
});
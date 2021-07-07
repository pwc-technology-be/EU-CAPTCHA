let useAudio = false;
let EuCaptchaToken;

function onPlayAudio(){
    useAudio = true;
}
function capitalized() {
    const checkBox = document.getElementById('capitalized');
    if(checkBox.checked === true) {
        sessionStorage.setItem("capitalized", "true");
    } else {
        sessionStorage.setItem("capitalized", "false");
    }
    getcaptcha();
}
function getLastSelectedValue(){
    const language = sessionStorage.getItem("language");
    if(language) {
        document.getElementById('dropdown-language').value = language;
    } else {
        sessionStorage.setItem("language", "en-GB");
        document.getElementById('dropdown-language').value = "en-GB";
    }
}
function getLanguage(){
    let language = sessionStorage.getItem("language")
    if(language){
        return language;
    } else{
        return "en-GB";
    }
}
function getCaptchaLength(queryString){
    const urlParams = new URLSearchParams(queryString);
    console.log(urlParams);
    const captchaLength = urlParams.get("captchaLength");
    if(captchaLength) {
        return captchaLength;
    } else {
        return 10;
    }
}
function getcaptcha(){
    const getCaptchaUrl = $.ajax({
        type: "GET",
        url: 'api/captchaImg?locale='+ getLanguage() + '&captchaLength='+ getCaptchaLength(window.location.search) + '&capitalized=' + sessionStorage.getItem("capitalized"),
        success: function (data) {
            EuCaptchaToken = getCaptchaUrl.getResponseHeader("x-jwtString");
            const jsonData = JSON.parse(data);
            $("#captchaImg").attr("src", "data:image/png;base64," + jsonData.captchaImg);
            $("#captchaImg").attr("captchaId", jsonData.captchaId);
            $("#audioCaptcha").attr("src", "data:audio/wav;base64," + jsonData.audioCaptcha);
        }
    });
}
$(function(){

    function reloadCaptcha(){
        const reloadCaptchaUrl = $.ajax({
            type: "GET",
            url: 'api/reloadCaptchaImg/' + $("#captchaImg").attr("captchaId") + '/?locale=' + sessionStorage.getItem("language") + '&captchaLength='+ getCaptchaLength(window.location.search) + '&capitalized='+ sessionStorage.getItem("capitalized"),
            beforeSend: function (xhr) {
                xhr.setRequestHeader("Accept", "application/json");
                xhr.setRequestHeader("Content-Type", "application/json");
                xhr.setRequestHeader("x-jwtString", EuCaptchaToken);
            },
            success: function (data) {
                EuCaptchaToken = reloadCaptchaUrl.getResponseHeader("x-jwtString");
                const jsonData = JSON.parse(data);
                $("#captchaImg").attr("src", "data:image/png;base64," + jsonData.captchaImg);
                $("#captchaImg").attr("captchaId", jsonData.captchaId);
                $("#audioCaptcha").attr("src", "data:audio/wav;base64," + jsonData.audioCaptcha);
                $("#captchaAnswer").val("");
                useAudio = false;
            }
        });
    }
    function validateCaptcha(){
        const validateCaptcha = $.ajax({
            type: "POST",
            contentType: 'application/json; charset=utf-8',
            url: "api/validateCaptcha/" + $("#captchaImg").attr("captchaId"),
            beforeSend: function (xhr) {
                xhr.setRequestHeader("Accept", "application/json");
                xhr.setRequestHeader("Content-Type", "application/json");
                xhr.setRequestHeader("x-jwtString", EuCaptchaToken);
            },
            data: jQuery.param({
                captchaAnswer: $("#captchaAnswer").val(),
                useAudio: useAudio
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
                    reloadCaptcha();
                }
            },
            error: function (e) {
                $("input").css({"border": "2px solid red"});
                $("#error").css("visibility", "visible");
                $("#fail").css("visibility", "hidden");
                $("#success").css("visibility", "hidden");
                reloadCaptcha();
            }
        });
    }

    $("#captchaReload").click(function(){
        $("#fail").css("visibility", "hidden");
        $("#success").css("visibility", "hidden");
        reloadCaptcha();
    });

    $("#captchaSubmit").click(function(){
        validateCaptcha();
    });

    $('#captchaAnswer').keypress(function(e) {
        if (e.keyCode === 13) {
            validateCaptcha();
            return false; // prevent the button click from happening
        }
    });

    $(document).ready(function () {
        $("#dropdown-language").change(function () {
            const selectedOption = $('#dropdown-language').val();
            $('#dropdown-language').val(selectedOption);
            if (selectedOption !== '') {
                sessionStorage.setItem("language", selectedOption);
                window.location.replace('?lang=' + selectedOption);
            }
        });
        var checkBox = document.getElementById('capitalized');
        if(checkBox.checked === true) {
            sessionStorage.setItem("capitalized", "true");
        } else {
            sessionStorage.setItem("capitalized", "false");
        }
    });

    getcaptcha();


});

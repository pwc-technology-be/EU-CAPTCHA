<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8">

    <link rel="shortcut icon" href="#" />
    <link rel="stylesheet" href="css/font-awesome-4.7.0/css/font-awesome.min.css">
    <link rel="stylesheet" href="css/jquery-ui.min.css">
    <link rel="stylesheet" href="css/eu-captcha-style.css">

    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js"></script>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css">
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/js/bootstrap.min.js"></script>

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="js/jquery-1.2.min.js"></script>
    <script src="js/restWhatsUpCaptcha.js" Prefer></script>

    <title>EU Captcha Rotate</title>
</head>
<body>
     <div class="container">
         <div class="rotation-captcha">
             <div class="row text-center">

                 <div class=" col-md-12 alert alert-success" id="success" role="alert" style="visibility : hidden">
                     <i class="fa fa-check-circle-o fa-3x" aria-hidden="true"></i> <fmt:message key="euCaptcha.valid" />
                 </div>
                 <div class=" col-md-12 alert alert-danger" id="fail" role="alert" style="visibility : hidden">
                     <i class="fa fa-exclamation-triangle fa-3x" aria-hidden="true"></i> <fmt:message key="euCaptcha.invalid" />
                 </div>
                 <div class=" col-md-12 alert alert-danger" id="error" role="alert" style="visibility : hidden">
                     <i class="fa fa-exclamation-triangle fa-3x" aria-hidden="true"></i> <fmt:message key="euCaptcha.error" />
                 </div>
             </div>

             <button id="btnToLeft" class="btn btn-default">to The left</button>
             <div class="captchaContainer">
                 <img id="captchaImage" src="" alt="duck" width="200" height="200" captchaId="">
             </div>
             <button id="btnToRight" class="btn btn-default">to The right</button>

             <div class="slidecontainer">
                 <input type="range" min="-360" max="360" value="0" class="slider" step="15" id="captcha-range">
                 <p>Rotation Angle : <span id="captcha-range-value"></span></p>
             </div>
             <div class="rotation-captcha-btn-container">
                 <button id="validateWhatsUpCaptcha" class="btn btn-success" >Validate</button>
             </div>
         </div>
         <form>
             <input id="captchaAnswer" type="hidden" value="0">
         </form>
     </div>
</body>
</html>
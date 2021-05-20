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

  <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
  <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js"></script>
  <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css">
  <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/js/bootstrap.min.js"></script>

  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
  <script src="js/jquery-1.2.min.js"></script>
  <script src="js/restCaptcha.js"></script>

  <title>EU Captcha</title>
</head>
<body dir="rtl " onload="getLastSelectedValue()">
  <div class="container ">
   <div class="row text-center">
      <div class="col-md-3">
      </div>
      <div class="col-md-6">
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
        <div class="panel panel-default bg border">
          <div class="panel-body">
            <div class="row ">
              <div class="col-md-12">
                <div class="form-group">
                  <label title="Change the language" for="dropdown-language"><fmt:message key="language.change" /> :  </label>
                  <select id="dropdown-language" class="custom-select  form-control">
                    <option value=" "> ... </option>
                    <option value="en-GB">English</option>
                    <option value="fr-FR">Français</option>
                    <option value="de-DE">Deutsch</option>
                    <option value="bg-BG">български</option>
                    <option value="hr-HR">Hrvatski</option>
                    <option value="da-DK">Dansk</option>
                    <option value="es-ES">Espanol</option>
                    <option value="et-EE">Eestlane</option>
                    <option value="fi-FI">Suomalainen</option>
                    <option value="el-GR">ελληνικά</option>
                    <option value="hu-HU" >Magyar</option>
                    <option value="it-IT">Italiano</option>
                    <option value="lv-LV">Latvietis</option>
                    <option value="lt-LT">Lietuvis</option>
                    <option value="mt-MT">Maltin</option>
                    <option value="nl-NL">Nederlands</option>
                    <option value="pl-PL">Polski</option>
                    <option value="pt-PT">Português</option>
                    <option value="ro-RO">Românesc</option>
                    <option value="sk-SK">Slovenský</option>
                    <option value="sl-SI">Slovensko</option>
                    <option value="sv-SE">Svenska</option>
                    <option value="cs-CZ">česky</option>
                  </select>
                </div>
              </div>
              <div class="col-md-4">
              </div>
            </div>
            <div class="row">
              <div class="col-md-2"></div>
              <div class="col-md-8">
                <img alt="Captcha image to solve" class="img-fluid img-thumbnail" src="" id="captchaImg" captchaId="">
                <hr>
                <audio controls autostart="1" src="" id="audioCaptcha" onplay="onPlayAudio()"></audio>
              </div>
              <div class="col-md-2">
                <br><br><br>
                <label title="Change the language" for="captchaReload">Reload the Captcha</label>
                <button title="Reload the Captcha" class="btn btn-primary btn-lg " id="captchaReload"> <em class="fa fa-refresh"></em> </button>
              </div>
            </div>
            <hr>
            <div class="row">
              <div class="col-md-2">

              </div>
              <div class="col-md-8">
                <label title="Captcha input field for solution" for="captchaAnswer">Input field for solution</label>
                <input title="Captcha input field for solution" type="text" class="form-control" id="captchaAnswer" placeholder="Captcha Text">
              </div>
              <div class="col-md-2">
                <label title="Submit tha Captcha" for="captchaSubmit">Submit the Captcha</label>
                <button title="Submit the Captcha" class="btn btn-primary btn-lg " id="captchaSubmit"> <em class="fa fa-check" aria-hidden="true"></em> </button>
              </div>
            </div>  
            <br>
          </div>
        </div>
        <div class="col-md-3">
        </div>
      </div>
    </div>
  </div>
</body>
</html>
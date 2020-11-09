<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
  <meta http-equiv="content-type" content="text/html; charset=utf-8">

  <link rel="shortcut icon" href="#" />
  <link rel="stylesheet" href="css/font-awesome-4.7.0/css/font-awesome.min.css">
  <link rel="stylesheet" href="css/jquery-ui.min.css">

<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js" integrity="sha384-DfXdz2htPH0lsSSs5nCTpuj/zy4C+OGpamoFVy38MVBnE+IbbVYUew+OrCXaRkfj" crossorigin="anonymous"></script>
  <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js" integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo" crossorigin="anonymous"></script>
  <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css" integrity="sha384-9aIt2nRpC12Uk9gS9baDl411NQApFmC26EwAOH8WgZl5MYYxFfc+NcPb1dKGj7Sk" crossorigin="anonymous">
  <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/js/bootstrap.min.js" integrity="sha384-OgVRvuATP1z7JjHLkuOU7Xw704+h835Lr+6QL9UvYjZE3Ipu6Tp75j7Bh/kR0JKI" crossorigin="anonymous"></script>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script src="js/jquery-1.2.min.js"></script>
<script src="js/restCaptcha.js"></script>

  <title>EU Captcha</title>
</head>
<body>
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
    </div>
        <div class="panel panel-default bg border">
          <div class="panel-body">
            <div class="row ">
              <div class="col-md-12">
                <div class="form-group">
                  <select id="dropdown-language" class="custom-select  form-control">
                    <option> <fmt:message key="language.change" /> :  </option>
                    <option value="en">English</option>
                    <option value="fr">French</option>
                    <option value="de">German</option>
                    <option value="bg">български</option>
                    <option value="hr">Hrvatski</option>
                    <option value="da">Dansk</option>
                    <option value="es">Espanol</option>
                    <option value="et">Eestlane</option>
                    <option value="fi">Suomalainen</option>
                    <option value="el">ελληνικά</option>
                    <option value="hu">Magyar</option>
                    <option value="it">Italiano</option>
                    <option value="lv">Latvietis</option>
                    <option value="lt">Lietuvis</option>
                    <option value="mt">Maltin</option>
                    <option value="nl">Nederlands</option>
                    <option value="pl">Polski</option>
                    <option value="pt">Português</option>
                    <option value="ro">Românesc</option>
                    <option value="sk">Slovenský</option>
                    <option value="sl">Slovensko</option>
                    <option value="sv">Svenska</option>
                    <option value="cs">česky</option>
                  </select>
                </div>
              </div>
              <div class="col-md-4">
              </div>
            </div>
            <div class="row">
              <div class="col-md-2"></div>
              <div class="col-md-8">
                <img alt="Captcha Loading" class="img-fluid img-thumbnail" src="" id="captchaImg" captchaId="">
                <hr>
                <audio controls autostart="1" src="" id="audioCaptcha" onplay="onPlayAudio()"></audio>
              </div>
              <div class="col-md-2">
                <br><br><br>
                <button class="btn btn-primary btn-lg " id="captchaReload"> <i class="fa fa-refresh"></i> </button>
              </div>
            </div>
            <hr>
            <div class="row">
              <div class="col-md-2">

              </div>
              <div class="col-md-8">
                <input type="text" class="form-control" id="captchaAnswer" placeholder="Captcha Text">
              </div>
              <div class="col-md-2">
                <button class="btn btn-primary btn-lg " id="captchaSubmit"> <i class="fa fa-check" aria-hidden="true"></i> </button>
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
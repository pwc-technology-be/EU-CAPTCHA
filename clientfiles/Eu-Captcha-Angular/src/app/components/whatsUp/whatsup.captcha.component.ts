import { Component, OnInit } from '@angular/core';
import { CaptchaService } from '../../services/captcha.service';
import { tap } from "rxjs/operators";
import { HttpResponse } from '@angular/common/http';
import { Captcha } from '../../models/captcha.model'
import { CaptchaActions } from '../interfaces/captcha.actions.interface';
import { FormControl } from '@angular/forms';
import * as $ from 'jquery'


@Component({
    selector: 'whatsup-captcha',
    templateUrl: './whatsup.captcha.component.html',
    styleUrls: ['./whatsup.captcha.component.scss']
})
export class WhatsUp implements OnInit, CaptchaActions {

    title = 'Eu-Captcha'

    /* Captcha Information */
    captchaImage: string | undefined
    captchaId: string = ""
    audioCaptcha: string | undefined
    degree: number | undefined
    /* end Captcha Information */
  
    answer: string = "0";
  
    validationStatus:boolean = false 
  
    showAlert : boolean = false ;
    alertMessage:string = "CAPTCHA validation successful."
    alertClass:string = "alert-success"
  

    constructor(private captchaService: CaptchaService) { }

    _currentRange = 0 ;

    whatsAppStyle = {};


    ngOnInit(): void {
        this.captchaService.getCaptcha("WHATS_UP")
        .subscribe(captchaRequest => {
          const captcha:Captcha = captchaRequest.body
  
          this.captchaImage = `data:image/png;base64,${captcha.captchaImg}`
          this.captchaId = captcha.captchaId
          this.audioCaptcha = captcha.audioCaptcha
          this.degree = captcha.degree
        })
    }

    onValidate(): void {
        this.captchaService.validateCaptcha(this.captchaId, this.answer, false , "WHATS_UP").subscribe(res => {
            const {responseCaptcha = 'fail'} = {...res}  ;
            this.validationStatus = (responseCaptcha!='fail')
            if(this.validationStatus){
             this.alertMessage = "CAPTCHA validation successful."
             this.alertClass = "alert-success"
            }else {
             this.alertMessage = "The text you have enterd does not match.Please try again."
             this.alertClass = "alert-danger"
            }
            this.showAlert = true ;
            this.onReload()
            this.validationStatusExpiration()
         } , err => {
            this.onReload()
         })
    }
    onReload(): void {
        this.captchaService.reloadCaptcha(this.captchaId, "WHATS_UP")
        .subscribe(captchaRequest => {
          const captcha:Captcha = captchaRequest.body
          this.captchaImage = `data:image/png;base64,${captcha.captchaImg}`
          this.captchaId = captcha.captchaId
          this.audioCaptcha = captcha.audioCaptcha
          this.degree = captcha.degree
        })
    }
    validationStatusExpiration(): void {
        setTimeout( () => {
            this.showAlert = false
            console.log(`validation status become  ${this.validationStatus}`) 
         } , 2000)
    }
    


    onChange(value:any):void {
        this._currentRange = value;
        
        let degree = value.target.value ;
       
        this.whatsAppStyle = {
            'transform': 'rotate(' + degree + 'deg)',
            '-ms-transform': 'rotate(' + degree + 'deg)',
            '-moz-transform': 'rotate(' + degree + 'deg)',
            '-webkit-transform': 'rotate(' + degree + 'deg)',
            '-o-transform': 'rotate(' +degree + 'deg)'
        }
        this.answer = degree
    }

         
   
}
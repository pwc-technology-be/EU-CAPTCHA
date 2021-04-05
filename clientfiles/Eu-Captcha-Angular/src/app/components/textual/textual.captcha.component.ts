

import { Component, OnInit } from '@angular/core';
import { CaptchaService } from '../../services/captcha.service';
import { tap } from "rxjs/operators";
import { HttpResponse } from '@angular/common/http';
import { Captcha } from '../../models/captcha.model'
import { CaptchaActions } from '../interfaces/captcha.actions.interface';
import { DomSanitizer, SafeResourceUrl, SafeStyle, SafeUrl } from '@angular/platform-browser';
import { FormBuilder, FormGroup, Validators , ReactiveFormsModule, FormsModule  } from "@angular/forms";


@Component({
  selector: 'textual-captcha',
  templateUrl: './textual.captcha.component.html',
  styleUrls: ['./textual.captcha.component.scss']
})


export class Textual implements OnInit , CaptchaActions {

  useAudio : boolean = false 

  title = 'Eu-Captcha'

  lang = "en"

  /* Captcha Information */
  captchaImage: string | undefined
  captchaId: string = ""
  audioCaptcha:SafeStyle= ""
  degree: number | undefined
  /* end Captcha Information */

  answer: string = "";

  validationStatus:boolean = false 

  showAlert : boolean = false ;
  alertMessage:string = "CAPTCHA validation successful."
  alertClass:string = "alert-success"

  captchaForm : FormGroup 
  constructor(private captchaService: CaptchaService , private _sanitizer: DomSanitizer , public fb: FormBuilder) { 
    this.captchaForm = this.fb.group({
        language: ['']
      })
  }
 


  captchaLang = 
  [{id : "en" , name : "English"},
                    {id : "fr" , name : "Français"},
                    {id : "de" , name : "Deutsch"},
                    {id : "bg" , name : "български"},
                    {id : "hr" , name : "Hrvatski"},
                    {id : "da" , name : "Dansk"},
                    {id : "es" , name : "Espanol"},
                    {id : "et" , name : "Eestlane"},
                    {id : "fi" , name : "Suomalainen"},
                    {id : "el" , name : "ελληνικά"},
                    {id : "hu" , name : "Magyar"},
                    {id : "it" , name : "Italiano"},
                    {id : "lv" , name : "Latvietis"},
                    {id : "lt" , name : "Lietuvis"},
                    {id : "mt" , name : "Maltin"},
                    {id : "nl" , name : "Nederlands"},
                    {id : "pl" , name : "Polski"},
                    {id : "pt" , name : "Português"},
                    {id : "ro" , name : "Românesc"},
                    {id : "sk" , name : "Slovenský"},
                    {id : "sl" , name : "Slovensko"},
                    {id : "sv" , name : "Svenska"},
                    {id : "cs" , name : "česky"}
]



  


  ngOnInit(): void {
        
    this.captchaService.getCaptcha("STANDARD")
      .subscribe(captchaRequest => {
        const captcha:Captcha = captchaRequest.body

        this.captchaImage = `data:image/png;base64,${captcha.captchaImg}`
        this.captchaId = captcha.captchaId
       
        this.audioCaptcha = this._sanitizer.bypassSecurityTrustResourceUrl(`data:audio/wav;base64,${captcha.audioCaptcha}`);
        
        console.log( this.audioCaptcha)
        
        this.degree = captcha.degree
      })
  }

  onValidate = () => {
    this.captchaService.validateCaptcha(this.captchaId, this.answer, false , "STANDARD").subscribe(res => {
       const {responseCaptcha = 'fail'} = {...res}  ;
       this.validationStatus = (responseCaptcha!='fail')
       if(this.validationStatus){
        this.alertMessage = "CAPTCHA validation successful."
        this.alertClass = "alert-success"
       }else {
        this.alertMessage = "The text you have enterd does not match.Please try again."
        this.alertClass = "alert-danger"
       }
       this.onReload()
       this.showAlert = true ;
       this.validationStatusExpiration()
    } , err => {
         this.onReload()
    })
  }
  onReload = () => {
    console.log(this.captchaForm.value)
    this.lang = this.captchaForm.value.language ;
    this.captchaService.reloadCaptcha(this.captchaId, "STANDARD", this.lang)
      .subscribe(captchaRequest => {
        const captcha:Captcha = captchaRequest.body
        this.captchaImage = `data:image/png;base64,${captcha.captchaImg}`
        this.captchaId = captcha.captchaId
        this.audioCaptcha = this._sanitizer.bypassSecurityTrustResourceUrl(`data:audio/wav;base64,${captcha.audioCaptcha}`);
        this.degree = captcha.degree
      })
  }

  validationStatusExpiration = () => {
       setTimeout( () => {
          this.showAlert = false
          console.log(`validation status become  ${this.validationStatus}`) 
       } , 2000)
  }

   onPlayAudio(){
    this.useAudio = true;
    }

    submit() {
         
    }
}
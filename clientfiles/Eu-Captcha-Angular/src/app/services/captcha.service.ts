import { HttpClient, HttpHeaders, HttpResponse } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs/internal/Observable";
import { tap } from "rxjs/operators";

import { environment } from "src/environments/environment";
import { Captcha } from "../models/captcha.model";

@Injectable({ providedIn: "root" })
export class CaptchaService {

    host:string = environment.host

    captchaLength = 8
    captchaLengthParam = `captchaLength=${this.captchaLength}`

    
    
   

    euCaptchaToken : string = ""
    

    constructor(private http: HttpClient) {

    }


    getCaptcha = (captchaType: string = "STANDARD"): Observable<HttpResponse<any>> => {
        let captchaTypeParam = `captchaType=${captchaType}`
        return this.http.get<HttpResponse<any>>(`${this.host}/api/captchaImg?${captchaTypeParam}&${this.captchaLengthParam}` , {observe : 'response'}  )
        .pipe(tap(res => {
            const euCaptchaToken = res.headers.get("x-jwtString")
            if(euCaptchaToken) this.setEuCaptchaToken(euCaptchaToken) 
        }))
    }
    validateCaptcha = (captchaId : string, captchaAnswer : string , useAudio : boolean , captchaType : string): Observable<any>  => {

        const httpOptions = {
            headers: new HttpHeaders({
              'Content-Type':  'application/x-www-form-urlencoded; charset=UTF-8 application/json',
              'Accept' : 'application/json',
              'x-jwtString': this.euCaptchaToken
            })
          }; 
        
        return this.http.post<any>(`${this.host}/api/validateCaptcha/${captchaId}` , `captchaAnswer=${captchaAnswer}&useAudio=false&captchaType=${captchaType}`, httpOptions)
    }
    setEuCaptchaToken = (euCaptchaToken : string) =>  {
        
        this.euCaptchaToken = euCaptchaToken ;

        console.log(` set euCaptchaToken to ${euCaptchaToken} ` )
        
        //setTimeout( () => {this.euCaptchaToken = ""} , 20000)
    }

    reloadCaptcha = (captchaId : string , captchaType: string = "STANDARD" , language : string = "en") : Observable<HttpResponse<any>> => {
        let captchaTypeParam = `captchaType=${captchaType}`
        let localeParam = `lang=${language}`
        const httpOptions = {
            headers: new HttpHeaders({
              'Content-Type':  'application/x-www-form-urlencoded; charset=UTF-8 application/json',
              'Accept' : 'application/json',
              'x-jwtString': this.euCaptchaToken
            })
          };
         return this.http.get<HttpResponse<any>>(`${this.host}/api/reloadCaptchaImg/${captchaId}?${captchaTypeParam}&${this.captchaLengthParam}&${localeParam}`  , {headers : httpOptions.headers , observe : 'response'} )
         .pipe(tap(res => {
            const euCaptchaToken = res.headers.get("x-jwtString")
            if(euCaptchaToken) this.setEuCaptchaToken(euCaptchaToken) 
        }))
    }

}


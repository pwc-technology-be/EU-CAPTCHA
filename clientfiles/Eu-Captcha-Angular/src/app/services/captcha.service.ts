import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/internal/Observable';
import { tap } from 'rxjs/operators';

import { environment } from 'src/environments/environment';

@Injectable({ providedIn: 'root' })
export class CaptchaService {
  host: string = environment.host;

  euCaptchaToken = '';

  constructor(private http: HttpClient) {}

  getCaptcha = (
    captchaType: string = 'STANDARD',
    captchaLanguage = 'en-GB',
    captchaLength = 8,
    capitalized= true
  ): Observable<HttpResponse<any>> => {
    const captchaTypeParam = `captchaType=${captchaType}`;

    const captchaLengthParam = `captchaLength=${captchaLength}`;
    const captchaLanguageParam = `locale=${captchaLanguage}`;
    const captchaCapitalizedParam = `capitalized=${capitalized}`;

    return this.http
      .get<HttpResponse<any>>(
        `${this.host}/api/captchaImg?${captchaTypeParam}&${captchaLanguageParam}&${captchaLengthParam}&${captchaCapitalizedParam}`,
        { observe: 'response' }
      )
      .pipe(
        tap((res) => {
          const euCaptchaToken = res.headers.get('x-jwtString');
          if (euCaptchaToken) {
            this.setEuCaptchaToken(euCaptchaToken);
          }
        })
      );
  }
  validateCaptcha = (
    captchaId: string,
    captchaAnswer: string,
    captchaType: string
  ): Observable<any> => {
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type':
          'application/x-www-form-urlencoded; charset=UTF-8 application/json',
        Accept: 'application/json',
        'x-jwtString': this.euCaptchaToken,
        withCredentials: String(true),
      }),
    };

    return this.http.post<any>(
      `${this.host}/api/validateCaptcha/${captchaId}`,
      `captchaAnswer=${captchaAnswer}&useAudio=false&captchaType=${captchaType}`,
      httpOptions
    );
  }
  setEuCaptchaToken = (euCaptchaToken: string) => {
    this.euCaptchaToken = euCaptchaToken;

    console.log(` set euCaptchaToken to ${euCaptchaToken} `);

    // setTimeout( () => {this.euCaptchaToken = ""} , 20000)
  }

  reloadCaptcha = (
    captchaId: string,
    captchaType: string = 'STANDARD',
    language: string = 'en',
    captchaLength = 8
  ): Observable<HttpResponse<any>> => {
    const captchaTypeParam = `captchaType=${captchaType}`;
    const localeParam = `lang=${language}`;
    const captchaLengthParam = `captchaLength=${captchaLength}`;

    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type':
          'application/x-www-form-urlencoded; charset=UTF-8 application/json',
        Accept: 'application/json',
        'x-jwtString': this.euCaptchaToken,
        withCredentials: String(true),
      }),
    };
    return this.http
      .get<HttpResponse<any>>(
        `${this.host}/api/reloadCaptchaImg/${captchaId}?${captchaTypeParam}&${captchaLengthParam}&${localeParam}`,
        { headers: httpOptions.headers, observe: 'response' }
      )
      .pipe(
        tap((res) => {
          const euCaptchaToken = res.headers.get('x-jwtString');
          if (euCaptchaToken) {
            this.setEuCaptchaToken(euCaptchaToken);
          }
        })
      );
  }
}

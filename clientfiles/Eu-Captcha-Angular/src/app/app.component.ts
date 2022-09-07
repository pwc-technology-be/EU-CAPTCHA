import { Component, OnInit } from '@angular/core';
import { CaptchaService } from './services/captcha.service';
import { tap } from 'rxjs/operators';
import { HttpResponse } from '@angular/common/http';
import { Captcha } from './models/captcha.model';

@Component({
  selector: 'eucaptcha-app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent {
  isTextual = true;

  switchCaptchaType = (captchaType: string) => {
    if (captchaType === 'Textual') { this.isTextual = true; }
    else { this.isTextual = false; }
  }
}

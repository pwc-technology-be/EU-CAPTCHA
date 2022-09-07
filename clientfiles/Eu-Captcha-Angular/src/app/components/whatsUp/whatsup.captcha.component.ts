import { Component, OnInit } from '@angular/core';
import { CaptchaService } from '../../services/captcha.service';
import { Captcha } from '../../models/captcha.model';
import { CaptchaActions } from '../interfaces/captcha.actions.interface';

@Component({
  selector: 'eucaptcha-whatsup',
  templateUrl: './whatsup.captcha.component.html',
  styleUrls: ['./whatsup.captcha.component.scss'],
})
export class WhatsUpComponent implements OnInit, CaptchaActions {
  title = 'Eu-Captcha';

  /* Captcha Information */
  captchaImage: string | undefined;
  captchaId = '';
  audioCaptcha: string | undefined;
  degree: number | undefined;
  /* end Captcha Information */

  answer = '0';

  validationStatus = false;

  showAlert = false;
  alertMessage = 'CAPTCHA validation successful.';
  alertClass = 'alert-success';

  constructor(private captchaService: CaptchaService) {}

  currentRange = 0;

  whatsAppStyle = {};

  ngOnInit(): void {
    this.captchaService.getCaptcha('WHATS_UP').subscribe((captchaRequest) => {
      const captcha: Captcha = captchaRequest.body;

      this.captchaImage = `data:image/png;base64,${captcha.captchaImg}`;
      this.captchaId = captcha.captchaId;
      this.audioCaptcha = captcha.audioCaptcha;
      this.degree = captcha.degree;
    });
  }

  onValidate(): void {
    this.captchaService
      .validateCaptcha(this.captchaId, this.answer, 'WHATS_UP')
      .subscribe(
        (res) => {
          const { responseCaptcha = 'fail' } = { ...res };
          this.validationStatus = responseCaptcha !== 'fail';
          if (this.validationStatus) {
            this.alertMessage = 'CAPTCHA validation successful.';
            this.alertClass = 'alert-success';
          } else {
            this.alertMessage =
              'The text you have enterd does not match.Please try again.';
            this.alertClass = 'alert-danger';
          }
          this.showAlert = true;
          this.onReload();
          this.validationStatusExpiration();
        },
        (err) => {
          this.onReload();
        }
      );
  }
  onReload(): void {
    this.captchaService
      .reloadCaptcha(this.captchaId, 'WHATS_UP')
      .subscribe((captchaRequest) => {
        const captcha: Captcha = captchaRequest.body;
        this.captchaImage = `data:image/png;base64,${captcha.captchaImg}`;
        this.captchaId = captcha.captchaId;
        this.audioCaptcha = captcha.audioCaptcha;
        this.degree = captcha.degree;
      });
  }
  validationStatusExpiration(): void {
    setTimeout(() => {
      this.showAlert = false;
      console.log(`validation status become  ${this.validationStatus}`);
    }, 2000);
  }

  onChange(value: any): void {
    this.currentRange = value;

    const degree = value.target.value;

    this.whatsAppStyle = {
      transform: 'rotate(' + degree + 'deg)',
      '-ms-transform': 'rotate(' + degree + 'deg)',
      '-moz-transform': 'rotate(' + degree + 'deg)',
      '-webkit-transform': 'rotate(' + degree + 'deg)',
      '-o-transform': 'rotate(' + degree + 'deg)',
    };
    this.answer = degree;
  }
}

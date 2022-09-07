import { Component, OnInit } from '@angular/core';
import { CaptchaService } from '../../services/captcha.service';
import { Captcha } from '../../models/captcha.model';
import { CaptchaActions } from '../interfaces/captcha.actions.interface';
import { DomSanitizer, SafeStyle } from '@angular/platform-browser';
import { FormBuilder, FormControl, FormGroup } from '@angular/forms';

@Component({
  selector: 'eucaptcha-textual',
  templateUrl: './textual.captcha.component.html',
  styleUrls: ['./textual.captcha.component.scss'],
})
export class TextualComponent implements OnInit, CaptchaActions {
  useAudio = false;

  title = 'Eu-Captcha';

  lang = 'en-GB';

  /* Captcha Information */
  captchaImage: string | undefined;
  captchaId = '';
  audioCaptcha: SafeStyle = '';
  degree: number | undefined;
  /* end Captcha Information */

  answer = '';

  validationStatus = false;

  showAlert = false;
  alertMessage = 'CAPTCHA validation successful.';
  alertClass = 'alert-success';

  captchaForm: FormGroup;
  capitalized: FormControl;

  constructor(
    private captchaService: CaptchaService,
    private sanitizer: DomSanitizer,
    public fb: FormBuilder
  ) {
    this.captchaForm = this.fb.group({
      language: ['']
    });
    this.capitalized = new FormControl(true);
  }

  captchaLang = [
    { id: 'en-GB', name: 'English' },
    { id: 'fr-FR', name: 'Français' },
    { id: 'de-DE', name: 'Deutsch' },
    { id: 'bg-BG', name: 'български' },
    { id: 'hr-HR', name: 'Hrvatski' },
    { id: 'da-DA', name: 'Dansk' },
    { id: 'es-ES', name: 'Español' },
    { id: 'et-ET', name: 'Eesti keel' },
    { id: 'fi-FI', name: 'Suomi' },
    { id: 'el-EL', name: 'ελληνικά' },
    { id: 'hu-HU', name: 'Magyar' },
    { id: 'it-IT', name: 'Italiano' },
    { id: 'lv-LV', name: 'Latviešu valoda' },
    { id: 'lt-LT', name: 'Lietuvių kalba' },
    { id: 'mt-MT', name: 'Malti' },
    { id: 'nl-NL', name: 'Nederlands' },
    { id: 'pl-PL', name: 'Polski' },
    { id: 'pt-PT', name: 'Português' },
    { id: 'ro-RO', name: 'Română' },
    { id: 'sk-SK', name: 'Slovenčina' },
    { id: 'sl-SL', name: 'Slovenščina' },
    { id: 'sv-SV', name: 'Svenska' },
    { id: 'cs-CS', name: 'čeština' },
  ];

  ngOnInit(): void {
    this.onInit();
  }
  onInit = () => {
    this.captchaService
      .getCaptcha('STANDARD', this.lang, 8, this.capitalized.value)
      .subscribe((captchaRequest) => {
        const captcha: Captcha = captchaRequest.body;

        this.captchaImage = `data:image/png;base64,${captcha.captchaImg}`;
        this.captchaId = captcha.captchaId;

        this.audioCaptcha = this.sanitizer.bypassSecurityTrustResourceUrl(
          `data:audio/wav;base64,${captcha.audioCaptcha}`
        );

        console.log(this.audioCaptcha);

        this.degree = captcha.degree;
      });
  }

  onValidate = () => {
    this.captchaService
      .validateCaptcha(this.captchaId, this.answer, 'STANDARD')
      .subscribe(
        (res) => {
          const { responseCaptcha = 'fail' } = { ...res };
          this.validationStatus = responseCaptcha !== 'fail';
          if (this.validationStatus) {
            this.alertMessage = 'CAPTCHA validation successful.';
            this.alertClass = 'alert-success';
          } else {
            this.alertMessage =
              'The text you have entered does not match.Please try again.';
            this.alertClass = 'alert-danger';
          }
          this.onReload();
          this.showAlert = true;
          this.validationStatusExpiration();
        },
        (err) => {
          this.onReload();
        }
      );
  }
  onReload = () => {
    console.log(this.captchaForm.value);
    this.lang = this.captchaForm.value.language;
    this.captchaService
      .reloadCaptcha(this.captchaId, 'STANDARD', this.lang)
      .subscribe((captchaRequest) => {
        const captcha: Captcha = captchaRequest.body;
        this.captchaImage = `data:image/png;base64,${captcha.captchaImg}`;
        this.captchaId = captcha.captchaId;
        this.audioCaptcha = this.sanitizer.bypassSecurityTrustResourceUrl(
          `data:audio/wav;base64,${captcha.audioCaptcha}`
        );
        this.degree = captcha.degree;
      });
  }

  onChange(value: string): void {
    this.captchaForm.value.language = value;
    this.lang = value;
    this.onInit();
  }

  capitalizedChange(value: string): void {
    this.onInit();
  }

  validationStatusExpiration = () => {
    setTimeout(() => {
      this.showAlert = false;
      console.log(`validation status become  ${this.validationStatus}`);
    }, 2000);
  }

  onPlayAudio(): void {
    this.useAudio = true;
  }

}

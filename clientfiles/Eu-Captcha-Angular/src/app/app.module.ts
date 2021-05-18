import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import {HttpClientModule} from '@angular/common/http'
import { AppComponent } from './app.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { Textual } from './components/textual/textual.captcha.component';
import { WhatsUp } from './components/whatsUp/whatsup.captcha.component';

@NgModule({
  declarations: [
    AppComponent,
    Textual,
    WhatsUp
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule ,
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }

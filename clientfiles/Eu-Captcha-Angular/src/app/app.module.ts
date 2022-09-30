import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { AppComponent } from './app.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { TextualComponent } from './components/textual/textual.captcha.component';
import { WhatsUpComponent } from './components/whatsUp/whatsup.captcha.component';

@NgModule({
  declarations: [AppComponent, TextualComponent, WhatsUpComponent],
  imports: [BrowserModule, HttpClientModule, FormsModule, ReactiveFormsModule],
  providers: [],
  bootstrap: [AppComponent],
})
export class AppModule {}

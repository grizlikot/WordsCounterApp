import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { HttpErrorResponse } from '@angular/common/http/src/response';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'Welcome to Word Counter App!';

  private files:string [] = [];
  successful:boolean = false;
  message:string = '';
  wordsMap:Map<number, Map<string, number>> = new Map<number, Map<string, number>>();

  constructor (private httpService: HttpClient) {  }

  pushFilesContents (e) {
    this.files.length = 0;
    for (var i = 0; i < e.target.files.length; i++) { 
      this.files.push(e.target.files[i]);
    }
  }

  uploadFiles () {
    const formData = new FormData();

    if (this.files.length === 0) {
      alert('Choose files!');
      return;
    }
    
    for (var i = 0; i < this.files.length; i++) { 
      formData.append("files", this.files[i]);
    }
    
    this.httpService.post('/files/count', formData).subscribe(
      data => {
        debugger;
        this.message = 'Words counted successfully!'
        this.wordsMap = data as Map<number, Map<string, number>>;
        this.successful = true;
      },
      (error: HttpErrorResponse) => {
        console.log (error.message);
        this.message = 'There are some errors! Check console!'
        this.successful = false;
      }
    );
  }

  clearValues() {
    debugger;
    this.files.length = 0;
    (<HTMLInputElement>document.getElementById("file")).value = "";
    this.successful = false;
    this.message = ''
    this.wordsMap = new Map<number, Map<string, number>>();
  }

}

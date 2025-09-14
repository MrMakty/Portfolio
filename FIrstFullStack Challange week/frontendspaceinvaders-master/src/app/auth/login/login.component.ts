import {Component, inject} from '@angular/core';
import {LoginService} from '../../services/login.service';
import {Router} from '@angular/router';
import {FormsModule} from '@angular/forms';
import {TranslatePipe} from '@ngx-translate/core';

@Component({
  selector: 'app-login',
  imports: [FormsModule, TranslatePipe],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {
  protected email = "";
  protected password = "";

  protected loginService = inject(LoginService);
  private router = inject(Router);

  login() {
    this.loginService.login({email: this.email, password: this.password}).subscribe({
      next: (responseData) => {
        this.router.navigate([''])
      },
      error: (error) =>{
        console.log(error);
      }
    });

    if(this.loginService.isLoggedIn()){
      this.router.navigate(['']);
    }
  }

  navigateToRegister() {
    this.router.navigate(['register']);
  }
}

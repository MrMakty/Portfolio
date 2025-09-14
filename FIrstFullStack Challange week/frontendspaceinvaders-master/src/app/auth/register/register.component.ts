import {Component, inject} from '@angular/core';
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {LoginService} from '../../services/login.service';
import {Router} from '@angular/router';
import {Register} from '../../models/register';
import {SpaceshipService} from '../../services/spaceship.service';
import {Spaceship} from '../../models/spaceship';
import {ProfileService} from '../../services/profile.service';
import {TranslatePipe} from '@ngx-translate/core';

@Component({
  selector: 'app-register',
  imports: [
    ReactiveFormsModule,
    TranslatePipe
  ],
  templateUrl: './register.component.html',
  styleUrl: './register.component.scss'
})
export class RegisterComponent {
  protected error: number|null = null;
  protected registerForm = new FormGroup({
    "email": new FormControl("", [Validators.required, Validators.email]),
    "password": new FormControl("", [Validators.required, Validators.minLength(8),
      Validators.pattern(/^(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&(),.?":{}|<>]).{8,}$/)]),
    "firstName": new FormControl("", [Validators.required]),
    "lastName": new FormControl("", [Validators.required]),
    "occupation": new FormControl("", [Validators.required]),

    "shipname": new FormControl("", [Validators.required]),
    "function": new FormControl("", [Validators.required]),
    "size": new FormControl("", [Validators.required]),
    "imagePath": new FormControl("", [Validators.required]),
  })

  private loginService = inject(LoginService);
  private router = inject(Router);
  private spaceshipService = inject(SpaceshipService);
  private profileService = inject(ProfileService);

  protected register(): void {
    const registerData: Register = {
      email: this.registerForm.get('email')!.value!,
      password: this.registerForm.get('password')!.value!,
      firstName: this.registerForm.get('firstName')!.value!,
      lastName: this.registerForm.get('lastName')!.value!,
      occupation: this.registerForm.get('occupation')!.value!,
      shipName: this.registerForm.get('shipname')!.value!,
      shipType: this.registerForm.get('function')!.value!,
      size: this.registerForm.get('size')!.value!,
      imagePath: this.registerForm.get('imagePath')!.value!
    }
    const shipData:Spaceship = {
      shipName: registerData.shipName,
      shipType: registerData.shipType,
      size: registerData.size,
      imagePath: registerData.imagePath
    }

    this.loginService.registerAccount(registerData).subscribe({
      next: (responseData) => {
        console.log(registerData);
        console.log(shipData);
        this.spaceshipService.createSpaceship(shipData).subscribe({
          next: (response) => {
            this.router.navigate(['/homepage']);
          },
          error: (error) => {
            console.error('Error creating spaceship:', error);
            this.profileService.deleteUser();
            this.loginService.resetToken();
            this.error = error;
          }
        });
      },
      error: (error) => {
        console.log(error)
        this.error = error;
      }
    })
  }
}

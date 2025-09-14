  import {Component, inject} from '@angular/core';
import {ProfileInformationComponent} from './profile-information/profile-information.component';
import {MyReservationListComponent} from './reservations/my-reservation-list/my-reservation-list.component';
  import {LoginService} from '../services/login.service';
  import {ProfileService} from '../services/profile.service';
  import {Observable, tap} from 'rxjs';
  import {Profile} from '../models/profile';
  import {Router} from '@angular/router';
  import {MyShipsListComponent} from './ships/my-ships-list/my-ships-list.component';
  import {TranslatePipe} from '@ngx-translate/core';

@Component({
  selector: 'app-profile',
  imports: [
    ProfileInformationComponent,
    MyReservationListComponent,
    MyShipsListComponent,
    TranslatePipe,
  ],
  templateUrl: './profile.component.html',
  standalone: true,
  styleUrl: './profile.component.scss'
})

export class ProfileComponent {
  private loginService = inject(LoginService);
  private profileService = inject(ProfileService);
  private router = inject(Router);
  protected ifFalseDisableButton: boolean = true;


  protected logout(): void {
    this.loginService.logout();
  }

  protected toAdminPageCheck(): void {
    const user$: Observable<Profile> = this.profileService.getUser();
    user$.pipe(
      tap(user => {
        if (user.role.name === 'ROLE_ADMIN') {
          console.log(user);
          console.log("user mag naar admin panaal");
          this.router.navigate(['/admin']);
        } else{
          console.log("user mag niet naar admin panaal");
          this.ifFalseDisableButton = false;
        }
      })
    ).subscribe();
  }
}

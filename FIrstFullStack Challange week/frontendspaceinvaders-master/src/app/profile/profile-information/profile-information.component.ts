import {Component, inject, OnInit} from '@angular/core';
import {ProfileService} from '../../services/profile.service';
import {Profile} from '../../models/profile';
import {TranslatePipe} from '@ngx-translate/core';

@Component({
  selector: 'app-profile-information',
  imports: [
    TranslatePipe
  ],
  templateUrl: './profile-information.component.html',
  standalone: true,
  styleUrl: './profile-information.component.scss'
})
export class ProfileInformationComponent implements OnInit{
  protected user?: Profile
  private profileService = inject(ProfileService)

  ngOnInit() {
    this.profileService.getUser().subscribe((user)=>{
      this.user = user
    });
  }
}

import {Component, OnInit, inject} from '@angular/core';
import { Profile } from '../../../models/profile';
import { ProfileService } from '../../../services/profile.service';
import {MyShipsItemComponent} from '../my-ships-item/my-ships-item.component';

@Component({
  selector: 'app-my-ship-list',
  standalone: true,
  imports: [
    MyShipsItemComponent
  ],
  templateUrl: './my-ships-list.component.html',
  styleUrl: './my-ships-list.component.scss'
})
export class MyShipsListComponent implements OnInit {
  public user?: Profile;
  private profileService = inject(ProfileService);

  ngOnInit() {
    this.profileService.getUser().subscribe((user) => {
      this.user = user;
      console.log(this.user)
    });

    console.log('Length: ' + this.user?.spaceships.length)
    console.log(this.user?.spaceships)
  }
}

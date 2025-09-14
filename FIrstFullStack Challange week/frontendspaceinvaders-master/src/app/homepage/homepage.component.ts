import { Component } from '@angular/core';
import {DockingMapComponent} from '../reservation/docking-map/docking-map.component';

@Component({
  selector: 'app-homepage',
  imports: [
    DockingMapComponent
  ],
  templateUrl: './homepage.component.html',
  styleUrl: './homepage.component.scss'
})
export class HomepageComponent {

}

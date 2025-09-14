import {Component, Input} from '@angular/core';
import {Reservation} from '../../../models/reservation';
import {TranslatePipe} from "@ngx-translate/core";
import {CommonModule} from '@angular/common';

@Component({
  selector: 'app-my-reservation-item',
    imports: [
        TranslatePipe, CommonModule
    ],
  templateUrl: './my-reservation-item.component.html',
  styleUrl: './my-reservation-item.component.scss'
})
export class MyReservationItemComponent{
  @Input() reservation!: Reservation;
}

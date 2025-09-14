import { Component, Input, OnInit } from '@angular/core';
import { Spaceship } from '../../../models/spaceship';
import {TranslatePipe} from "@ngx-translate/core";

@Component({
  selector: 'app-my-ships-item',
  standalone: true,
    imports: [
        TranslatePipe
    ],
  templateUrl: './my-ships-item.component.html',
  styleUrl: './my-ships-item.component.scss'
})
export class MyShipsItemComponent implements OnInit {
  @Input() spaceship!: Spaceship;

  ngOnInit(){
    console.log("Schip!")
  }
}

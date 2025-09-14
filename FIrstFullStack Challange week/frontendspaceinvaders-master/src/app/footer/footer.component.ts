import { Component } from '@angular/core';
import {NgForOf} from '@angular/common';

@Component({
  selector: 'app-footer',
  standalone: true,
  imports: [
    NgForOf
  ],
  templateUrl: './footer.component.html',
  styleUrl: './footer.component.scss'
})
export class FooterComponent {
  teamMembers = [
    { name: 'Jasper de Waal', studentNr: '1155590', email: 's1155590@student.hsleiden.nl' },
    { name: 'Matthieu Bulthuis', studentNr: '1142445', email: 's1142445@student.hsleiden.nl' },
    { name: 'Renzo Steller', studentNr: '1154399', email: 's1154399@student.hsleiden.nl' },
    { name: 'Sander van Duffelen', studentNr: '1156708', email: 's1156708@student.hsleiden.nl' },
    { name: 'Tom Koper', studentNr: '1154680', email: 's1154680@student.hsleiden.nl' }
  ];

}

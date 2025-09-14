import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import { TranslatePipe, TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [TranslatePipe],
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss'
})
export class HeaderComponent {

  private router = inject(Router);

  constructor(private translate: TranslateService) {
    this.translate.addLangs(['nl', 'en']);
    this.translate.setDefaultLang('en');

    const browserLang = this.translate.getBrowserLang();
    this.translate.use(browserLang?.match(/nl|en/) ? browserLang : 'en');
  }

  navigateToProfile(): void {
    this.router.navigate(['profile']);
  }

  navigateToHomepage(): void {
    this.router.navigate(['']);
  }

  useLanguage(language: string): void {
    if (this.translate.getLangs().includes(language)) {
      this.translate.use(language);
    }
  }
}

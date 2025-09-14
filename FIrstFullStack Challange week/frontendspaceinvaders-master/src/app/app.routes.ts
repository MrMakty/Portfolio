import {Routes} from '@angular/router';
import {HomepageComponent} from './homepage/homepage.component';
import {DockingMapComponent} from './reservation/docking-map/docking-map.component';
import {authGuard} from './guards/auth.guard';
import {AdminComponent} from './admin/admin.component';
import {DockItemComponent} from './reservation/dock-item/dock-item.component';
import {ProfileComponent} from './profile/profile.component';
import {LoginComponent} from './auth/login/login.component';
import {RegisterComponent} from './auth/register/register.component';


export const routes: Routes = [
  {
    path: "",
    canActivate: [authGuard],
    component: HomepageComponent
  },
  {
    path: 'homepage',
    canActivate: [authGuard],
    component: HomepageComponent
  },
  {
    path: 'reserve-map',
    canActivate: [authGuard],
    component: DockingMapComponent
  },
  {
    path: 'reserve/:dockId/spaceship/:shipId',
    canActivate: [authGuard],
    component: DockItemComponent
  },
  {
    path: 'admin',
    canActivate: [authGuard],
    component: AdminComponent
  },
  {
    path: 'profile',
    canActivate: [authGuard],
    component: ProfileComponent
  },
  {
    path: 'reservations',
    canActivate: [authGuard],
    component: ProfileComponent
  },
  {
    path: 'login',
    component: LoginComponent
  },
  {
    path: 'register',
    component: RegisterComponent
  }
];

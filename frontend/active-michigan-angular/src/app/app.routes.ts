import { Routes } from '@angular/router';
import { HomeComponent } from './pages/home/home.component';
import { ActivitiesComponent } from './pages/activities/activities.component';
import { ActivityDetailComponent } from './pages/activity-detail/activity-detail.component';
import { LoginComponent } from './pages/login/login.component';
import { RegisterComponent } from './pages/register/register.component';
import { AccountComponent } from './pages/account/account.component';

export const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'activities', component: ActivitiesComponent },
  { path: 'activities/:id', component: ActivityDetailComponent },
  { path: 'account', component: AccountComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: '**', redirectTo: '' }
];

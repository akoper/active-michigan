import { Component, signal } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule, RouterLink, RouterLinkActive],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent {
  navOpen = signal(false);

  constructor(public authService: AuthService) {}

  toggleNav() {
    this.navOpen.update(v => !v);
  }

  closeNav() {
    this.navOpen.set(false);
  }

  logout() {
    this.authService.logout();
    this.closeNav();
  }
}

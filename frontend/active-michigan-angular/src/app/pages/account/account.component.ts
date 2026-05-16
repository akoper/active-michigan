import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { UsersManagementComponent } from '../../components/users-management/users-management.component';
import { UserRole } from '../../models/types';

@Component({
  selector: 'app-account',
  standalone: true,
  imports: [CommonModule, FormsModule, UsersManagementComponent],
  templateUrl: './account.component.html',
  styleUrl: './account.component.css'
})
export class AccountComponent {
  authEmail = signal('');
  authPassword = signal('');
  authDisplayName = signal('');
  authRole = signal<UserRole>('ORGANIZER');
  authMessage = signal('');

  constructor(public authService: AuthService) {}

  onRegister() {
    this.authMessage.set('');
    const payload = {
      email: this.authEmail(),
      password: this.authPassword(),
      displayName: this.authDisplayName(),
      role: this.authRole(),
    };

    this.authService.register(payload).subscribe({
      next: (res) => {
        this.authMessage.set(`Registered and signed in as ${res.email} (${res.role})`);
      },
      error: (err) => {
        this.authMessage.set(`Register failed: ${err.message}`);
      }
    });
  }

  onLogin() {
    this.authMessage.set('');
    const payload = {
      email: this.authEmail(),
      password: this.authPassword(),
    };

    this.authService.login(payload).subscribe({
      next: (res) => {
        this.authMessage.set(`Logged in as ${res.email} (${res.role})`);
      },
      error: (err) => {
        this.authMessage.set(`Login failed: ${err.message}`);
      }
    });
  }

  onLogout() {
    this.authService.logout();
    this.authMessage.set('Signed out.');
  }
}

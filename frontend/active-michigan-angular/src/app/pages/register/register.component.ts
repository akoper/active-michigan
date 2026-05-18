import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { UserRole } from '../../models/types';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {
  authEmail = signal('');
  authPassword = signal('');
  authDisplayName = signal('');
  authRole = signal<UserRole>('ORGANIZER');
  authMessage = signal('');

  constructor(public authService: AuthService, private router: Router) {}

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
        this.authMessage.set(`Registered and signed in as ${res.email}`);
        setTimeout(() => this.router.navigate(['/']), 1500);
      },
      error: (err) => {
        console.error('Registration error:', err);
        if (err.status === 0) {
          this.authMessage.set('Register failed: Cannot connect to the server. Please ensure the backend is running.');
        } else {
          this.authMessage.set(`Register failed: ${err.error?.message || err.message}`);
        }
      }
    });
  }
}

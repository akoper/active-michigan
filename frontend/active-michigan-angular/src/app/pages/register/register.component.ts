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
  authEmail = '';
  authPassword = '';
  authDisplayName = '';
  authRole = signal<UserRole>('ORGANIZER');
  authMessage = signal('');

  constructor(public authService: AuthService, private router: Router) {}

  onRegister() {
    this.authMessage.set('');
    const payload = {
      email: this.authEmail,
      password: this.authPassword,
      displayName: this.authDisplayName,
      role: this.authRole(),
    };

    this.authService.register(payload).subscribe({
      next: (res) => {
        this.authMessage.set(`Registered and signed in as ${res.email}`);
        setTimeout(() => this.router.navigate(['/account']), 1500);
      },
      error: (err) => {
        console.error('Registration error:', err);
        if (err.status === 0) {
          this.authMessage.set('Register failed: Cannot connect to the server. Please ensure the backend is running.');
        } else if (err.status === 400 && err.error?.errors) {
          // Handle Spring validation errors
          const validationErrors = err.error.errors;
          let errorMessage = 'Validation failed: ';
          if (Array.isArray(validationErrors)) {
            errorMessage += validationErrors.map((e: any) => `${e.field}: ${e.defaultMessage}`).join(', ');
          } else {
            errorMessage += JSON.stringify(validationErrors);
          }
          this.authMessage.set(errorMessage);
        } else if (err.status === 409) {
          this.authMessage.set('Register failed: This email is already registered.');
        } else {
          this.authMessage.set(`Register failed: ${err.error?.message || err.message}`);
        }
      }
    });
  }
}

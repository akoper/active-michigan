import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  authEmail = '';
  authPassword = '';
  authMessage = signal('');

  constructor(public authService: AuthService, private router: Router) {}

  onLogin() {
    this.authMessage.set('');
    const payload = {
      email: this.authEmail,
      password: this.authPassword,
    };

    this.authService.login(payload).subscribe({
      next: (res) => {
        this.authMessage.set(`Logged in as ${res.email}`);
        setTimeout(() => this.router.navigate(['/']), 1500);
      },
      error: (err) => {
        console.error('Login error:', err);
        if (err.status === 0) {
          this.authMessage.set('Login failed: Cannot connect to the server. Please ensure the backend is running.');
        } else {
          this.authMessage.set(`Login failed: ${err.error?.message || err.message}`);
        }
      }
    });
  }
}

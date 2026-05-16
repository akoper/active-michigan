import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { UserService } from '../../services/user.service';
import { AuthService } from '../../services/auth.service';
import { User, UserRole } from '../../models/types';

@Component({
  selector: 'app-users-management',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './users-management.component.html',
  styleUrl: './users-management.component.css'
})
export class UsersManagementComponent implements OnInit {
  users = signal<User[]>([]);
  message = signal('');
  form = {
    id: '',
    email: '',
    password: '',
    displayName: '',
    role: 'PARTICIPANT' as UserRole,
  };

  constructor(
    private userService: UserService,
    public authService: AuthService
  ) {}

  ngOnInit() {
    if (this.authService.isAuthenticated()) {
      this.load();
    }
  }

  load() {
    this.userService.getUsers().subscribe({
      next: (data) => {
        this.users.set(data);
        this.message.set('Loaded users.');
      },
      error: (err) => {
        this.message.set(err.message || 'Load failed');
      }
    });
  }

  createUser() {
    const payload = {
      email: this.form.email,
      password: this.form.password,
      displayName: this.form.displayName,
      role: this.form.role,
    };
    this.userService.createUser(payload).subscribe({
      next: () => {
        this.load();
        this.message.set('User created.');
      },
      error: (err) => this.message.set(err.message || 'Create failed')
    });
  }

  updateUser() {
    if (!this.form.id) return;
    const payload = {
      email: this.form.email,
      password: this.form.password || null,
      displayName: this.form.displayName,
      role: this.form.role,
    };
    this.userService.updateUser(Number(this.form.id), payload).subscribe({
      next: () => {
        this.load();
        this.message.set('User updated.');
      },
      error: (err) => this.message.set(err.message || 'Update failed')
    });
  }

  deleteUser() {
    if (!this.form.id) return;
    if (!window.confirm(`Delete user #${this.form.id}?`)) return;
    this.userService.deleteUser(Number(this.form.id)).subscribe({
      next: () => {
        this.load();
        this.message.set('User deleted.');
      },
      error: (err) => this.message.set(err.message || 'Delete failed')
    });
  }
}

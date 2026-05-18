import { Component, signal, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink, Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

import { ActivityService } from '../../services/activity.service';
import { ActivityForm, Activity } from '../../models/types';
import { ActivityFormComponent } from '../../components/activity-form/activity-form.component';

@Component({
  selector: 'app-account',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink, ActivityFormComponent],
  templateUrl: './account.component.html',
  styleUrl: './account.component.css'
})
export class AccountComponent implements OnInit {
  private router = inject(Router);
  private activityService = inject(ActivityService);
  authMessage = signal('');

  creating = signal(false);
  createMessage = signal('');
  newActivity: ActivityForm = this.emptyActivityForm();

  userActivities = signal<Activity[]>([]);
  loadingActivities = signal(false);

  constructor(public authService: AuthService) {}

  ngOnInit() {
    this.loadUserActivities();
  }

  loadUserActivities() {
    const user = this.authService.currentUser();
    if (user) {
      this.loadingActivities.set(true);
      this.activityService.getActivities({ userId: user.id }).subscribe({
        next: (res) => {
          this.userActivities.set(res.content);
          this.loadingActivities.set(false);
        },
        error: () => {
          this.loadingActivities.set(false);
        }
      });
    }
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/']);
  }

  onCreateActivity(form: ActivityForm) {
    if (!form.startsAt) {
      this.createMessage.set('Please provide a start date/time.');
      return;
    }

    this.creating.set(true);
    this.createMessage.set('');

    const payload = {
      ...form,
      startsAt: new Date(form.startsAt).toISOString(),
      endsAt: form.endsAt ? new Date(form.endsAt).toISOString() : null,
      description: form.description || null,
      region: form.region || null,
      websiteUrl: form.websiteUrl || null,
    };

    this.activityService.createActivity(payload).subscribe({
      next: () => {
        this.creating.set(false);
        this.createMessage.set('Activity created successfully.');
        this.newActivity = this.emptyActivityForm();
        this.loadUserActivities();
      },
      error: (err) => {
        this.creating.set(false);
        this.createMessage.set(`Could not create activity: ${err.message}`);
      }
    });
  }

  private emptyActivityForm(): ActivityForm {
    return {
      title: '',
      description: '',
      type: 'RUN',
      city: '',
      region: '',
      startsAt: '',
      endsAt: '',
      websiteUrl: '',
    };
  }
}

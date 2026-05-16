import { Component, OnInit, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { ActivityService } from '../../services/activity.service';
import { AuthService } from '../../services/auth.service';
import { Activity, ActivityForm, ActivityType } from '../../models/types';
import { ActivityCardComponent } from '../../components/activity-card/activity-card.component';
import { ActivityFormComponent } from '../../components/activity-form/activity-form.component';

@Component({
  selector: 'app-activities',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink, ActivityCardComponent, ActivityFormComponent],
  templateUrl: './activities.component.html',
  styleUrl: './activities.component.css'
})
export class ActivitiesComponent implements OnInit {
  // Filter states
  q = signal('');
  type = signal('');
  city = signal('');
  region = signal('');
  from = signal('');
  to = signal('');
  page = signal(0);
  pageSize = 12;

  // Data states
  activities = signal<Activity[]>([]);
  totalPages = signal(0);
  totalElements = signal(0);
  loading = signal(true);
  error = signal('');

  // Create states
  showCreate = signal(false);
  creating = signal(false);
  createMessage = signal('');
  newActivity: ActivityForm = this.emptyActivityForm();

  TYPES: ActivityType[] = ['RUN', 'BIKE', 'TRIATHLON', 'HIKE', 'PADDLE', 'SKI', 'OTHER'];

  subtitle = computed(() => {
    const total = this.totalElements();
    if (total === 0) return 'No activities found';
    const start = this.page() * this.pageSize + 1;
    const end = Math.min((this.page() + 1) * this.pageSize, total);
    return `Showing ${start}–${end} of ${total} activities`;
  });

  constructor(
    private activityService: ActivityService,
    public authService: AuthService
  ) {}

  ngOnInit() {
    this.loadActivities();
  }

  loadActivities() {
    this.loading.set(true);
    this.error.set('');

    const params: any = {
      page: this.page(),
      size: this.pageSize
    };

    if (this.q().trim()) params.q = this.q().trim();
    if (this.type()) params.type = this.type();
    if (this.city().trim()) params.city = this.city().trim();
    if (this.region().trim()) params.region = this.region().trim();
    if (this.from()) params.from = new Date(`${this.from()}T00:00:00`).toISOString();
    if (this.to()) params.to = new Date(`${this.to()}T23:59:59`).toISOString();

    this.activityService.getActivities(params).subscribe({
      next: (res) => {
        this.activities.set(res.content ?? []);
        this.totalPages.set(res.totalPages ?? 0);
        this.totalElements.set(res.totalElements ?? 0);
        this.page.set(res.number ?? this.page());
        this.loading.set(false);
      },
      error: (err) => {
        this.error.set(`Could not load activities: ${err.message}`);
        this.loading.set(false);
      }
    });
  }

  onSearch() {
    this.page.set(0);
    this.loadActivities();
  }

  clearFilters() {
    this.q.set('');
    this.type.set('');
    this.city.set('');
    this.region.set('');
    this.from.set('');
    this.to.set('');
    this.page.set(0);
    this.loadActivities();
  }

  goToPage(nextPage: number) {
    this.page.set(nextPage);
    this.loadActivities();
  }

  toggleCreate() {
    this.showCreate.update(v => !v);
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
        this.createMessage.set('Activity created.');
        this.newActivity = this.emptyActivityForm();
        this.showCreate.set(false);
        this.page.set(0);
        this.loadActivities();
      },
      error: (err) => {
        this.creating.set(false);
        this.createMessage.set(`Could not create activity: ${err.message}`);
      }
    });
  }

  emptyActivityForm(): ActivityForm {
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

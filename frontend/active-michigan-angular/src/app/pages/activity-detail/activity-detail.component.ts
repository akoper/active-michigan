import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { ActivityService } from '../../services/activity.service';
import { AuthService } from '../../services/auth.service';
import { Activity, ActivityForm } from '../../models/types';
import { ActivityFormComponent } from '../../components/activity-form/activity-form.component';

@Component({
  selector: 'app-activity-detail',
  standalone: true,
  imports: [CommonModule, RouterLink, ActivityFormComponent],
  templateUrl: './activity-detail.component.html',
  styleUrl: './activity-detail.component.css'
})
export class ActivityDetailComponent implements OnInit {
  activity = signal<Activity | null>(null);
  loading = signal(true);
  error = signal('');
  editing = signal(false);
  message = signal('');
  saving = signal(false);
  editForm: ActivityForm = this.emptyActivityForm();

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private activityService: ActivityService,
    public authService: AuthService
  ) {}

  ngOnInit() {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.loadActivity(Number(id));
    }
  }

  loadActivity(id: number) {
    this.loading.set(true);
    this.error.set('');
    this.activityService.getActivity(id).subscribe({
      next: (data) => {
        this.activity.set(data);
        this.editForm = this.activityToForm(data);
        this.loading.set(false);
      },
      error: (err) => {
        this.error.set(err.status === 404 ? 'Activity not found' : `Failed with ${err.status}`);
        this.loading.set(false);
      }
    });
  }

  onUpdate(form: ActivityForm) {
    const current = this.activity();
    if (!current) return;

    this.saving.set(true);
    this.message.set('');

    const payload = {
      ...form,
      startsAt: new Date(form.startsAt).toISOString(),
      endsAt: form.endsAt ? new Date(form.endsAt).toISOString() : null,
      description: form.description || null,
      region: form.region || null,
      websiteUrl: form.websiteUrl || null,
    };

    this.activityService.updateActivity(current.id, payload).subscribe({
      next: (updated) => {
        this.activity.set(updated);
        this.editForm = this.activityToForm(updated);
        this.editing.set(false);
        this.message.set('Activity updated.');
        this.saving.set(false);
      },
      error: (err) => {
        this.message.set(`Update failed: ${err.message}`);
        this.saving.set(false);
      }
    });
  }

  onDelete() {
    const current = this.activity();
    if (!current) return;

    if (!window.confirm(`Delete "${current.title}"?`)) return;

    this.message.set('');
    this.activityService.deleteActivity(current.id).subscribe({
      next: () => {
        this.router.navigate(['/account']);
      },
      error: (err) => {
        this.message.set(`Delete failed: ${err.message}`);
      }
    });
  }

  activityToForm(activity: Activity): ActivityForm {
    return {
      title: activity.title,
      description: activity.description ?? '',
      type: activity.type,
      city: activity.city,
      region: activity.region ?? '',
      startsAt: this.toDatetimeLocalValue(activity.startsAt),
      endsAt: activity.endsAt ? this.toDatetimeLocalValue(activity.endsAt) : '',
      websiteUrl: activity.websiteUrl ?? '',
    };
  }

  toDatetimeLocalValue(iso: string): string {
    const d = new Date(iso);
    const pad = (n: number) => String(n).padStart(2, '0');
    return `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(d.getDate())}T${pad(d.getHours())}:${pad(d.getMinutes())}`;
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

  formatWhen(startsAt: string, endsAt: string | null) {
    const start = new Date(startsAt).toLocaleString();
    if (!endsAt) return start;
    return `${start} – ${new Date(endsAt).toLocaleString()}`;
  }

  toggleEdit() {
    this.editing.update(v => !v);
  }

  onCancelEdit() {
    const current = this.activity();
    if (current) {
      this.editForm = this.activityToForm(current);
    }
    this.editing.set(false);
  }
}

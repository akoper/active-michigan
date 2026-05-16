import { Component, OnInit, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ActivityService } from '../../services/activity.service';
import { Activity } from '../../models/types';
import { ActivityCardComponent } from '../../components/activity-card/activity-card.component';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, RouterLink, ActivityCardComponent],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit {
  preview = signal<Activity[]>([]);
  loading = signal(true);

  constructor(private activityService: ActivityService) {}

  ngOnInit() {
    this.activityService.getActivities({ size: 3 }).subscribe({
      next: (body) => {
        this.preview.set(body.content ?? []);
        this.loading.set(false);
      },
      error: () => this.loading.set(false)
    });
  }
}

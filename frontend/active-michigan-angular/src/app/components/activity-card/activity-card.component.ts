import { Component, Input } from '@angular/core';
import { RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { Activity } from '../../models/types';

@Component({
  selector: 'app-activity-card',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './activity-card.component.html',
  styleUrl: './activity-card.component.css'
})
export class ActivityCardComponent {
  @Input({ required: true }) activity!: Activity;

  formatWhen(startsAt: string, endsAt: string | null) {
    const start = new Date(startsAt).toLocaleString();
    if (!endsAt) return start;
    return `${start} – ${new Date(endsAt).toLocaleString()}`;
  }
}

import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivityForm, ActivityType } from '../../models/types';

@Component({
  selector: 'app-activity-form',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './activity-form.component.html',
  styleUrl: './activity-form.component.css'
})
export class ActivityFormComponent implements OnInit {
  @Input() form: ActivityForm = this.emptyActivityForm();
  @Input() submitLabel = 'Submit';
  @Input() submitting = false;
  @Input() showCancel = false;

  @Output() formSubmit = new EventEmitter<ActivityForm>();
  @Output() cancel = new EventEmitter<void>();

  TYPES: ActivityType[] = ['RUN', 'BIKE', 'TRIATHLON', 'HIKE', 'PADDLE', 'SKI', 'OTHER'];

  ngOnInit() {}

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

  onSubmit() {
    this.formSubmit.emit(this.form);
  }

  onCancel() {
    this.cancel.emit();
  }
}

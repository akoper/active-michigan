export type ActivityType = 'RUN' | 'BIKE' | 'TRIATHLON' | 'HIKE' | 'PADDLE' | 'SKI' | 'OTHER';

export interface Activity {
  id: number;
  title: string;
  description: string | null;
  type: ActivityType;
  city: string;
  region: string | null;
  startsAt: string;
  endsAt: string | null;
  websiteUrl: string | null;
  user?: User | null;
}

export interface ActivityPage {
  content: Activity[];
  totalPages: number;
  totalElements: number;
  number: number;
}

export type UserRole = 'PARTICIPANT' | 'ORGANIZER' | 'ADMIN';

export interface ActivityForm {
  title: string;
  description: string;
  type: ActivityType;
  city: string;
  region: string;
  startsAt: string;
  endsAt: string;
  websiteUrl: string;
}

export interface AuthResponse {
  token: string;
  email: string;
  role: UserRole;
  displayName: string;
  id: number;
}

export interface User {
  id: number;
  email: string;
  displayName: string;
  role: UserRole;
}

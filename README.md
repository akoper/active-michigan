## Active Michigan (MVP)

Active Michigan is a directory of outdoor activities and competitions across Michigan (runs, bike races, triathlons, hikes, paddle events, ski, and more). Anyone can **browse and search** by type, location, and date. **Organizers** can create and edit events; **admins** can manage user accounts.

### Repo layout

- `backend/active-michigan-api/` — Spring Boot REST API (port 8080)
- `frontend/active-michigan-angular/` — Angular 21 web app (port 4200)

### Features

- Public activity search with filters (keyword, type, city, region, date range) and pagination
- Activity detail pages with edit and delete (organizer/admin, signed in)
- JWT authentication with session persisted in the browser
- Admin user CRUD

### Local development

**Prerequisites:** Java 17+, Node.js 18+

**Backend**

```bash
cd backend/active-michigan-api
./mvnw spring-boot:run
```

On Windows:

```powershell
cd backend\active-michigan-api
.\mvnw.cmd spring-boot:run
```

API: http://localhost:8080  
H2 console (dev): http://localhost:8080/h2

**Frontend**

```bash
cd frontend/active-michigan-angular
npm install
npm start
```

Web: http://localhost:4200

### Deployment to GCP

The project is configured for deployment to **Google Cloud Platform (GCP)** using **Cloud Run** and **Cloud Build**.

**Prerequisites:**
1. [Google Cloud SDK](https://cloud.google.com/sdk/docs/install) installed and initialized (`gcloud init`).
2. A GCP Project created and selected (`gcloud config set project [PROJECT_ID]`).
3. Cloud Run and Cloud Build APIs enabled in your GCP project.

**To deploy:**

Run the following command from the project root:

```bash
gcloud builds submit --config cloudbuild.yaml .
```

This will:
- Build Docker images for both backend and frontend.
- Push them to Google Container Registry.
- Deploy them as separate services to Cloud Run.

**Post-deployment notes:**
- The backend uses an in-memory H2 database by default. For persistence, configure a Cloud SQL instance in `backend/active-michigan-api/src/main/resources/application-prod.properties`.
- The frontend is configured to use `/api` as the base URL. If you deploy them to different domains, you may need to update `environment.prod.ts` or configure a Load Balancer/Proxy.

### Seeded data (dev)

On first startup the API seeds five sample Michigan activities and an admin account:

- **Email:** `admin@activemichigan.local`
- **Password:** `admin12345`

Register a new account with role **ORGANIZER** to create and edit activities without using the admin account.

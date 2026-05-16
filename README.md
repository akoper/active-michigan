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

### Seeded data (dev)

On first startup the API seeds five sample Michigan activities and an admin account:

- **Email:** `admin@activemichigan.local`
- **Password:** `admin12345`

Register a new account with role **ORGANIZER** to create and edit activities without using the admin account.

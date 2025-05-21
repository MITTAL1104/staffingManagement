## 📘 Project Title: Staff Allocation Management System
#
## 🧾 Project Overview

This is a **full-stack web application** for managing employee allocations to various projects. It supports access control based on the role of the user (admins and normal employees), secure login using JWT and HTTP-only cookies, and Excel export functionality and much more.

## 🧱 Architecture

- 🖥️ **Frontend**: React.js
- ⚙️ **Backend**: Java 11, Spring Boot 2.7.3(with JDBC)
- 🛢️  **Database**: Microsoft SQL Server
- 🔐 **Authentication**: JWT (stored in HTTP-only cookies)
- 🧩 **Microservices**:
```
| Service            | Responsibilities                              |
|--------------------|-----------------------------------------------|
| employee_service   | Manage employee data                          |
| project_service    | Manage project data                           |   
| allocation_service | Manage allocations                            |
| staff_service      | Handles login/registration/auth               |
```

## 🚀 Features
- 👨 User registration with verification flow
- 🔑 JWT-based login (stored in secure cookies)
- 💼 Access control based upon role (Admin vs User)
- 📖 CRUD operations on Employees, Projects and Allocations
- 📥 Download data in Excel using multithreading
- 🗓️ No overlapping allocations for the same employee
- 🗑️ Soft Delete functionality
- 🔓 Logout invalidates JWT token in DB + clears cookie
- 🔖 Clean separation between normal users and admin dashboards

## 🔐 Authentication Flow
- **JWT Generation**: On successful login, JWT token is generated and stored in tokens_staff table.
- **Storage**: Sent as HTTP-only cookie to frontend.
- **Logout**: Deletes JWT from DB and clears browser cookie.
- `JWT` contains:
   - `email`
   - `employeeId`
   - `isAdmin flag`
## 💼 Role-Based Access
- An `Admin` can manage all employees, projects, and allocations                 
- An `User` can only view/edit their own allocations and personal details

## 📂 Project Structure (Backend)

```
├── staff_service
│   ├── controller (StaffController)
|   ├── config (SecurityConfig)
│   ├── security (JwtUtil, JwtFilter)
│   ├── service (AuthService)
│   ├── dao (UserCredentialsDAO,TokenDAO)
│   └── entity (Tokens,UserCredentials)
├── employee_service
├── project_service
└── allocation_service
```

## 📂 Project Structure (Frontend)

```
├── staff-frontend
├── public
│   ├── assets
│   ├── index.html
├── src
│   ├── api
|   ├── components
│   ├── pages
│   ├── styles
├── App.js
├── index.js
└── Layout.js
```

## 🛠️ Getting Started

### 📦 Clone the Project

```
git clone https://github.com/{username}/staff-allocation-system.git
cd staff-allocation-system
```

### 🎨 Frontend Setup (React)

🗺️ *Navigate to frontend folder*
```
cd frontend
```
📥  *Install Node Modules*
```
npm install
```
🔧 *Configure API Endpoints*

Update the base API URL in your frontend code (create a .env file and write below code in it)
```
REACT_APP_API_BASE_URL={your_backend_url}
```
🚀 *Run the Frontend*
```
npm start
```
> **Frontend will run on `http://localhost:3000`**

### ⚙️ Backend Setup (Spring Boot)

🗺️ *Navigate to backend folder*
```
cd backend
```
🔧 *Update your application.properties to configure the database*

Fill with correct SQL Server Credentials and the port on which you want to run in all the 4 services
```
spring.datasource.url={DB_URL}
spring.datasource.username=your_username={DB_USERNAME}
spring.datasource.password=your_password={DB_PASSWORD}
server.port={PORT_NUMBER}
```
- Ensure the database is created in SQL Server and necessary tables are set up either manually or via application logic.
- Make sure that the port numbers entered should be available on the machine

🚀 *Run the Backend*

Run all the 4 services seperately 

**The services will start on the respective ports depending upon the microservice configuration**

### ✅ Success

Now you can open your browser and go to:

```
http://{FRONTEND_URL}
```
to start using the application!





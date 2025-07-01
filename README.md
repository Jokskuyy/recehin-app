# Recehin

Recehin is a financial management application designed to help users manage their finances efficiently. The project consists of a backend (Node.js/Express) and a frontend (Android) application.

## Features
- User authentication (register, login, password reset)
- Transaction and bill management
- Financial analytics and reports
- Profile management
- Educational articles about saving and investing

## Project Structure
```
recehin-app/
  ├── app/                # Android application (frontend)
  └── recehin-backend/    # Node.js/Express backend API
```

## Prerequisites
- **Backend:**
  - Node.js (v14 or higher)
  - npm
  - MySQL Server (local or cloud instance)
- **Frontend:**
  - Android Studio
  - Android SDK

## Backend Setup (Node.js/Express)
1. Navigate to the backend directory:
   ```bash
   cd recehin-backend
   ```
2. Install dependencies:
   ```bash
   npm install
   ```
3. Make sure MySQL server is running and create a database for the project.
4. Configure environment variables as needed (e.g., MySQL connection settings in `config/database.js`).
5. Start the backend server:
   ```bash
   npm start
   ```
   The backend will run on the default port (e.g., 3000).

## Frontend Setup (Android)
1. Open the `app` directory in Android Studio.
2. Let Gradle sync and download dependencies.
3. Connect an Android device or start an emulator.
4. Build and run the application from Android Studio.

## Usage
- Register or log in to your account.
- Add, view, and manage your transactions and bills.
- View analytics and reports on your financial activities.
- Access educational articles to improve your financial literacy.

## License
This project is for educational purposes.
 

# OpenTracker

A modern Android fitness and nutrition tracking app built with Jetpack Compose.

## Features

### 📊 Dashboard
- Daily calorie summary with progress visualization
- Quick overview of meals, workouts, and water intake
- Calorie goal tracking

### 🍽️ Meal Logging
- Log meals with calorie and nutritional information
- Search food database
- Save favorite meals
- Track daily calorie intake

### 🏋️ Workout Tracking
- Log workouts and exercises
- Track sets, reps, and weights
- Pre-loaded exercise library
- View workout history

### 💧 Water Intake
- Quick water intake logging
- Daily hydration goals
- Visual progress tracking

### 🤖 AI Assistant
- Ask nutrition and fitness questions
- Get personalized recommendations
- Powered by Gemini AI

### 🔐 Authentication
- Secure login with Auth0
- Session persistence
- User profile management

## Tech Stack

- **Language**: Kotlin
- **UI**: Jetpack Compose with Material Design 3
- **Architecture**: MVVM + Clean Architecture
- **DI**: Hilt
- **Database**: Room
- **Authentication**: Auth0
- **AI**: Google Gemini API
- **Min SDK**: 26 (Android 8.0)
- **Target SDK**: 34 (Android 14)

## Getting Started

### Prerequisites

- Android Studio (2023+)
- JDK 17+
- Android SDK 34

### Configuration

1. Copy the sample credentials file:
   ```
   cp credentials.properties.sample credentials.properties
   ```

2. Add your credentials to `credentials.properties`:
   ```properties
   AUTH0_CLIENT_ID=your_auth0_client_id
   AUTH0_DOMAIN=your-tenant.auth0.com
   GEMINI_API_KEY=your_gemini_api_key
   ```

3. Configure Auth0:
   - Create a Native App in your Auth0 dashboard
   - Add callback URL: `com.indiancalorietracker://your-tenant.auth0.com/android/com.indiancalorietracker/callback`
   - Add logout URL: `com.indiancalorietracker://your-tenant.auth0.com/v2/logout`

### Build

```bash
./gradlew assembleDebug
```

The APK will be generated at `app/build/outputs/apk/debug/app-debug.apk`

## Project Structure

```
app/src/main/java/com/indiancalorietracker/
├── data/
│   ├── local/
│   │   ├── dao/           # Room DAOs
│   │   └── entity/        # Room Entities
│   └── repository/        # Repository Implementations
├── di/                    # Hilt Modules
├── domain/
│   ├── model/             # Domain Models
│   └── repository/        # Repository Interfaces
├── service/               # Auth0 Service
└── ui/
    ├── navigation/        # Compose Navigation
    ├── screens/           # UI Screens
    │   ├── ai/           # AI Chat
    │   ├── auth/         # Authentication
    │   ├── history/      # History
    │   ├── home/        # Dashboard
    │   ├── log/         # Meal Logging
    │   ├── profile/     # User Profile
    │   ├── search/      # Food Search
    │   ├── settings/    # Settings
    │   ├── water/       # Water Tracking
    │   └── workout/    # Workout Tracking
    └── theme/           # Compose Theme
```

## License

MIT License

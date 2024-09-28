# RoseDine: Personalized Meal Recommendation System

## Overview

**RoseDine** is a full-stack meal recommendation system designed to offer personalized meal suggestions based on a user's macros and dietary preferences. Built with **Spring Boot** for the back end and **Flutter** for the front end, RoseDine integrates data from Rose-Hulman’s daily menu to suggest meals tailored to individual nutritional needs.

This system helps users manage their dietary goals by providing a seamless, user-friendly interface to monitor their macros and receive meal suggestions that align with their health objectives. RoseDine offers a secure and intuitive experience for personalized meal planning.

## Features

- **Personalized Meal Recommendations**: Suggests meals based on the user's dietary preferences and macro requirements (proteins, fats, carbs).
- **Macro Tracking**: Calculates and displays daily macros for the user, allowing for better meal planning.
- **Real-time Menu Integration**: Scrapes daily menus from Rose-Hulman’s website, keeping the data fresh and up to date.
- **User Authentication**: Secured with OAuth 2.0 for safe and reliable login and user data protection.
- **Notifications**: Sends email alerts to users via SendGrid API, such as updates on new meal options.
- **Mobile-Friendly UI**: Designed with **Flutter** and uses **Riverpod** for efficient state management, ensuring real-time updates and a smooth user experience across devices.
- **Continuous Integration/Continuous Deployment (CI/CD)**: Automated testing and deployment via Jenkins for streamlined updates and bug fixes.

## Tech Stack

### Back-end: 
- **Spring Boot**: Handles API requests and manages business logic, including meal recommendations and macro calculations.
- **MS SQL Server**: Stores user data, meal details, and macros securely.
- **OAuth 2.0**: Provides secure authentication and authorization.
- **SendGrid API**: Delivers email notifications to users.

### Front-end: 
- **Flutter**: Cross-platform mobile development, ensuring consistent user experience on both iOS and Android.
- **Riverpod**: State management for responsive and real-time UI updates.

### CI/CD Pipeline:
- **Jenkins**: Automates the testing, integration, and deployment processes to ensure smooth continuous delivery.

## Installation

### Prerequisites

- **Java 11** or higher
- **Spring Boot** (for the back-end service)
- **Flutter SDK** (for the mobile app)
- **MS SQL Server** (for database management)

### Back-end Setup (Spring Boot)

1. Clone the repository:
   ```bash
   git clone https://github.com/username/rosedine-backend.git

2. Configure the `application.properties` file with your database credentials and API keys:
   ```properties
   spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=roseDineDB
   spring.datasource.username=your-username
   spring.datasource.password=your-password
   sendgrid.api.key=your-sendgrid-api-key
   ```

3. Build the project:
   ```bash
   ./mvnw clean install
   ```

4. Run the Spring Boot application:
   ```bash
   ./mvnw spring-boot:run
   ```

## Front-end Setup (Flutter)

1. Clone the front-end repository:
   ```bash
   git clone https://github.com/username/rosedine-frontend.git
   ```

2. Navigate to the project directory:
   ```bash
   cd rosedine-frontend
   ```

3. Install the necessary Flutter dependencies:
   ```bash
   flutter pub get
   ```

4. Run the Flutter app:
   ```bash
   flutter run
   ```

## API Endpoints

### User Endpoints
- **GET /api/user/preferences**: Fetch user’s dietary preferences.
- **POST /api/user/preferences**: Update dietary preferences for personalized recommendations.

### Meal Recommendation Endpoints
- **GET /api/meal/recommendations**: Get meal recommendations based on current macros and preferences.
- **GET /api/menu/today**: Fetch today’s menu options from Rose-Hulman’s dining services.

### Authentication Endpoints
- **POST /api/auth/login**: User login with OAuth 2.0.
- **POST /api/auth/register**: New user registration.

## Future Enhancements
- **Nutrition Tracking Integration**: Allow users to manually log meals and adjust macros based on actual intake.
- **Advanced Filters**: Add options for dietary restrictions (e.g., gluten-free, vegan).
- **Machine Learning**: Use machine learning to improve recommendation accuracy based on user history.

## Contributing
We welcome contributions! Please follow these steps:

1. Fork the repository.
2. Create a new branch:
    ```bash
    git checkout -b feature-branch
    ```
3. Make your changes.
4. Commit the changes:
    ```bash
    git commit -m 'Add some feature'
    ```
5. Push to the branch:
    ```bash
    git push origin feature-branch
    ```
6. Create a pull request.

## License
This project is licensed under the MIT License. See the LICENSE file for details.

**RoseDine** is all about making personalized meal planning easier and more effective. Feel free to contribute or reach out for any questions!


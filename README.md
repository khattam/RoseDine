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

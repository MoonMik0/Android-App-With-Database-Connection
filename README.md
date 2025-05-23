 📅 Android Date-Driven Data Viewer App
## 📱 Project Description
This is an Android application developed in Kotlin that connects to a FastAPI backend with a MySQL database to allow authenticated users to view and analyze date-based records from various database tables.
The application supports:
-	User login authentication
-	Dynamic table selection
-	Detailed table content viewing based on selected dates
All data interactions are handled via RetrofitClient through secure API endpoints.

## 🧰 Technologies Used
-	Android Studio (UI built with XML)
-	Kotlin for Android development
-	RetrofitClient for API communication
-	FastAPI (Python) for backend development
-	MySQL as the relational database

## 🔐 Features
-	🔑 User Authentication: Secure login with credential verification via API.
-	📋 Dynamic Table Selection: Users are shown a list of table names retrieved from the database.
-	🔎 Date-Based Record Viewing: Upon selecting a table, users can view detailed content sorted or filtered by date.
-	🔄 Real-Time API Integration: All data is fetched live from a FastAPI backend with optimized performance.

## 🚀 How to Run the App
1.	Clone the repository:
2.	Open in Android Studio
3.	Ensure the FastAPI server is running:

  	`uvicorn api:app --reload`
5.	Run the app on an emulator or physical device.

## Developer
Özlem ELMALI

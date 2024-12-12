# Crous_Project_Front

<img src="https://upload.wikimedia.org/wikipedia/commons/e/e0/Logo_Crous_vectoris%C3%A9.svg" alt="Crous Logo" width="80" />
<img src ="https://alternance.imt.fr/wp-content/uploads/2020/11/logo_imt_st_etienne-verticale-223x300.jpg" alt="ISMIN Logo" width ="20" />

**This project was done by Alex El Chidiac and Elias Ibrahim and presented to Mr. Gaetan Maisse for the Web Dev and Android Development course during Semester 9 at ISMIN.**

## Overview

Crous_Project_Front is an Android application designed to display and manage data about Crous restaurants or facilities. Users can view Crous details in a list, mark certain establishments as favorites, visualize their locations on a map, and even add new Crous items using a built-in form. The app interfaces with a remote server using a RESTful API, handles pagination in the listing screen, and provides a user-friendly UI with tabs and bottom navigation.

**Key Features:**
- **List of Crous:** Displays a paginated list of restaurants/facilities fetched from a remote server.
- **Favorites:** Mark/unmark any Crous as a favorite for quick access.
- **Map Integration:** Visualize all available Crous locations directly on a Google Map.
- **Add New Crous:** Use a dedicated form to add new Crous entries to the server.
- **Info Tab:** An informational tab to present static or dynamic content relevant to the application.
- **API Integration:** Uses Retrofit for seamless integration with a backend API.
- **Pagination:** Easily navigate through large lists using "Next" and "Previous" controls.

## Prerequisites

- Android Studio (latest stable version preferred)
- Android SDK and necessary build tools
- Gradle
- A device or emulator running at least Android API 21 (Lollipop) or above
- Internet connection for API calls and map functionality (Google Maps)

## Getting Started

1. **Clone the repository:**
   ```bash
   git clone https://github.com/elias972/Crous_Project_Front.git

2. **Interacting with the App:**
   -**List Tab:** View a paginated list of Crous entries fetched from the server.

   -**Map Tab:** Visualize all Crous locations on an interactive Google Map.

   -**Info Tab:** View additional information about the app or related content.

   -**Add (Bottom Navigation):** Add a new Crous entry by filling out the form.

   -**Favorite (Bottom Navigation):** Filter the list to show only the Crous items you've marked as favorites.

4. **Project Structure:**
**Activities:**

   **-MainActivity.kt:** Manages tabs, bottom navigation, and fragment transactions.

   **-DetailActivity.kt:** Displays detailed information about a selected Crous.

**Fragments:**

   **-ListFragment.kt:** Shows a paginated list of Crous items.
   
   **-MapFragment.kt:** Displays Crous locations on a Google Map.
   
   **-InfoFragment.kt:** Presents general or dynamic information.
   
   **-CrousAddFragment.kt**: Allows users to add new Crous entries.

## API Integration
The project uses Retrofit to interact with a backend server:

-Base URL: https://app-38070179-aa49-47be-b530-6fece93917b4.cleverapps.io/

-Check CrousService.kt for endpoints and model definitions.




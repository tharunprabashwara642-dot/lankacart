# LankaJobs — Sri Lankan Job Discovery & Advertisement Platform (Android)

LankaJobs is a modern, light-first native Android application built with Kotlin, Jetpack Compose, Material 3, and Room local persistence. It is designed from scratch to serve as a fast job discovery app and verified advertisement platform for Sri Lankan jobseekers and employers.

---

## 📱 Features Implemented

1. **Home Screen**:
   - Modern brand header with personalized greeting ("Hi, [First Name]")
   - Quick search launcher bar
   - Horizontal category pills with live job counts
   - Featured Jobs carousel with verified badges
   - Latest Jobs vertical feed with salary ranges, employment tags, and bookmarking
   - Approved Sponsored Announcement cards

2. **Search & Comprehensive Filters**:
   - Instant search across job title, keywords, company names, skills, and locations
   - Quick filter chips (Remote Only, Full-time, Colombo)
   - Filter Modal Bottom Sheet:
     - Sorting: Newest First, Highest Salary, Most Relevant
     - Workplace Type: Remote, Hybrid, On-site
     - Sri Lankan Districts/Locations: Colombo, Kandy, Galle, Gampaha, Kurunegala, Negombo, Jaffna, etc.
     - Employment Type: Full-time, Part-time, Contract, Internship, Temporary
     - Experience Levels: Entry Level, Junior, Mid Level, Senior, Manager
     - Minimum Monthly Salary Threshold (LKR 50,000+ to 300,000+)
   - Real-time active filter count and reset functionality

3. **Job Details**:
   - Job title, company name, location, and verified badges
   - Salary range display (LKR) with negotiable indicators
   - Comprehensive Job Description, Key Responsibilities list, Requirements list, and Benefits tags
   - Verified Employer Source transparency card
   - Native Share intent to share opportunities
   - Sticky bottom action bar with offline Bookmark Save and prominent "Apply Now" (directly opens the verified external employer portal)

4. **Category Jobs View**:
   - Filtered view for specific categories with job counts and quick access

5. **Saved Jobs (Offline-First Persistence)**:
   - Persisted locally using Room database
   - Search within saved jobs
   - Instant bookmark toggling and removal

6. **Authentication & User Profile**:
   - Google Sign-In ready architecture
   - Mandatory **Complete Your Profile** onboarding flow (collects Full Name, Phone Number, Preferred District, and Job Categories)
   - Edit Profile management
   - Security and privacy transparency notices

7. **Advertisement System**:
   - Browse approved announcements, diplomas, career fairs, and walks-in
   - "Visit Official Website" button is rendered **only** when a valid website URL was provided by the advertiser
   - Direct call/phone inquiry action
   - Multi-step **Create Advertisement** flow:
     - Title, Organization, Description, Category, Location, Contact Phone, Website URL
     - Advertising Package selection (Starter LKR 2,500, Professional LKR 6,000, Featured Enterprise LKR 12,500)
     - Strict Admin Approval State: All user-submitted advertisements are assigned `PENDING_APPROVAL` status
   - **My Advertisements** screen to track approval statuses

8. **Notification Preferences**:
   - Granular toggles for New Jobs, Featured Jobs, Approved Announcements, and Saved Job closing updates

---

## 🛠️ Architecture & Tech Stack

- **UI**: Jetpack Compose with Material 3 design tokens
- **Architecture**: MVVM with Clean Architecture separation
  - `presentation/`: Compose screens, ViewModels, navigation, and reusable components
  - `domain/`: Business entities (`Job`, `Advertisement`, `UserProfile`, `JobFilter`, etc.) and Repository interfaces
  - `data/`: Room entities, DAOs, Database, Mappers, and Repository implementations
  - `core/`: Dependency injection container (`AppContainer`)
- **Local Persistence**: Android Room database (`LankaJobsDatabase`)
- **Asynchrony**: Kotlin Coroutines & Flow (`StateFlow`, `combine`, `collectAsStateWithLifecycle`)
- **Images**: Coil for Compose
- **Strict Separation**: **Zero in-app admin screens or credentials**. All administrative management belongs exclusively to the future separate Web Admin Panel.

---

## 🚀 How to Build & Run

### 1. Requirements
- Android Studio Ladybug / Meerkat or later
- JDK 17 or later
- Android SDK Platform 36 (minSdk 24)

### 2. Build Debug APK
```bash
gradle assembleDebug
```
The compiled debug APK will be generated at:
`app/build/outputs/apk/debug/app-debug.apk`

### 3. Build Release APK
```bash
gradle assembleRelease
```
The signed release APK will be generated at:
`app/build/outputs/apk/release/app-release.apk`

### 4. Run Unit Tests
```bash
gradle :app:testDebugUnitTest
```

---

## 🔮 Future Supabase & Payment Integration Steps

The application has been engineered so that connecting Supabase and payments requires **zero UI rewrites**:

1. **Supabase Remote Data Source**:
   - Add the Supabase Kotlin SDK to `libs.versions.toml`
   - Implement `SupabaseRemoteDataSource` querying tables: `jobs`, `categories`, `advertisements`, `profiles`
   - In `JobRepositoryImpl` and `AdvertisementRepositoryImpl`, fetch from `SupabaseRemoteDataSource` and write to Room DAOs
   - The UI will continue observing the existing Room `Flow` streams seamlessly.

2. **Payments (PayHere / Gateway)**:
   - When user selects an advertising package and taps submit, trigger the payment order gateway
   - Once payment is verified by the backend, state transitions to `PAYMENT_RECEIVED` -> `PENDING_APPROVAL` for admin review in the Web Admin Panel.

3. **Google Sign-In with Credential Manager**:
   - Add `credentials-play-services-auth` and `googleid` dependencies
   - Supply Google Web Client ID in the AI Studio Secrets panel / `.env`
   - Exchange Google ID Token with Supabase Auth `signInWithIdToken`

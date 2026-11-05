# LEVELUP — Real Life RPG

**Your Life. Your Game. Your Level.**

LEVELUP is a native Android personal-growth RPG. Turn real-world goals into quests, earn XP, grow character attributes, and build repeatable habits.

## Current feature set

- RPG-style dark dashboard with player level and XP progress
- Daily quests with Easy, Normal, Hard, and Epic rewards
- Create custom quests with a difficulty and character attribute
- Quest rewards can be claimed once per local calendar day
- Five attributes: Strength, Intelligence, Wellness, Discipline, and Creativity
- Attribute XP is awarded according to each quest's category
- Create custom habits and check them off daily
- Habit check-ins are stored by date, so a new day starts with a fresh checklist
- Weekly habit consistency summary (check-ins over the last seven days)
- Offline-first Room database and observable ViewModel state
- Database migrations for the evolving local schema

## Technology

- Kotlin
- Jetpack Compose + Material 3
- MVVM-style ViewModel and repository
- Room + Kotlin Flow
- KSP for Room code generation
- Gradle Kotlin DSL
- JUnit

## Project setup

1. Install Android Studio and JDK 17.
2. Install Android SDK Platform 36.
3. Open this repository in Android Studio.
4. Use Gradle 8.11.1 for the project.
5. Sync Gradle and run the `app` configuration on an API 26+ emulator or device.

The GitHub Actions workflow also installs Gradle 8.11.1, builds the debug APK, runs unit tests, and executes Room instrumentation tests on an Android 16 emulator for pushes to `main`.

## Package details

- Application ID: `com.novara.levelup`
- Minimum Android: API 26
- Target SDK: 36
- Current version: 0.3.0

## Repository layout

```
app/src/main/java/com/novara/levelup/
  data/       Room entities, DAO, database, repository
  domain/     XP and progression rules
  ui/         Compose screens, theme, ViewModel
docs/         Product, mechanics, development and release notes
.github/      Android CI workflow
```

## Current limitations

This is an actively developed MVP, not yet a Play Store release. Habit check-ins are saved by date, but consecutive-day streaks and a full calendar history still need a dedicated UI. There is no account, cloud backup, social leaderboard, payment, analytics, or notification system. Attribute levels currently advance in 100-XP steps within each attribute.

## Product principles

- Reward consistency, not unhealthy overwork.
- Allow rest days; never shame users for missed days.
- Keep progress understandable and user-controlled.
- Protect progress with transactional reward logic.
- Keep the core experience usable offline.

## License

MIT. See [LICENSE](LICENSE).

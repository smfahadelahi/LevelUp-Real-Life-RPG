# Development Notes
## Local requirements
- Android Studio and Android SDK Platform 36
- JDK 17
- Gradle 8.11.1
- Android Gradle Plugin 8.10.0
- Kotlin 2.1.21 and KSP 2.1.21-2.0.1
- Android emulator or physical device (API 26+)

## Data architecture
Room is the source of truth for player, quest, and habit records. A repository exposes observable Flows. The ViewModel combines those Flows into UI state. Quest claiming is a Room transaction: the quest completion update must affect exactly one uncompleted row before XP is awarded, preventing repeat claims.

## Current limitations
- Starter quests and habits are seeded on first launch.
- Habit check-ins store a current completion flag; calendar history and streak calculations are not implemented.
- Character attribute progression is a visual placeholder pending attribute-specific quest rewards.
- There are no accounts, cloud backup, payments, ads, or public leaderboards.
- CI is configured; check the first GitHub Actions run before treating the build as verified.

## Database library
Room 2.8.5 is used for SQLite persistence and KSP-based DAO generation.

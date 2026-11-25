# Phonics Garden 🌱🐝

An Android educational app that helps early learners (ages 3-6) practise letter sounds through short
minigames. Each sound a child masters grows a flower in their garden.

Built for CP3406 / CP5307 Assessment 3 using Kotlin, Jetpack Compose and Material 3.

## Current features
- **Garden landing page** showing progress as flowers, with shortcuts to the games
- **Bubble Pop**: hear a sound (text-to-speech, en-AU) and pop the matching letter
- **Feed the Monster**: hear a sound, then drag (or tap) the picture that starts with it onto the monster
- **Tracing Garden**: trace lowercase letters in the correct stroke order; a flower blooms when done
- **Progress screen**: per-sound mastery and overall accuracy
- **Settings**: phonics phase, sound on/off, reduce motion (saved with DataStore)
- Spaced-repetition (Leitner boxes) decides which sounds appear more often
- Offline-first: bundled starter data, optional word refresh from the Datamuse API

## Planned
- Parent gate for Progress and Settings
- Recorded phoneme audio

## Architecture
```
Compose Screen -> ViewModel (StateFlow) -> Repository -> Room / Retrofit
```
- `engine/`: plain Kotlin game logic (ChallengeGenerator, MasteryScheduler, TraceValidator)
- `data/`: Room entities and DAOs, Retrofit API, repositories
- `di/`: Hilt modules
- `ui/`: Compose screens, ViewModels and type-safe navigation

## Ethics and privacy by design
- No accounts, no ads, no analytics, no tracking
- All progress stored on the device only (backups disabled)
- Network used only to fetch public word lists; no personal data sent
- Gentle feedback (no buzzers, timers, streaks or guilt mechanics)
- Natural session endings with break prompts
- Words are only shown to children if they are in the bundled picture allowlist; network words without a picture are never displayed
- Games work by tap as well as drag (motor and screen-reader accessibility); reduce-motion setting respected

## Running the tests
`./gradlew test` (JVM unit tests for the engine package)

## Inspiration and credits
- Game-hub concept inspired by Phonics & Stuff (https://phonicsandstuff.com/games). No assets or code copied.
- Datamuse API (https://www.datamuse.com/api/)

## AI use declaration
Add your Declaration of AI-Generated Material here and submit it with the assignment.

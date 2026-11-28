# Phonics Garden 🌱🐝

An Android educational app that helps early learners (ages 3-6) practise letter sounds through short
minigames. Each sound a child masters grows a flower in their garden.

Built for CP3406 / CP5307 Assessment 3 using Kotlin, Jetpack Compose and Material 3.

## Current features
- **Garden landing page** showing progress as flowers, with shortcuts to the games
- **Bubble Pop**: hear a sound (text-to-speech, en-AU) and pop the matching letter as bubbles drift slowly upwards. With "Reduce motion" or a screen reader on, the bubbles hold still in a grid instead
- **Feed the Monster**: hear a sound, then drag (or tap) the picture that starts with it onto the monster
- **Tracing Garden**: trace lowercase letters in the correct stroke order; a flower blooms when done
- **Sound effects:** a bubble pop, monster chomp, flower bloom and round-complete fanfare (SoundPool). Wrong answers get a soft, quiet boop, never a buzzer, and the parent's Sound setting mutes both voice and effects
- **Progress screen**: per-sound mastery and overall accuracy (protected by Parent Gate)
- **Settings**: phonics phase, sound on/off, reduce motion (saved with DataStore, protected by Parent Gate)
- **Parent Gate**: adult verification math challenge to protect adult settings and statistics; it re-locks as soon as the adult leaves those screens
- Spaced-repetition (Leitner boxes) decides which sounds appear more often
- **Online word packs:** new words and pictures are downloaded from `content/word_pack.json` in this repo (Retrofit + kotlinx.serialization), validated on the device, and cached in Room. Works offline using the bundled words.
- **Progress screen:** a "last 7 days" chart and a "practise next" list, from a per-day SQL query (no streaks or targets)

## Planned
- Recorded phoneme audio

## Architecture
```
Compose Screen -> ViewModel (StateFlow) -> Repository -> Room / Retrofit
```
- `engine/`: plain Kotlin game logic (ChallengeGenerator, WordChallengeGenerator, MasteryScheduler, TraceValidator, LetterShapes, BubbleField, ParentGateValidator)
- `util/`: `Speaker` (TextToSpeech) and `SoundEffects` (SoundPool) interfaces with Android implementations, swapped for fakes in tests
- `data/`: Room entities, DAOs and migrations (schema v2), Retrofit word pack API, repositories
- `di/`: Hilt modules
- `ui/`: Compose screens, ViewModels and type-safe navigation

## Ethics and privacy by design
- No accounts, no ads, no analytics, no tracking
- All progress stored on the device only (backups disabled)
- Network is used only to download a public word list from this repository; nothing about the child is ever sent
- Gentle feedback (no buzzers, timers, streaks or guilt mechanics)
- Natural session endings with break prompts
- Downloaded content is never trusted: each word is validated (plain lowercase letters, a taught first letter, a short emoji picture with no text) before it is stored, and the database will not accept a word without a picture
- Games work by tap as well as drag (motor and screen-reader accessibility); reduce-motion setting respected

## Testing
- **Model and ViewModel tests (JVM):** `./gradlew test`. Covers the game engine (challenge generation, mastery scheduling, trace validation, letter shapes, parent gate maths), seed data integrity, and every ViewModel using fake repositories and a fake `Speaker`.
- **GUI tests (Compose UI, instrumented):** `./gradlew connectedDebugAndroidTest` with an emulator or device running. Covers the landing page, settings, parent gate dialog and round complete screens. Screens are written with state hoisting (`LandingContent`, `SettingsContent`) so they can be tested without Hilt.

## Inspiration and credits
- Game-hub concept inspired by Phonics & Stuff (https://phonicsandstuff.com/games). No assets or code copied.
- Word packs are original content hosted in this repository (`content/word_pack.json`)
- Sound effects are original, synthesized by `tools/generate_sounds.py` (no third-party audio)

## AI use declaration
Add your Declaration of AI-Generated Material here and submit it with the assignment.

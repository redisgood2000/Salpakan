# Salpakan — Native Android

A native Kotlin + Jetpack Compose Android game for Games of the Generals.

## Visual direction
The Android UI follows the selected Salpakan reference: deep navy backgrounds, gold borders and buttons, blue and red army colors, military-rank styling, and a compact mobile-first layout. This is the chosen design system; the previous green/charcoal concept is not used.

## Current native screens
- Home dashboard with Play Game, Online Multiplayer, Local Multiplayer, Rank Guide, How to Play, Settings, Profile and Leaderboard
- Online room list and Create Room UI
- Local pass-and-play entry screen
- 8 × 9 game-board presentation with hidden/revealed rank display
- Rank guide, rules, victory, profile, leaderboard and settings screens

## Build an APK
1. Open the **Actions** tab in this repository.
2. Select **Build Salpakan Android APK**.
3. Run the workflow, or let a push trigger it.
4. Download the **Salpakan-debug-apk** artifact from the completed run.

## Development status
This commit implements the selected visual design and screen navigation. The board is still a UI foundation: legal movement, army setup validation, rank combat, Spy/Five-Star General attack exception, Flag capture, turn validation, and game-end conditions still need implementation and testing. Online room UI is present, but Firebase authentication, realtime multiplayer, and chat are not connected yet.

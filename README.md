# 2048 Android Game

![App Icon](app/src/main/res/mipmap-xxxhdpi/ic_launcher.png)

A complete implementation of the classic 2048 puzzle game for Android with Material Design.

## Table of Contents
- [Features](#features)
- [Screenshots](#screenshots)
- [Installation](#installation)
- [Gameplay](#gameplay)
- [Architecture](#architecture)
- [Customization](#customization)
- [Building](#building)
- [License](#license)

## Features
- 🎮 Authentic 2048 gameplay mechanics  
- 👆 Intuitive swipe gesture controls  
- 📊 Real-time score and high score tracking  
- ♻️ One-tap new game restart  
- 🎨 Dynamic tile coloring system  
- 📱 Fully responsive layout  
- 🏆 Persistent high scores  

## Screenshots
| Main Menu | Gameplay | Victory Screen |
|-----------|----------|----------------|
| <img src="screenshots/main.jpg" width="200"> | <img src="screenshots/gameplay.jpg" width="200"> | <img src="screenshots/win.jpg" width="200"> |

## Installation

### Prerequisites
- Android Studio Arctic Fox or later  
- Android SDK API 21+

### Steps
1. Clone the repository:
    ```bash
    git clone https://github.com/yourusername/2048-android.git
    ```
2. Open the project in Android Studio  
3. Sync Gradle dependencies  
4. Run on emulator or physical device

## Gameplay
- **Objective**: Combine numbered tiles to reach 2048  
- **Controls**: Swipe in any direction to move tiles  
- **Scoring**: +X points when merging two X-value tiles  
- **Game Over**: When board fills with no valid moves  

## Architecture

### Components
| Class         | Responsibility                |
|---------------|-------------------------------|
| `MainActivity`| Entry point with start button |
| `GameActivity`| Main game screen and UI       |
| `GameLogic`   | Core game rules and state     |
| `TileView`    | Custom tile rendering         |

### Key Design Patterns
- **MVC** architecture  
- **Observer** pattern for UI updates  
- **Strategy** pattern for movement handling  

## Customization

### Changing Game Parameters
Edit `GameLogic.java`:
```java
// Change grid size
private int gridSize = 4;

// Adjust new tile spawn probability
private final float SPAWN_4_PROBABILITY = 0.3f;
```

### Modifying Appearance
Update colors in `res/values/colors.xml`:
```xml
<!-- Tile colors -->
<color name="tile_2">#A5CA6B</color>
<color name="tile_4">#7A9D4F</color>
```
Adjust dimensions in `res/values/dimens.xml` for spacing and tile size.

## Building

### Debug Build
```bash
./gradlew assembleDebug
```

### Release Build
```bash
./gradlew assembleRelease
```

## License
This project is licensed under the [MIT License](LICENSE).
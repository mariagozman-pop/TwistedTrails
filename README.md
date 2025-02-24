# TwistedTrails

## Overview

**Twisted Trails** is a Java-based maze puzzle game designed for friendly competition and nostalgia. Inspired by classic paper maze games, it offers a fun and engaging experience where players navigate through randomly generated mazes, collect items, and challenge friends. The game was developed primarily in **IntelliJ IDEA**, but it can also run in other IDEs like **Eclipse, BlueJ, NetBeans, and Visual Studio Code**.

## Features

- **Maze Puzzle Gameplay** – Solve procedurally generated mazes with increasing difficulty.
- **Collectible Items** – Gather special items within each maze to progress.
- **Multiplayer Social Features** – Add friends, track their progress, and compete.
- **Dynamic Soundtrack** – Enjoy immersive background music and sound effects.
- **Progress Saving** – Automatically saves user progress.
- **Randomized Mazes** – Each game restart generates a brand-new maze challenge.

## Installation & Setup

### Prerequisites

- **Java Development Kit (JDK)** (version 8 or higher)
- **IntelliJ IDEA** (recommended) or any compatible IDE
- **Git** (for version control)


### Running the Game

1. Open the project in your preferred IDE.
2. Ensure Java is installed and properly configured.
3. Compile and run the **MainMenu.java** file.

## Usage Guide

### 1. Starting the Game

- Upon launching, you'll see the **Main Menu**.
- Options:
  - **Create a New Account**
  - **Log in to an Existing Account**
  - **Exit the Game**

### 2. Creating an Account

- Click **"Create New Account"**.
- Enter a **username** and **password**.
- A confirmation message will appear once the account is created.

### 3. Logging In

- Click **"Log In"** on the main menu.
- Enter your **credentials**.
- Upon success, the game interface loads.

### 4. Playing the Game

- **Maze Navigation:** Use keyboard or on-screen controls to move.
- **Collecting Items:** Gather required items to proceed.
- **Level Completion:** Reach the maze exit to progress.
- **Friend System:** Add and track friends' progress.

### 5. Restarting the Game

- After completing all levels, restart the game for a **new randomized maze**.

### 6. Exiting the Game

- Use the **Exit** option in the main menu or in-game menu.
- Progress is automatically saved.

## Project Structure

```
TwistedTrails/
│── src/
│   ├── main/
│   │   ├── MainMenu.java  # Main entry point
│   │   ├── UIManager.java  # Handles UI components
│   │   ├── Level.java  # Manages maze generation & gameplay
│   │   ├── CredentialsManager.java  # Manages user data
│   │   ├── UserAccount.java  # Handles user profile & progress
│   │   ├── FriendManagementUI.java  # Implements social features
│   │   ├── Sound.java  # Base class for audio functionality
│   │   ├── BackgroundSound.java  # Manages background music
│   │   ├── WinningSounds.java  # Handles level completion sounds
│   ├── resources/  # Assets like sounds, UI graphics
│── README.md
│── .gitignore
│── LICENSE
```

## Object-Oriented Design Principles

### **1. Classes & Objects**

- The project is built with multiple Java classes for modularity.
- Key classes: `MainMenu`, `UIManager`, `Level`, `UserAccount`, `CredentialsManager`, etc.

### **2. Inheritance**

- `BackgroundSound` and `WinningSounds` inherit from `Sound`.
- Reuse of base functionality reduces redundancy.

### **3. Encapsulation**

- Use of private fields and public methods for controlled access.

### **4. Polymorphism**

- Overriding methods in sound-related classes allows for flexible behavior.

### **5. Abstraction**

- The `UIManager`, `Level`, and `MainMenu` classes hide complex logic behind simple interfaces.

## Future Improvements

- **Enhanced AI-based Maze Generation**
- **More Multiplayer Features** (Real-time competitive mode)
- **Customization Options** (Skins, themes, and avatars)
- **Mobile Version Compatibility**

## Author

**Gozman-Pop Maria-Eliza** 


# TCP Wireless CLI Battleship Game 🎯

This is a two-player **Command-Line Interface Battleship Game** implemented in **Java**, using **TCP sockets** to allow wireless play between a Host and a Client.

Players take turns attacking grid coordinates, with real-time feedback sent over the network. Designed to work over **local networks (LAN)**
---

## Features

- Two-player battleship gameplay over a network
- CLI interface with user-friendly prompts
- Reliable communication using Java TCP sockets
- Graceful disconnect and exit handling
- Prevents duplicate attacks
- Cross-platform compatible

---

## How to Run

### 1. Clone the Repository (hopefully you can do that)

### 2. Compile the Project
  - Once you have cloned the repository, compile (and download dependencies) the project.

### 3. Run the Game
  - Launch the game using: mvn exec:java
  - You will be prompted to choose if you are a host (h) or client (c).
  - If you are the host:
      - Choose h
      - Enter a port to listen on
      - Wait for the client to connect
  - If you are the client:
      - Choose c
      - Enter the host's IP address (localhost or IPv4 address)
      - Enter the same port the host used

### 4. Play the Game
  - At any time during your turn, you can type: exit or EXIT.
    - This will close your socket, notify the opponent, and shut down cleanly.
   
### 5. Enjoy!

Thanks!!!

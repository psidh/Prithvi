# Prithvi: A Key-Value Database

Prithvi is an in-memory key-value database built from scratch in Java, without relying on external frameworks. It provides basic data storage operations, persistence to disk, and essential features like TTL (Time-To-Live) expiry and automatic data management.

---

## Table of Contents

- [Prithvi: A Key-Value Database](#prithvi-a-key-value-database)
  - [Table of Contents](#table-of-contents)
  - [Features](#features)
  - [Getting Started](#getting-started)
    - [Prerequisites](#prerequisites)
    - [Running the Server](#running-the-server)
  - [Commands](#commands)
  - [Persistence](#persistence)
  - [Architecture \& Design Notes](#architecture--design-notes)
  - [Security Warning](#security-warning)
  - [License](#license)
  - [Author](#author)

---

## Features

Prithvi offers a core set of functionalities for a lightweight key-value database:

- **In-Memory Key-Value Storage**: Pure Java implementation for efficient in-memory data handling.
- **Data Types**: Supports string values for keys, and also includes basic list (deque) operations.
- **Time-To-Live (TTL) Expiry**: Keys can be set with an expiration time, after which they are automatically removed.
- **LRU Cache**: Maintains a `MAX_CAPACITY` of 10,000 keys and uses `LinkedHashMap` to evict elderly keys after threshold is reached. Load Factor is 0.75f.
- **Persistence to Disk**: Stores data to a JSON-based file (`data/store.json`) allowing data to survive server restarts.
- **Auto-Save & Auto-Load**: Configurable periodic saving of the store to disk and automatic loading on server startup.
- **Automatic Expired Key Removal**: A background task periodically cleans up expired keys.
- **Multi-threaded Client Handling**: Each client connection is handled in a separate thread for concurrent access.
- **Graceful Shutdown**: Implements a shutdown hook to ensure data is saved to disk when the server is terminated (e.g., via Ctrl+C).

---

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

- Java Development Kit (JDK) 11 or newer.

### Running the Server

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/psidh/Prithvi
    cd Prithvi
    ```
2.  **Compile and run the server:**
    Run the command
    ```bash
    chmod +x run.sh
    ./run.sh
    ```
    The server will start and listen on port `1902`. You will see startup information and the Prithvi ASCII art logo in your console.

---

## Commands

Once the server is running, you can connect to it using a client (e.g., `nc` or a simple TCP client program) on port `1902` and issue the following commands:

| Command Syntax                   | Description                                                           |
| :------------------------------- | :-------------------------------------------------------------------- |
| `SET <key> <value>`              | Stores a string `value` associated with `key`.                        |
| `SET <key> <value> EX <seconds>` | Stores `value` with `key` that expires after `<seconds>`.             |
| `GET <key>`                      | Retrieves the `value` and its expiry information for `key`.           |
| `DEL <key>`                      | Deletes the specified `key` from the store.                           |
| `EXISTS <key>`                   | Checks if `key` exists in the store.                                  |
| `SADD <key> <value>`             | Adds `value` to a Set identified by `key`. Creates new Set if needed. |
| `SMEMBERS <key>`                 | Lists all members of the Set stored at `key`.                         |
| `SREM <key> <value>`             | Removes `value` from the Set identified by `key`.                     |
| `LPUSH <key> <value>`            | Pushes `value` onto the left end of a list identified by `key`.       |
| `RPUSH <key> <value>`            | Pushes `value` onto the right end of a list identified by `key`.      |
| `LPOP <key>`                     | Removes and returns the element from the left end of a list.          |
| `RPOP <key>`                     | Removes and returns the element from the right end of a list.         |
| `GETLIST <key>`                  | Displays all elements in the list associated with `key`.              |
| `KEYS`                           | Lists all keys currently in the store with their expiry.              |
| `FLUSH`                          | Clears all keys from the store, requires confirmation.                |
| `FLUSH FALL`                     | Clears all keys from the store without confirmation.                  |
| `SAVE`                           | Explicitly persists the current state of the store to disk.           |
| `LOAD`                           | Loads the store state from disk, overwriting current in-memory data.  |
| `QUIT`                           | Gracefully closes the client connection.                              |
| `HELP`                           | Displays a quick reference of all available commands.                 |

---

## Persistence

Prithvi's persistence mechanism saves the in-memory state to `data/store.json`.

- **AutoSave**: By default, the server performs a periodic auto-save (e.g., every 300 seconds as configured in `Prithvi.java`).
- **AutoLoad**: On startup, Prithvi attempts to load the last saved state from `data/store.json`.
- **Manual Save/Load**: You can manually trigger saves with the `SAVE` command and loads with the `LOAD` command.
- **Shutdown Hook**: Ensures data is saved upon a graceful shutdown (e.g., Ctrl+C).

---

## Architecture & Design Notes

- **No Frameworks**: The project is intentionally built using pure Java APIs to demonstrate fundamental concepts of network programming, concurrency, and data structures.
- **`Map`**: Used as the primary data store to ensure thread-safe operations across multiple client connections.
- **Background Tasks**: Two Dedicated threads manage key expiry and auto-saving, keeping the main server loop responsive.

---

## Security Warning

**⚠️ Warning**: This is an experimental build and is not intended for production use. It lacks authentication, encryption, and robust error handling typical of production-grade systems. Use with caution.

---

## License

This project is licensed under the [MIT License](LICENSE)

---

## Author

- **Philkhana Sidharth**

---

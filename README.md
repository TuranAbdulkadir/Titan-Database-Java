# 💾 TITAN DB (Custom Key-Value Database)

**Titan DB** is a lightweight, high-performance in-memory key-value store built in **Java**, inspired by **Redis**.
It demonstrates core database concepts including **In-Memory Caching**, **Disk Persistence (AOF)**, and **Network Socket Communication**.

## 🚀 Features
* **In-Memory Speed:** Stores data in RAM for microsecond-latency read/write operations using `HashMap`.
* **Disk Persistence:** Automatically saves data to a local file to prevent data loss on shutdown.
* **Custom Query Language:** Supports commands like `SET key value`, `GET key`, and `DEL key`.
* **Networked Architecture:** Listens on a TCP port to accept connections from other applications (like C++ Web Servers).

## 🛠️ Tech Stack
* **Language:** Java (JDK 21)
* **Networking:** `java.net.ServerSocket`
* **I/O:** `java.io` (File Handling)

## ⚙️ How It Works
1. Starts a TCP Server on port `6379` (Standard Redis port).
2. Loads existing data from disk into memory.
3. Accepts string commands from clients.
4. Processes commands and updates both RAM and Disk logs instantly.

---
*Developed by [Adin Soyadin] as a Backend Development Portfolio Project.*
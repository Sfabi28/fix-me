# Fixme ⚡

Fixme is a Java networking project focused on building a multi-threaded trading simulation over TCP. It is designed to explore non-blocking sockets, asynchronous message handling, and a modular Maven architecture.

## Overview 🌍

The project is composed of three independent components that communicate over the network:

- **Market** 🏛️: simulates the exchange side of the system.
- **Broker** 💼: represents the client-side trading participant.
- **Router** 🔀: forwards and coordinates FIX messages between components.

The goal is to exchange well-formed FIX messages reliably while keeping the implementation clean, scalable, and easy to maintain.

## Key Requirements ✅

- Use non-blocking sockets.
- Use the Java Executor Framework for message handling.
- Organize the code as a multi-module Maven project.
- Follow the Java package naming conventions.
- Avoid the default package.

## Project Goals 🎯

This project is meant to practice:

- TCP networking and asynchronous communication.
- Concurrent programming with threads and executors.
- Message routing and validation.
- Clean architecture using modular Java code.

## Suggested Structure 🧩

The final implementation will typically include separate modules for:

- shared domain and FIX message models,
- router logic,
- broker logic,
- market logic.

## Build and Run 🛠️

If the project is organized as a Maven multi-module build, the usual workflow is:

```bash
mvn clean install
```

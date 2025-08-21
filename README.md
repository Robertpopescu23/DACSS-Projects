# Broker Pattern Project – ToyORB

## Overview

This project implements a **Toy Object Request Broker (ToyORB)** to demonstrate the **Broker pattern**.  
The goal is to understand the role of middleware in **distributed object-oriented applications** by allowing applications to invoke methods on remote objects.

---

## Problem Description

**ToyORB** is a minimalist implementation of the Broker pattern supporting **distributed object-oriented applications**.  

**Simplifications for this assignment:**
- Remote objects’ operations only have parameters and return types of `int`, `float`, and `String`.  
- Concurrency handling for remote objects is **out of scope**.  

**Example Servers to demonstrate ToyORB:**

### InfoServer
- `get_road_info(int road_ID)` → returns road information  
- `get_temp(String city)` → returns temperature for a city  

### MathServer
- `do_add(float a, float b)` → computes sum  
- `do_sqr(float a)` → computes square root  

> You can use the example code as-is or modify it for testing.

---

## Project Structure

ToyORB/
├── ByteSendReceive/ # Handles low-level byte sending/receiving
├── Client/ # Client application code
├── Configuration/ # Configuration files and settings
├── MessageMarshaller/ # Serialization/deserialization of messages
├── Registry/ # Maintains remote object registry
├── RequestReply/ # Request-reply protocol implementation
└── Server/ # Server application code (InfoServer, MathServer)


---

## Features

- Implements the **Broker pattern** for method invocation on remote objects.  
- Middleware components handle communication between clients and servers.  
- Supports demonstration servers (InfoServer, MathServer) to validate the implementation.  
- Simplified type system: only `int`, `float`, `String`.  

---

## Usage

1. Start the ToyORB servers (InfoServer or MathServer).  
2. Run the client code to invoke methods on the remote objects via ToyORB.  
3. Observe correct responses to remote method calls.

---

## Notes

- Focus of the assignment is **learning middleware design**, not concurrency or complex object types.  
- All APIs are minimal and simplified to illustrate the Broker pattern.

---

This README should be placed in the **root of the branch** dedicated to the Broker Pattern project (e.g., `Broker-Pattern` branch) to keep it separate from other assignments.  


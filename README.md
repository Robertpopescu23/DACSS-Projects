# DACSS Projects Repository

This repository contains multiple assignments and projects for the **DACSS course**, organized by branch. Each branch contains a separate project with its own detailed README and source code.

---

## Branches and Projects

### 1. `Fundamentals-Architectural-Styles`
Implements core architectural styles:

- **Pipes-and-Filters:** Linear pipeline of filters to process messages, including concurrent processing.  
- **Blackboard:** Shared memory architecture where knowledge sources process messages independently.  
- **Event-Driven:** Two applications using a custom BasicEventBus:
  - Sensor monitoring system
  - News agencies and subscriber system

### 2. `The-Reflection-Pattern`
Implements a **Java Reflection-based reverse engineering tool**:

- Extracts classes, interfaces, methods, fields, and relationships from compiled `.jar` files.
- Generates textual representations of class diagrams.
- Optional feature: produces UML-compatible outputs for tools like yUML or PlantUML.

### 3. `Broker-Pattern`
Implements a **Toy Object Request Broker (ToyORB)**:

- Middleware pattern allowing remote method invocation.
- Example applications:
  - **InfoServer:** Provides road info and temperature data.
  - **MathServer:** Provides arithmetic operations.
- Focus: understanding the broker pattern for distributed object-oriented applications.

### 4. `Data-Access-Pattern`
Contains **two projects**:

- **HoroscopeApp:** Uses the DAO pattern to read students from XML or SQL and predict daily horoscope.  
- **Object Relational Mapper (ORM):** Generates database table structures from class models and supports optional object persistence via EntityManager.

---

## General Notes

- Each project lives in its own branch and has a dedicated README.md with detailed instructions.  
- The `main` branch contains only this high-level overview and serves as the starting point for navigating the repository.  
- Branches are isolated to avoid mixing code from different assignments.

---

## Repository Structure (branch-independent overview)

DACSS-Projects/

- Fundamentals-Architectural-Styles/    # Project subfolders for different architectural styles
- Reflection-Pattern/                    # Java reflection tool
- Broker-Pattern/                        # ToyORB project
- Assignment5/                           # HoroscopeApp & ORM
- README.md                              # This overview

---

This main README provides a quick summary of the repository and guides users to the appropriate branch for each project.

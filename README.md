# Reflection Pattern Project

## Overview

This branch contains the **Reflection Pattern** project for the DACSS course.  
The goal of this assignment was to implement a **Java reverse engineering tool** using **reflection** (`java.lang.reflect`) to extract design information from compiled `.jar` files.

The tool generates a textual representation of **class diagrams**, including:

- Classes and interfaces  
- Methods and constructors  
- Fields and their types  
- Relationships between classes/interfaces (`extends`, `implements`, `association`, `dependency`)  

---

## Features

- Accepts a compiled `.jar` file as a command-line argument.
- Extracts all information needed to describe the class diagram.
- Supports optional configuration:
  - Fully qualified class names (yes/no)
  - Show/hide methods
  - Show/hide attributes
  - Ignore specific classes (e.g., `java.lang.*`)
- Handles generic type parameters in classes and methods.
- Optional UML output compatible with **yUML** or **PlantUML**.

---

## How It Works

1. Open the input JAR file using `java.util.jar.JarFile`.  
2. Load classes dynamically with `URLClassLoader`.  
3. Use reflection to gather metadata:
   - Superclasses and interfaces
   - Fields and their modifiers
   - Constructors and parameters
   - Methods and parameters
4. Output the extracted information in a readable format suitable for class diagrams.

---

## Usage

```bash
java MyReverseEngineeringTool <compiled-file.jar>

Input: Animal.class inside a JAR

Output:
Class: com.example.Animal
  extends: java.lang.Object
  implements: com.example.LivingBeing
  Fields: private String name
  Constructors: public Animal()
  Methods: public void speak(), public void eat(String food)

## References

- [Java Reflection API](https://docs.oracle.com/javase/8/docs/api/java/lang/reflect/package-summary.html)  
- [JarFile documentation](https://docs.oracle.com/javase/8/docs/api/java/util/jar/JarFile.html)  
- [yUML](https://yuml.me/) | [PlantUML](https://plantuml.com/)

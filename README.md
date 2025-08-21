# Projects – HoroscopeApp & Object Relational Mapper

This branch contains two separate projects for DACSS Assignment 5:

1. **HoroscopeApp** – a student horoscope application  
2. **Object Relational Mapper (ORM)** – a tool to map class structures to database tables

---

## 1. HoroscopeApp

**Overview:**  
A horoscope application that predicts whether a student will have a good day based on a simple algorithm.

**Requirements:**
- Read students from different data sources:  
  - XML files  
  - SQL databases  
- Use the **Data Access Object (DAO) pattern** to decouple business logic from the data source.  
- The data source choice (XML or SQL) is selected **at runtime**.  
- Business logic algorithm:  
  - Count the number of vowels in the student’s name.  
  - If the number of vowels divides the current day of the month, the student is predicted to have a good day.

**Purpose:**  
Understand the DAO pattern and design a system where the business logic is independent of data source implementation.

---

## 2. Object Relational Mapper (ORM)

**Overview:**  
A tool that generates database table structures from a set of classes and their relationships.

**Standard Requirements:**
- Input: A set of classes related by inheritance and associations.  
- Output: Generated database tables and relationships using standard O-R mapping patterns.  
- Implements **class-to-table mapping** for:
  - Inheritance relationships  
  - Association relationships  

**Optional Features (Bonus):**
- **EntityManager:** Save object instances into the generated database tables.  
- Provides full ORM functionality by linking object instances to database records.  

**Purpose:**  
Understand object-relational mapping and automate the creation of database schemas from class models.

---

## Project Structure

---

## Usage

1. **HoroscopeApp:**
   - Configure the data source (XML or SQL) at runtime.  
   - Run the application to predict daily horoscope for students.

2. **ORM:**
   - Provide a set of classes as input.  
   - Run the generator to automatically create database tables and relationships.  
   - Optional: Use EntityManager to save object instances into the database.

---

## Notes

- Both projects are implemented **separately in their respective directories**.  



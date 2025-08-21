package org.example;

import org.example.generator.ORMSchemaGenerator;
import org.example.model.*;
import org.example.EntityManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            // Step 1: Generate SQL for tables from annotated classes
            ORMSchemaGenerator generator = new ORMSchemaGenerator();
            generator.generateSQL(List.of(Person.class, Employee.class, Department.class));

            // Step 2: Connect to database
            String url = "jdbc:mysql://localhost:3306/schooldb?useSSL=false&serverTimezone=UTC";
            String user = "root";
            String password = "";

            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connected Succesfully");

            // Step 3: Use EntityManager to save objects
            EntityManager em = new EntityManager(conn);

            Department department = new Department(1, "Engineering");
            em.persist(department);

            Employee employee = new Employee(1, "Alice", 28, department);
            em.persist(employee);

            Person person = new Person(2, "Bob", 35);
            em.persist(person);

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

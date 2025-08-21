package org.example.model;

import org.example.annotations.*;

import static java.lang.Character.getName;


@Entity(name = "employee")
public class Employee extends Person
{
    @Column
    private double salary;
    @ManyToOne
    private Department department; //association

    public Employee() {}

    public Employee(int id, String name, int age, Department department) {
        super(id, name, age);
        this.department = department;
    }

    public double getSalary() { return salary; }

    public Department getDepartment() { return department; }

    public void setDepartment() { this.department = department; }

    @Override
    public String toString() {
        return "Employee{id=" + getId() + ", name=" + getName() + ", department=" + department + "}";
    }
}
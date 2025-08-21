package org.example.model;

import org.example.annotations.*;

import java.util.List;

@Entity
public class Department {
    @Id
    private int id;

    @Column
    private String name;

    @OneToMany(targetEntity = Employee.class)
    private List<Employee> employees; // aggregation

    public Department() {}

    public Department(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() { return id; }

    public String getName() { return name; }

    public List<Employee> getEmployees() { return employees; }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Department{id=" + id + ", name='" + name + "'}";
    }
}

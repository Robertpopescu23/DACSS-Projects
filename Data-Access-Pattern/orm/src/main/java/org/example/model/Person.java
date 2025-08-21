    package org.example.model;

    import org.example.annotations.*;

    @Entity
    @Inheritance(strategy = "JOINED")
    public class Person
    {
        @Id
        private int id;

        @Column
        private String name;

        @Column
        private int age;

        public Person() {}
        public Person(int id, String name, int age)
        {
            this.id = id;
            this.name = name;
            this.age = age;
        }

        public int getId() { return id; }

        public String getName() { return name; }

        public int getAge() { return age; }

        @Override
        public String toString() { return "Person{id=" + id + ", name=" + name + ", age=" + age + "}"; }
    }
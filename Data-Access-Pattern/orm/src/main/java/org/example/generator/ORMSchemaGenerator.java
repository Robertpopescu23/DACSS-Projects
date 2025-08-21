package org.example.generator;

import org.example.annotations.*;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

public class ORMSchemaGenerator {

    public void generateSQL(List<Class<?>> classes) {
        for (Class<?> clazz : classes) {
            if (clazz.isAnnotationPresent(Entity.class)) {
                Inheritance inheritance = clazz.getAnnotation(Inheritance.class);
                String strategy = inheritance != null ? inheritance.strategy() : "SINGLE_TABLE";

                switch (strategy.toUpperCase()) {
                    case "JOINED":
                        generateJoinedTables(clazz, classes);
                        break;
                    case "TABLE_PER_CLASS":
                        generateTablePerClass(clazz);
                        break;
                    default:
                        generateSingleTable(clazz);
                }
            }
        }
    }

    private void generateSingleTable(Class<?> clazz) {
        StringBuilder sql = new StringBuilder("CREATE TABLE " + clazz.getSimpleName().toLowerCase() + " (\n");

        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Id.class)) {
                sql.append(field.getName()).append(" INT PRIMARY KEY,\n");
            } else if (field.isAnnotationPresent(Column.class)) {
                sql.append(field.getName()).append(" VARCHAR(255),\n");
            }
        }

        sql.deleteCharAt(sql.lastIndexOf(","));
        sql.append("\n);\n");

        System.out.println(sql);
    }

    private void generateJoinedTables(Class<?> baseClass, List<Class<?>> allClasses) {
        String baseTable = baseClass.getSimpleName().toLowerCase();
        StringBuilder baseSQL = new StringBuilder("CREATE TABLE " + baseTable + " (\n");

        for (Field field : baseClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(Id.class)) {
                baseSQL.append(field.getName()).append(" INT PRIMARY KEY,\n");
            } else if (field.isAnnotationPresent(Column.class)) {
                baseSQL.append(field.getName()).append(" VARCHAR(255),\n");
            }
        }

        baseSQL.deleteCharAt(baseSQL.lastIndexOf(","));
        baseSQL.append("\n);\n");
        System.out.println(baseSQL);

        for (Class<?> subclass : allClasses) {
            if (baseClass.isAssignableFrom(subclass) && !subclass.equals(baseClass)) {
                String subTable = subclass.getSimpleName().toLowerCase();
                StringBuilder subSQL = new StringBuilder("CREATE TABLE " + subTable + " (\n");
                subSQL.append("id INT PRIMARY KEY,\n");
                subSQL.append("FOREIGN KEY (id) REFERENCES ").append(baseTable).append("(id),\n");

                for (Field field : subclass.getDeclaredFields()) {
                    if (!Modifier.isStatic(field.getModifiers())
                            && field.isAnnotationPresent(Column.class)
                            && !field.isAnnotationPresent(Id.class)) {
                        subSQL.append(field.getName()).append(" VARCHAR(255),\n");
                    }
                }

                subSQL.deleteCharAt(subSQL.lastIndexOf(","));
                subSQL.append("\n);\n");
                System.out.println(subSQL);
            }
        }
    }

    private void generateTablePerClass(Class<?> clazz) {
        String tableName = clazz.getSimpleName().toLowerCase();
        StringBuilder sql = new StringBuilder("CREATE TABLE " + tableName + " (\n");

        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Id.class)) {
                sql.append(field.getName()).append(" INT PRIMARY KEY,\n");
            } else if (field.isAnnotationPresent(Column.class)) {
                sql.append(field.getName()).append(" VARCHAR(255),\n");
            }
        }

        sql.deleteCharAt(sql.lastIndexOf(","));
        sql.append("\n);\n");

        System.out.println(sql);
    }
}

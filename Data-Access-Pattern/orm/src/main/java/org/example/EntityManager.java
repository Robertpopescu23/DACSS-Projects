package org.example;

import org.example.annotations.*;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;
import java.util.StringJoiner;

public class EntityManager {
    private final Connection connection;

    public EntityManager(Connection connection)
    {
        this.connection = connection;
    }

    public void persist(Object entity) throws SQLException, IllegalAccessException {
        Class<?> clazz = entity.getClass();

        if (!clazz.isAnnotationPresent(Entity.class)) {
            throw new RuntimeException("Class is not annotated with @Entity: " + clazz.getName());
        }

        String tableName = clazz.getSimpleName().toLowerCase(); // You can enhance this to use Entity.name()

        Field[] fields = clazz.getDeclaredFields();
        StringJoiner columns = new StringJoiner(", ");
        StringJoiner placeholders = new StringJoiner(", ");

        for (Field field : fields) {
            if (field.isAnnotationPresent(Column.class) || field.isAnnotationPresent(Id.class)) {
                field.setAccessible(true);
                columns.add(field.getName());
                placeholders.add("?");
            }
        }

        String sql = "INSERT INTO " + tableName + " (" + columns + ") VALUES (" + placeholders + ")";
        PreparedStatement stmt = connection.prepareStatement(sql);

        int index = 1;
        for (Field field : fields) {
            if (field.isAnnotationPresent(Column.class) || field.isAnnotationPresent(Id.class)) {
                field.setAccessible(true);
                Object value = field.get(entity);
                stmt.setObject(index++, value);
            }
        }
        stmt.executeUpdate();
        stmt.close();
    }
}
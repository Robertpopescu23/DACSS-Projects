    package DAO;

    import java.sql.*;
    import java.util.*;
    import model.Student;

    public class SQLStudentDAO implements StudentDAO
    {
        private final Connection connection;

        public SQLStudentDAO() throws SQLException
        {
            try
            {
                Class.forName("com.mysql.cj.jdbc.Driver");
            }catch (ClassNotFoundException e)
            {
                throw new RuntimeException("MySQL JDBC Driver not found", e);
            }

            String dbName = "db/schooldb";
            String connectionURL = "jdbc:mysql://localhost:3306/schooldb?useSSL=false&serverTimezone=Europe/Bucharest";
            connection = DriverManager.getConnection(connectionURL, "root", "");

            if(!tableExists("STUDENTS"))
            {
                createStudentsTable();
                insertSampleData();
            }
        }

        private boolean tableExists(String tableName) throws SQLException
        {
            DatabaseMetaData meta = connection.getMetaData();
            ResultSet result = meta.getTables(null, null, tableName.toUpperCase(), null);
            return result.next(); //returns true if table exists
        }

        private void createStudentsTable() throws SQLException
        {
            String sql = """
                    CREATE TABLE STUDENTS(
                    Stud_ID INT PRIMARY KEY,
                    NAME VARCHAR(32) NOT NULL,
                    SURNAME VARCHAR(32) NOT NULL
                    )
                    """;

            Statement stm = connection.createStatement();
            stm.executeUpdate(sql);
            System.out.println("Table STUDENTS created.");
        }

    private void insertSampleData() throws SQLException
    {
        String sql = """
                INSERT INTO STUDENTS (Stud_ID, NAME, SURNAME) VALUES
                (1, 'Mario', 'Smith'),
                (2, 'Bob', 'Johson'),
                (3, 'Cleopatra', 'Stratan'),   
                (4, 'Goldis', 'Daniel')
                """;

        Statement stm = connection.createStatement();
        stm.executeUpdate(sql);
        System.out.println("Sample student data inserted");
    }

        @Override
        public List<Student> getAllStudents()
        {
            List<Student> students = new ArrayList<>();

            try
            {
                Statement stm = connection.createStatement();
                ResultSet resultSet = stm.executeQuery("SELECT Stud_ID, NAME, SURNAME FROM STUDENTS");
                while(resultSet.next())
                {
                    int id = resultSet.getInt("Stud_ID");
                    String name = resultSet.getString("NAME");
                    String surname = resultSet.getString("SURNAME");
                    students.add(new Student(id, name, surname));
                }
            }catch (SQLException e)
            {
                e.printStackTrace();
            }

            return students;
        }
    }
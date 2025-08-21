package DAO;

import java.sql.SQLException;

public class SQLDAOFactory extends DAOFactory
{
    @Override
    public StudentDAO getStudentDAO()
    {
        try
        {
            return new SQLStudentDAO();
        }catch (SQLException e)
        {
            e.printStackTrace();
            throw new RuntimeException("Failed to create SQLStudentDAO", e);
        }
    }
}
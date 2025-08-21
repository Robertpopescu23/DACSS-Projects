package DAO;

public class XMLDAOFactory extends DAOFactory
{

    private static final String xmlFilePath = "DAO/data/students.xml";
    @Override
    public StudentDAO getStudentDAO()
    {
        return new XMLStudentDAO(xmlFilePath);
    }
}
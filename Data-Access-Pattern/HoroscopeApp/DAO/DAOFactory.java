package DAO;

public abstract class DAOFactory
{
    public static final int XML = 1;
    public static final int SQL = 2;

    public abstract StudentDAO getStudentDAO();

    public static DAOFactory getDAOFactory(int factoryType)
    {
        return switch (factoryType) {
            case XML -> new XMLDAOFactory();
            case SQL -> new SQLDAOFactory();
            default -> null;
        };
    }
}
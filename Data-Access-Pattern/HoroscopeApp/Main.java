import DAO.*;
import model.Student;
import service.*;

import java.util.*;

public class Main
{
    public static void main(String[] args)
    {
        StudentDAO xmlStudentDAO = DAOFactory.getDAOFactory(DAOFactory.XML).getStudentDAO();
        StudentDAO sqlStudentDAO = DAOFactory.getDAOFactory(DAOFactory.SQL).getStudentDAO();

        HoroscopeService horoscopeService = new HoroscopeService();

        List<Student> allStudents = new ArrayList<>();
        allStudents.addAll(xmlStudentDAO.getAllStudents());
        allStudents.addAll(sqlStudentDAO.getAllStudents());

        System.out.println("Horoscope Predictions: ");
        for(Student s : allStudents)
        {
            boolean goodDay = horoscopeService.willHaveAGoodDay(s);
            System.out.println(s.getName() + " will " + (goodDay ? "have" : "not have") + " a good day.");
        }
    }
}
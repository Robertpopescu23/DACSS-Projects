package service;

import java.time.LocalDate;
import model.Student;

public class HoroscopeService
{
    public boolean willHaveAGoodDay(Student student)
    {
        String name = student.getName().toLowerCase();
        int vowelCount = 0;
        for (char c : name.toCharArray())
        {
            if("aeiou".indexOf(c) >= 0) vowelCount++;
        }
        int day = LocalDate.now().getDayOfMonth();

        return vowelCount > 0 && day % vowelCount == 0;
    }
}
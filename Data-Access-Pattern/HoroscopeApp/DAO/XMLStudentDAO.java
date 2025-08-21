package DAO;

import java.io.File;
import java.util.*;
import model.Student;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class XMLStudentDAO extends DefaultHandler implements StudentDAO
{
    private final List<Student> students = new ArrayList<>();

    public XMLStudentDAO(String filePath)
    {
        try
        {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            saxParser.parse(new File(filePath), this);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    public List<Student> getAllStudents()
    {
        return students;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attrs)
    {
        if(qName.equalsIgnoreCase("student"))
        {
            int id = Integer.parseInt(attrs.getValue("id"));
            String name = attrs.getValue("name");
            String surname = attrs.getValue("surname");
            students.add(new Student(id, name, surname));
        }
    }
}
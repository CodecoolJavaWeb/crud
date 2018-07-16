package com.codecool.krk;

import org.omg.CORBA.PUBLIC_MEMBER;

import java.util.ArrayList;
import java.util.List;

public class StudentsDataBase {

    private List<Student> studentsList;

    public StudentsDataBase() {
        studentsList = new ArrayList<>();
    }

    public List<Student> getStudentsList() {
        return studentsList;
    }

    public Student getStudentById(String id) {
        for (Student student : studentsList) {
            if (student.getId().equals(id)) {
                return student;
            }
        }
        return null;
    }

    public void deleteStudentById(String id) {
        System.out.println("przed" + studentsList);
        studentsList.remove(getStudentById(id));
        System.out.println("po" + studentsList);
    }

}

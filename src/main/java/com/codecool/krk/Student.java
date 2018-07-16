package com.codecool.krk;

public class Student {

    private String id;
    private static Integer counter =0;
    private String firstName;
    private String lastName;
    private String age;

//    public Student() {
//        this.id = (counter++).toString();
//        this.firstName = firstName;
//        this.lastName = lastName;
//        this.age = age;
//    }

    public Student(String firstName, String lastName, String age) {
        this.id = (counter++).toString();
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }

    public Student(String id, String firstName, String lastName, String age) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}

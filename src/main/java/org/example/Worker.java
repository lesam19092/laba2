package org.example;


import javax.persistence.*;

@Entity
public class Worker  {

    private int age;
    private String fullName;
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }


    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }


    private String profession;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id ;


    public Worker() {
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }
}

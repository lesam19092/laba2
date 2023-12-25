package org.example;

import org.apache.log4j.PropertyConfigurator;
import org.example.interfaces.HotelInterface;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Main {
    public static void main(String[] args) {


        EntityManagerFactory emf = Persistence.createEntityManagerFactory("admin");
        EntityManager em = emf.createEntityManager();

        System.out.println("Start a hibernate test");

        em.getTransaction().begin();


        em.getTransaction().commit();

        String log4jConfPath = "C:\\Users\\danil\\IdeaProjects\\laba2\\src\\main\\java\\log4j.properties";
        PropertyConfigurator.configure(log4jConfPath);

        new HotelInterface().show();
    }
}




package org.example.Entities;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
public class HotelRoom {


    private int number;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    private int price;
    private int capacity;
    private Boolean occupied;

    private int days;

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public void setOccupied(Boolean occupied) {
        this.occupied = occupied;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }


    public Boolean getOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public List<Guest> getGuestbl() {
        return guestbl;
    }

    public void setGuestbl(List<Guest> guestbl) {
        this.guestbl = guestbl;
    }

    @OneToMany(mappedBy = "hotelnomer")
    private List<Guest> guestbl = new ArrayList<>();

}

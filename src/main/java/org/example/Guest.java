package org.example;

import javax.persistence.*;

@Entity
public class Guest {

    private String fullName;
    private int age;

    private int duration;

    private int numberOfRoom;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int guestId;






    public String getFullName() {
        return fullName;
    }


    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }


    public void setFullName(String fullName) {
        this.fullName = fullName;
    }


    public Guest() {
    }

   public HotelRoom getHotelnomer() {
        return hotelnomer;
    }

    public void setHotelnomer(HotelRoom hotelnomer) {
        this.hotelnomer = hotelnomer;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    public HotelRoom hotelnomer;



    public int getGuestId() {
        return guestId;
    }

    public void setGuestId(int guestId) {
        this.guestId = guestId;
    }

    public int getNumberOfRoom() {
        return numberOfRoom;
    }

    public void setNumberOfRoom(int numberOfRoom) {
        this.numberOfRoom = numberOfRoom;
    }


    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }


}

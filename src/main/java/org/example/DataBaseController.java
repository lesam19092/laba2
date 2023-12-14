package org.example;

import org.apache.log4j.Logger;
import org.example.interfaces.GraficInterface;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.util.List;

public class DataBaseController {
    private static final Logger log = Logger.getLogger(GraficInterface.class);

    public static boolean saveGuestToDB(Guest guest) {

        boolean flag = false;
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("admin");
        EntityManager em = emf.createEntityManager();
        log.info("Сохранение пользователя в бд");
        em.getTransaction().begin();

        int a = guest.getNumberOfRoom();
        TypedQuery<HotelRoom> query = em.createQuery("SELECT h FROM HotelRoom h WHERE h.number = :roomNumber", HotelRoom.class);
        query.setParameter("roomNumber", a);
        List<HotelRoom> roomList = query.getResultList();
        if (roomList.size() == 1) {
            HotelRoom hotelRoom = roomList.get(0);
            if (hotelRoom.getGuestbl().size() < hotelRoom.getCapacity()) {
                System.out.println("был взяат с бд номер с id " + hotelRoom.getId());
                hotelRoom.setOccupied(true);
                guest.setHotelnomer(hotelRoom);
                em.persist(guest);
                em.getTransaction().commit();

                System.out.println(hotelRoom.getGuestbl().size());
                log.info("Пользователь был сохранен в бд");
                return true;
            }
            return false;
        }
      /*  else{
            HotelRoom hotelRoom1 = new HotelRoom();
            hotelRoom1.setNumber(guest.getNumberOfRoom());
            hotelRoom1.setTypeOfRoom(guest.getTypeOfRoom());
            System.out.println("был добавлен в бд");
            guest.setHotelnomer(hotelRoom1);
            em.persist(hotelRoom1);

        }*/
        //  em.persist(guest.getHotelnomer());
        em.getTransaction().commit();
        log.info("Пользователь не был сохранен в бд");
        return false;
    }

    public static void deleteById(int id) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("admin");
        EntityManager em = emf.createEntityManager();
        log.info("было начато удаление пользователя в бд");
        em.getTransaction().begin();
        Guest guest = em.find(Guest.class, id);
        if (guest != null) {
            String name = guest.getFullName();
            int a = guest.getNumberOfRoom();
            TypedQuery<HotelRoom> query = em.createQuery("SELECT h FROM HotelRoom h WHERE h.number = :roomNumber", HotelRoom.class);
            query.setParameter("roomNumber", a);

            List<HotelRoom> roomList = query.getResultList();
            HotelRoom hotelRoom = roomList.get(0);

            if (hotelRoom.getGuestbl().size() == 1) {
                log.info(String.format("гость , который проживал в комнате %d , освободил ее", hotelRoom.getNumber()));
                hotelRoom.setOccupied(false);
            } else {
                log.info(String.format("гость , который проживал в комнате %d , покинул ее", hotelRoom.getNumber()));

                hotelRoom.getGuestbl().remove(guest);
            }


            em.remove(guest);
            log.info(String.format("был удален гость с id : %d , c именем:%s", id, name));
        } else log.info("гость не был найден");

        em.getTransaction().commit();

    }

    public static boolean redactById(int id, String name, int age, int room) {


        boolean flag = false;

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("admin");
        EntityManager em = emf.createEntityManager();
        log.info("редактирование пользователя в бд");
        em.getTransaction().begin();

        Guest guest = em.find(Guest.class, id);
        if (guest != null) {

            guest.setFullName(name);
            guest.setAge(age);
            if (room != guest.getNumberOfRoom()) {
                log.info("комнаты не совпадают");
                int a = room;
                TypedQuery<HotelRoom> query = em.createQuery("SELECT h FROM HotelRoom h WHERE h.number = :roomNumber", HotelRoom.class);
                query.setParameter("roomNumber", a);
                log.info("была получена друга комната комната");
                List<HotelRoom> roomList = query.getResultList();
                if (roomList.size() == 1) {
                    HotelRoom hotelRoom = roomList.get(0);
                    if (hotelRoom.getGuestbl().size() < hotelRoom.getCapacity()) {
                        log.info("был добавлен в другую комнату");
                        hotelRoom.setOccupied(true);
                        if (guest.getHotelnomer().getGuestbl().size() == 1) {
                            guest.getHotelnomer().setOccupied(false);
                            guest.setNumberOfRoom(room);
                            guest.setHotelnomer(hotelRoom);
                            flag = true;
                        } else {
                            guest.setNumberOfRoom(room);
                            guest.setHotelnomer(hotelRoom);
                            flag = true;
                        }
                    }
                    flag = true;
                }


            }


            //  guest.setNumberOfRoom(room);
            log.info(String.format("был редактирован гость с id : %d ", id));
            flag = true;
        } else {
            log.info("гость не был найден");
        }

        em.getTransaction().commit();
        return flag;
    }

    public static Guest getGuest(int id) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("admin");
        EntityManager em = emf.createEntityManager();
        log.info("было начато удаление пользователя в бд");
        em.getTransaction().begin();
        Guest guest = em.find(Guest.class, id);
        em.getTransaction().commit();
        return guest;
    }

    public static List<HotelRoom> getAllHotelRooms() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("admin");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        TypedQuery<HotelRoom> query = em.createQuery("SELECT h FROM HotelRoom h", HotelRoom.class);
        List<HotelRoom> roomList = query.getResultList();

        em.getTransaction().commit();

        return roomList;
    }

    public static List<Guest> getAllGuests() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("admin");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        TypedQuery<Guest> query = em.createQuery("SELECT h FROM Guest h", Guest.class);
        List<Guest> guestList = query.getResultList();

        em.getTransaction().commit();

        return guestList;
    }

    public static void dropDay(int id) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("admin");
        EntityManager em = emf.createEntityManager();
        log.info("редактирование пользователя в бд");
        em.getTransaction().begin();

        Guest guest = em.find(Guest.class, id);


            if (guest.getDuration() > 1) {
                System.out.println("заходит?");
                guest.setDuration(guest.getDuration() - 1);
                System.out.println(guest.getDuration());
            } else {
                deleteById(guest.getGuestId());
            }
        em.getTransaction().commit();




    }
}

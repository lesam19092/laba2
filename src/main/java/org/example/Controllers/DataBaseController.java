package org.example.Controllers;

import org.apache.log4j.Logger;
import org.example.Entities.Guest;
import org.example.Entities.HotelRoom;
import org.example.Entities.Worker;
import org.example.interfaces.HotelInterface;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.util.List;

public class DataBaseController {
    private static final Logger log = Logger.getLogger(HotelInterface.class);

    public static boolean saveGuestToDB(Guest guest) {
      /*  добавление удаление редактирования для всех сущностей

        // модальное подтверждение удаления
        //логирование
        // сообщение об ошибках
        + все тз
                //генерация пдф нормальная
        просмотр кода (возможно)*/


        //  проверка при добавлении на комнату , существует ли она или нет

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

    public static void deleteGuestById(int id) {

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

    public static boolean redactGuestById(int id, String name, int age, int room, int duration) {


        boolean flag = false;

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("admin");
        EntityManager em = emf.createEntityManager();
        log.info("редактирование пользователя в бд");
        em.getTransaction().begin();

        Guest guest = em.find(Guest.class, id);
        if (guest != null) {

            guest.setFullName(name);
            guest.setAge(age);
            guest.setDuration(duration);
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

    public static List<Worker> getAllWorker() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("admin");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        TypedQuery<Worker> query = em.createQuery("SELECT h FROM Worker h", Worker.class);
        List<Worker> workerList = query.getResultList();

        em.getTransaction().commit();

        return workerList;

    }

    public static void dropDay(int id) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("admin");
        EntityManager em = emf.createEntityManager();
        log.info("редактирование пользователя в бд");
        em.getTransaction().begin();

        Guest guest = em.find(Guest.class, id);


        if (guest.getDuration() > 1) {
            guest.setDuration(guest.getDuration() - 1);
        } else {
            deleteGuestById(guest.getGuestId());
        }
        em.getTransaction().commit();


    }

    public static boolean saveWorkerToDB(Worker worker) {

        boolean flag = false;
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("admin");
        EntityManager em = emf.createEntityManager();
        log.info("Сохранение работника в бд");
        em.getTransaction().begin();
        em.persist(worker);
        em.getTransaction().commit();
        log.info("работник был сохранен");
        return true;
    }

    public static Worker getWorker(int id) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("admin");
        EntityManager em = emf.createEntityManager();
        log.info("было начато удаление пользователя в бд");
        em.getTransaction().begin();
        Worker worker = em.find(Worker.class, id);
        em.getTransaction().commit();
        return worker;

    }

    public static void deleteWorkerById(int id) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("admin");
        EntityManager em = emf.createEntityManager();
        log.info("было начато удаление работника в бд");
        em.getTransaction().begin();
        Worker worker = em.find(Worker.class, id);
        if (worker != null) {
            em.remove(worker);
            log.info(String.format("был удален работник с id : %d , c именем:%s", id, worker.getFullName()));
        } else log.info("работник не был найден");

        em.getTransaction().commit();

    }

    public static void redactWorkerById(int id, int age, String name, String profession) {


        EntityManagerFactory emf = Persistence.createEntityManagerFactory("admin");
        EntityManager em = emf.createEntityManager();
        log.info("Сохранение работника в бд");
        em.getTransaction().begin();


        Worker worker = em.find(Worker.class, id);

        if (worker != null) {
            worker.setAge(age);
            worker.setFullName(name);
            worker.setProfession(profession);
            log.info("работник был редактирован");
            em.getTransaction().commit();
            return;

        }


        em.getTransaction().commit();
        log.info("работник не был редактирован");

    }

    public static boolean saveRoomToDB(HotelRoom hotelRoom) {

        boolean flag = false;
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("admin");
        EntityManager em = emf.createEntityManager();
        log.info("Сохранение комнаты в бд");
        em.getTransaction().begin();
        em.persist(hotelRoom);
        hotelRoom.setNumber(hotelRoom.getId());
        em.getTransaction().commit();
        log.info("комната была сохранена");
        return true;
    }

    public static HotelRoom getRoomById(int id) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("admin");
        EntityManager em = emf.createEntityManager();
        log.info("было начато удаление пользователя в бд");
        em.getTransaction().begin();
        HotelRoom room = em.find(HotelRoom.class, id);
        em.getTransaction().commit();
        return room;

    }

    /*public static void deleteRoomById(int id) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("admin");
        EntityManager em = emf.createEntityManager();

        log.info("было начато удаление номера в бд");
        em.getTransaction().begin();

        HotelRoom room = em.find(HotelRoom.class, id);

        if (room != null) {
            for (Guest guest : room.getGuestbl()) {

                if (guest != null) {
                    guest.setNumberOfRoom(0);
                }
            }
            em.remove(room);
        } else {
            log.info("комната не была найдена");
        }

        em.getTransaction().commit();
    }*/
    public static void deleteRoomById(int id) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("admin");
        EntityManager em = emf.createEntityManager();

        try {
            log.info("Starting the deletion of a room in the database");
            em.getTransaction().begin();

            HotelRoom room = em.find(HotelRoom.class, id);

            if (room != null) {

                List<Guest> list = room.getGuestbl();
                em.remove(room);
                for (Guest guest : list) {
                    em.remove(guest);
                }
            } else {
                log.info("The room was not found");
            }

            em.getTransaction().commit();
            log.info("Room deletion completed successfully");
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }

            log.error("An error occurred during room deletion: " + e.getMessage());
        } finally {
            if (em != null) {
                em.close();
            }

        }
    }

    public static void redactRoomById(int id, int capacity, int price) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("admin");
        EntityManager em = emf.createEntityManager();

        try {
            log.info("Starting the deletion of a room in the database");
            em.getTransaction().begin();

            HotelRoom room = em.find(HotelRoom.class, id);

            if (room != null) {

                List<Guest> list = room.getGuestbl();
                for (Guest guest : list) {
                    em.remove(guest);
                }
                room.setCapacity(capacity);
                room.setPrice(price);
            } else {
                log.info("The room was not found");
            }

            em.getTransaction().commit();
            log.info("Room deletion completed successfully");
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }

            log.error("An error occurred during room deletion: " + e.getMessage());
        } finally {
            if (em != null) {
                em.close();
            }

        }


    }

    public static void plusDay(int id ){

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("admin");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        HotelRoom hotelRoom = em.find(HotelRoom.class, id);
        hotelRoom.setDays(hotelRoom.getDays()+1);
        em.getTransaction().commit();


    }
    public static void setZeroDay(int id ){

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("admin");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        HotelRoom hotelRoom = em.find(HotelRoom.class, id);
        hotelRoom.setDays(0);
        em.getTransaction().commit();


    }


}

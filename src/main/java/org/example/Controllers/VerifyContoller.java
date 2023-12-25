package org.example.Controllers;

import org.apache.log4j.Logger;
import org.example.Entities.Guest;
import org.example.Entities.Worker;
import org.example.interfaces.HotelInterface;

import javax.swing.*;
import java.util.InputMismatchException;
import java.util.Random;

public class VerifyContoller {


    private static final Logger log = Logger.getLogger(HotelInterface.class);

    public boolean verifyName(JFrame hotelList, String name) {

        try {
            ChecksController.checkName(name);
        } catch (ChecksController.MyException exception) {
            log.warn("введено некорректное имя", exception);
            JOptionPane.showMessageDialog(hotelList, exception.getMessage());
            return false;
        } catch (NullPointerException ex) {
            log.warn("введено некорректное имя", ex);
            JOptionPane.showMessageDialog(hotelList, "имя не может быть пустым");
            return false;
        }
        return true;
    }

    public boolean verifyAge(JFrame hotelList, String tmp_Age) {
        int age = 0;
        try {
            age = Integer.parseInt(tmp_Age);
            ChecksController.checkAge(age);
        } catch (NumberFormatException exx) {
            log.warn("введен некорректный возраст", new NumberFormatException());
            JOptionPane.showMessageDialog(hotelList, "ввод некорректных данных для int");
            return false;
        } catch (ChecksController.MyException exception) {
            log.warn("введен некорректный возраст", exception);
            JOptionPane.showMessageDialog(hotelList, exception.getMessage());
            return false;
        } catch (InputMismatchException exp) {
            log.warn("введен некорректный возраст", exp);
            JOptionPane.showMessageDialog(hotelList, exp.toString());
            return false;
        }
        return true;
    }

    public boolean verifyRoom(JFrame hotelList, String tmp_room) {
        int room = 0;
        try {
            room = Integer.parseInt(tmp_room);
            ChecksController.checkRoom(room);
        } catch (NumberFormatException exx) {
            log.warn("введен некорректный возраст", new NumberFormatException());
            JOptionPane.showMessageDialog(hotelList, "ввод некорректных данных для int");
            return false;
        } catch (ChecksController.MyException exception) {
            log.warn("введен некорректный возраст", exception);
            JOptionPane.showMessageDialog(hotelList, exception.getMessage());
            return false;
        } catch (InputMismatchException exp) {
            log.warn("введен некорректный возраст", exp);
            JOptionPane.showMessageDialog(hotelList, exp.toString());
            return false;
        }

        if (DataBaseController.getRoomById(room) == null) {
            JOptionPane.showMessageDialog(hotelList, "такой комнаты не существует");
            return false;
        }
        return true;
    }

    public boolean verifyDuration(JFrame hotelList, String tmp_duration) {
        int duration = 0;
        try {
            duration = Integer.parseInt(tmp_duration);
            ChecksController.checkDuration(duration);
        } catch (NumberFormatException exx) {
            log.warn("введен некорректный возраст", new NumberFormatException());
            JOptionPane.showMessageDialog(hotelList, "ввод некорректных данных для int");
            return false;
        } catch (ChecksController.MyException exception) {
            log.warn("введена некорректная длительность", exception);
            JOptionPane.showMessageDialog(hotelList, exception.getMessage());
            return false;
        } catch (InputMismatchException exp) {
            log.warn("введена некорректная длительность", exp);
            JOptionPane.showMessageDialog(hotelList, exp.toString());
            return false;
        }
        return true;
    }

    public boolean verifyGuestId(JFrame hotelList, String tmp_id) {

        int id = 0;
        try {

            id = Integer.parseInt(tmp_id);
            ChecksController.checkId(id);
        } catch (NumberFormatException exx) {
            log.warn("введен некорректный id", new NumberFormatException());
            JOptionPane.showMessageDialog(hotelList, "ввод некорректных данных для int");
            return false;
        } catch (ChecksController.MyException exception) {
            log.warn("введен некорректный id", exception);
            JOptionPane.showMessageDialog(hotelList, exception.getMessage());
            return false;
        } catch (InputMismatchException exp) {
            log.warn("введен некорректный id", exp);
            JOptionPane.showMessageDialog(hotelList, exp.toString());
            return false;
        }

        Guest guest_t = DataBaseController.getGuest(id);

        if (guest_t == null) {
            JOptionPane.showMessageDialog(hotelList, "Гость не был найден");
            return false;
        }

        return true;
    }

    public boolean verifyWorkerId(JFrame hotelList, String tmp_id) {

        int id = 0;
        try {

            id = Integer.parseInt(tmp_id);
            ChecksController.checkId(id);
        } catch (NumberFormatException exx) {
            log.warn("введен некорректный id", new NumberFormatException());
            JOptionPane.showMessageDialog(hotelList, "ввод некорректных данных для int");
            return false;
        } catch (ChecksController.MyException exception) {
            log.warn("введен некорректный id", exception);
            JOptionPane.showMessageDialog(hotelList, exception.getMessage());
            return false;
        } catch (InputMismatchException exp) {
            log.warn("введен некорректный id", exp);
            JOptionPane.showMessageDialog(hotelList, exp.toString());
            return false;
        }

        Worker worker = DataBaseController.getWorker(id);

        if (worker == null) {
            JOptionPane.showMessageDialog(hotelList, "Гость не был найден");
            return false;
        }

        return true;
    }

    public boolean verifyProfession(JFrame hotelList, String profession) {

        try {
            ChecksController.checkName(profession);
        } catch (ChecksController.MyException exception) {
            log.warn("введено некорректное имя", exception);
            JOptionPane.showMessageDialog(hotelList, exception.getMessage());
            return false;
        } catch (NullPointerException ex) {
            log.warn("введено некорректное имя", ex);
            JOptionPane.showMessageDialog(hotelList, "профессия не может быть пустой");
            return false;
        }
        return true;
    }


    public boolean verifyCapacity(JFrame roomlist, String tmp_capaity) {
        int capacity = 0;
        try {
            capacity = Integer.parseInt(tmp_capaity);
            ChecksController.checkCapacity(capacity);
        } catch (NumberFormatException exx) {
            log.warn("введен некорректный возраст", new NumberFormatException());
            JOptionPane.showMessageDialog(roomlist, "ввод некорректных данных для int");
            return false;
        } catch (ChecksController.MyException exception) {
            log.warn("введена некорректная длительност ", exception);
            JOptionPane.showMessageDialog(roomlist, "введена некорректная длительность(должна быть от 1 до 6) ");
            return false;
        } catch (InputMismatchException exp) {
            log.warn("введена некорректная длительность возраст", exp);
            JOptionPane.showMessageDialog(roomlist, exp.toString());
            return false;
        }
        return true;
    }

    public boolean verifyPrice(JFrame roomlist, String tmp_price) {
        int price = 0;
        try {
            price = Integer.parseInt(tmp_price);
            ChecksController.checkPrice(price);
        } catch (NumberFormatException exx) {
            log.warn("введен некорректный возраст", new NumberFormatException());
            JOptionPane.showMessageDialog(roomlist, "ввод некорректных данных для int");
            return false;
        } catch (ChecksController.MyException exception) {
            log.warn("введена некорректная длительность ", exception);
            JOptionPane.showMessageDialog(roomlist, exception.getMessage());
            return false;
        } catch (InputMismatchException exp) {
            log.warn("введена некорректная длительность возраст", exp);
            JOptionPane.showMessageDialog(roomlist, exp.toString());
            return false;
        }
        return true;
    }


    public String kapcha() {
        int leftLimit = 97;
        int rightLimit = 122;
        int targetStringLength = 5;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        String generatedString = buffer.toString();
        return generatedString;
    }

}

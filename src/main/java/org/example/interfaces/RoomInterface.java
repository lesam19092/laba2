package org.example.interfaces;

import org.example.DataBaseController;
import org.example.Guest;
import org.example.HotelRoom;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class RoomInterface {
    private JFrame roomList;
    private DefaultTableModel model;


    private JToolBar toolBar;

    private JScrollPane scroll;
    private JTable guests;
    private JComboBox name;
    private JTextField room;
    private JButton filter;

    public void show(){
        roomList = new JFrame("Список комнат");
        roomList.setSize(800, 600);
        roomList.setLocation(100, 100);


        String[] colums = { "id","вместимость" ,"комната", "занята/не занято", "цена","id_гостей" , "имя гостя" };
        String[][] data = null;

        model = new DefaultTableModel(data, colums);
        guests = new JTable(model);
        scroll = new JScrollPane(guests);

        roomList.add(scroll, BorderLayout.CENTER);
        roomList.setVisible(true);
        сompletiomRoomsFromBd();

    }


    private void сompletiomRoomsFromBd(){
        List<HotelRoom> list = DataBaseController.getAllHotelRooms();
        for (HotelRoom room : list){
            List<String> tmp = new ArrayList<>();
            tmp.add(Integer.toString(room.getId()));
            tmp.add(Integer.toString(room.getCapacity()));
            tmp.add(Integer.toString(room.getNumber()));
            tmp.add(String.valueOf(room.getOccupied()));
            tmp.add(Integer.toString(room.getPrice()));
            List<Guest> tmp1 =  room.getGuestbl();
            String str_id = "" ;
            String str_names = "";
            for (int i =0 ; i<tmp1.size();i++){
                str_id =str_id + Integer.toString(tmp1.get(i).getGuestId())+" ";
                str_names=str_names + tmp1.get(i).getFullName()+" ";
            }
            tmp.add(str_id);
            tmp.add(str_names);

            Vector RoomtOnTable = new Vector<String>(tmp);
            model.addRow(RoomtOnTable);
            guests = new JTable(model);
            scroll = new JScrollPane(guests);

            roomList.add(scroll, BorderLayout.CENTER);
            roomList.setVisible(true);
        }
    }
}

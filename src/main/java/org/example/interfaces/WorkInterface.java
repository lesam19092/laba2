package org.example.interfaces;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class WorkInterface extends JFrame {
    private JFrame worklist;
    private DefaultTableModel model;


    private JToolBar toolBar;

    private JScrollPane scroll;
    private JTable guests;
    private JComboBox name;
    private JTextField room;
    private JButton filter;

    public void show(){
        worklist = new JFrame("Список работников");
        worklist.setSize(600, 500);
        worklist.setLocation(100, 100);
       // worklist.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        String[] colums = { "id", "Имя", "возраст", "профессия", };
        String[][] data = {{ "3", "Гловацкий Даниил Армэнович", "19", "повар"}, { "10", "Михаил Плюснин", "20", "учитель"}};
        model = new DefaultTableModel(data, colums);
        guests = new JTable(model);
        scroll = new JScrollPane(guests);


        worklist.add(scroll, BorderLayout.CENTER);
        worklist.setVisible(true);
    }
}

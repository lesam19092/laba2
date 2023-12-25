package org.example.interfaces;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRXmlDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.view.JasperViewer;
import org.apache.log4j.Logger;
import org.example.Controllers.ChecksController;
import org.example.Controllers.DataBaseController;
import org.example.Controllers.VerifyContoller;
import org.example.Entities.Guest;
import org.example.Entities.HotelRoom;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class RoomInterface {
    private JFrame roomList;
    private DefaultTableModel model;


    private JScrollPane scroll;
    private JTable guests;

    private VerifyContoller verify = new VerifyContoller();
    private static final Logger log = Logger.getLogger(HotelInterface.class);

    public void show() {
        log.info("---------------------------------------------------");

        log.info("Открытыие пользовательского интерфейса");
        roomList = new JFrame("Список комнат");
        roomList.setSize(1100, 800);
        roomList.setLocation(100, 100);

        JButton add = new JButton(new ImageIcon("C:\\Users\\danil\\Desktop\\add.jpg"));
        JButton redact = new JButton(new ImageIcon("C:\\Users\\danil\\Desktop\\redact.png"));
        JButton delete = new JButton(new ImageIcon("C:\\Users\\danil\\Desktop\\delete.png"));
        JButton free = new JButton(new ImageIcon("C:\\Users\\danil\\Desktop\\free_rooms.jpg"));
        JButton all = new JButton(new ImageIcon("C:\\Users\\danil\\Desktop\\all.png"));
        JButton createPdf = new JButton(new ImageIcon("C:\\Users\\danil\\Desktop\\pdf.jpeg"));

        add.setToolTipText("добавить комнату");
        redact.setToolTipText("редактировать таблицу");
        delete.setToolTipText("удалить комнату");
        free.setToolTipText("список свободных комнат");
        all.setToolTipText("показать список всех комнат");
        createPdf.setToolTipText("получить прейскурант");


        JToolBar toolBar = new JToolBar("Панель инструментов");
        toolBar.add(add);
        toolBar.add(redact);
        toolBar.add(delete);
        toolBar.add(free);
        toolBar.add(all);

        toolBar.add(createPdf);

        roomList.setLayout(new BorderLayout());
        roomList.add(toolBar, BorderLayout.NORTH);


        String[] colums = {"id", "вместимость", "комната", "занята/не занято", "цена за день", "id_гостей", "имя гостя"};

        model = new DefaultTableModel(null, colums);
        guests = new JTable(model);
        scroll = new JScrollPane(guests);

        roomList.add(scroll, BorderLayout.CENTER);
        roomList.setVisible(true);

        сompletiomRoomsFromBd();


        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                log.info("было начато сохранение комнаты вручную");


                String tmp_capacity = JOptionPane.showInputDialog(
                        roomList,
                        "Введите вместимость комнаты",
                        "авторизация",
                        JOptionPane.QUESTION_MESSAGE);
                boolean flag = verify.verifyCapacity(roomList, tmp_capacity);
                if (!flag) return;

                String tmp_price = JOptionPane.showInputDialog(
                        roomList,
                        "Введите стоимость проживания",
                        "авторизация",
                        JOptionPane.QUESTION_MESSAGE);
                boolean flag1 = verify.verifyPrice(roomList, tmp_price);
                if (!flag1) return;


                HotelRoom hotelRoom = new HotelRoom();
                hotelRoom.setOccupied(false);
                hotelRoom.setCapacity(Integer.parseInt(tmp_capacity));
                hotelRoom.setPrice(Integer.parseInt(tmp_price));


                DataBaseController.saveRoomToDB(hotelRoom);


                List<String> save = new ArrayList<>();
                save.add(Integer.toString(hotelRoom.getId()));
                save.add(Integer.toString(hotelRoom.getCapacity()));
                save.add(Integer.toString(hotelRoom.getNumber()));
                save.add("не занята");
                save.add(Integer.toString(hotelRoom.getPrice()));


                saveRoomToTable(save);

            }
        });
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                log.info("было начато удаление работника по id");

                String tmp_id = JOptionPane.showInputDialog(
                        roomList,
                        "Введите id комнаты",
                        "удаление",
                        JOptionPane.QUESTION_MESSAGE);
                int id = 0;
                try {
                    id = Integer.parseInt(tmp_id);
                    ChecksController.checkId(id);
                } catch (NumberFormatException exx) {
                    log.warn("введен некорректный id", new NumberFormatException());
                    JOptionPane.showMessageDialog(roomList, "ввод некорректных данных для int");
                    return;
                } catch (ChecksController.MyException exception) {
                    log.warn("введен некорректный id", exception);
                    JOptionPane.showMessageDialog(roomList, exception.getMessage());
                    return;
                } catch (InputMismatchException exp) {
                    log.warn("введен некорректный возраст", exp);
                    JOptionPane.showMessageDialog(roomList, exp.toString());
                    return;
                }

                if (DataBaseController.getRoomById(id) != null) {


                    String s_kapcha = verify.kapcha();


                    // JOptionPane.showInputDialog(roomList,"После удаления будут выселены и жители с их номеров");
                    int reply = JOptionPane.showConfirmDialog(null, "После удаления будут выселены и жители с их номеров", "удаление", JOptionPane.YES_NO_OPTION);
                    if (reply != JOptionPane.YES_OPTION) {
                        return;
                    }

                    String tmp_kapcha = JOptionPane.showInputDialog(
                            roomList,
                            "Введите капчу " + s_kapcha + " для подтверждения удаления",
                            "удаление",
                            JOptionPane.QUESTION_MESSAGE);

                    if (tmp_kapcha != null && s_kapcha.equals(tmp_kapcha)) {


                        DataBaseController.deleteRoomById(id);


                        for (int i = 0; i < model.getRowCount(); i++) {
                            if (Integer.parseInt(model.getValueAt(i, 0).toString()) == id) {
                                model.removeRow(i);
                            }
                        }
                        JOptionPane.showMessageDialog(roomList, "комната была удален");
                        log.info("комната была удалена из таблички");
                    } else {
                        JOptionPane.showMessageDialog(roomList, "капча была введена неверно");
                    }
                } else {
                    JOptionPane.showMessageDialog(roomList, "комната с данным Id не был найден");
                }


            }
        });

        redact.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                String tmp_id = JOptionPane.showInputDialog(
                        roomList,
                        "Введите id комнаты",
                        "редактирование",
                        JOptionPane.QUESTION_MESSAGE);
                boolean flag5 = verify.verifyRoom(roomList, tmp_id);
                if (!flag5) return;


                String tmp_capacity = JOptionPane.showInputDialog(
                        roomList,
                        "Введите вместимость комнаты",
                        "редактирование",
                        JOptionPane.QUESTION_MESSAGE);
                boolean flag2 = verify.verifyCapacity(roomList, tmp_capacity);
                if (!flag2) return;

                String tmp_price = JOptionPane.showInputDialog(
                        roomList,
                        "Введите цену за комнату",
                        "редактинование",
                        JOptionPane.QUESTION_MESSAGE);
                boolean flag1 = verify.verifyPrice(roomList, tmp_price);
                if (!flag1) return;

                int reply = JOptionPane.showConfirmDialog(null, "После редактирования будут выселены и жители с их номеров", "редактирование", JOptionPane.YES_NO_OPTION);
                if (reply != JOptionPane.YES_OPTION) {
                    return;
                }


                DataBaseController.redactRoomById(Integer.parseInt(tmp_id), Integer.parseInt(tmp_capacity), Integer.parseInt(tmp_price));


                HotelRoom hotelRoom_tmp = DataBaseController.getRoomById(Integer.parseInt(tmp_id));


                for (int i = 0; i < model.getRowCount(); i++) {
                    if (Integer.parseInt(model.getValueAt(i, 0).toString()) == Integer.parseInt(tmp_id)) {
                        model.setValueAt(hotelRoom_tmp.getCapacity(), i, 1);
                        model.setValueAt(hotelRoom_tmp.getPrice(), i, 4);
                        model.setValueAt(null, i, 5);
                        model.setValueAt(null, i, 6);

                    }
                }

                JOptionPane.showMessageDialog(roomList, "данные были редактированы");


            }
        });

        free.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.setRowCount(0);
                List<HotelRoom> list = DataBaseController.getAllHotelRooms();
                for (HotelRoom room : list) {
                    List<String> tmp = new ArrayList<>();
                    tmp.add(Integer.toString(room.getId()));
                    tmp.add(Integer.toString(room.getCapacity()));
                    tmp.add(Integer.toString(room.getNumber()));
                    if (room.getOccupied() == false) tmp.add("не занята");
                    else {
                        continue;
                    }
                    tmp.add(Integer.toString(room.getPrice()));
                    List<Guest> tmp1 = room.getGuestbl();
                    String str_id = "";
                    String str_names = "";
                    for (int i = 0; i < tmp1.size(); i++) {
                        str_id = str_id + Integer.toString(tmp1.get(i).getGuestId()) + " ";
                        str_names = str_names + tmp1.get(i).getFullName() + " ";
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


        });

        all.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.setRowCount(0);
                сompletiomRoomsFromBd();
            }
        });

        createPdf.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                log.info("начало создания pdf отчета");

                log.info("начало создания xml файла");
                Document doc = null;
                try {
                    DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                    doc = builder.newDocument();
                } catch (ParserConfigurationException exception) {
                    log.warn("ошибка при создании xml файла");
                    exception.printStackTrace();
                }
                log.info("осуществление переноса комнат ");
                Node guestlist = doc.createElement("rooms");
                doc.appendChild(guestlist);
                for (int i = 0; i < model.getRowCount(); i++) {
                    Element room = doc.createElement("room");
                    guestlist.appendChild(room);
                    room.setAttribute("id", (String) model.getValueAt(i, 0));
                    room.setAttribute("room", (String) model.getValueAt(i, 2));
                    room.setAttribute("price", (String) model.getValueAt(i, 4));
                    room.setAttribute("capacity", (String) model.getValueAt(i, 1));
                }

                try {
                    Transformer trans = TransformerFactory.newInstance().newTransformer();
                    FileWriter fw = new FileWriter("komnat.xml");
                    trans.transform(new DOMSource(doc), new StreamResult(fw));
                } catch (TransformerConfigurationException exception) {
                    log.warn("ошибка при создании xml файла", exception);
                    exception.printStackTrace();
                } catch (TransformerException exception) {
                    log.warn("ошибка при создании xml файла", exception);
                    exception.printStackTrace();
                } catch (IOException exception) {
                    log.warn("ошибка при создании xml файла", exception);
                    exception.printStackTrace();
                }

                log.info("xml файл создан");


                String data = "C:\\Users\\danil\\IdeaProjects\\laba2\\komnat.xml";

                String tmp = "C:\\Users\\danil\\IdeaProjects\\laba2\\prices.jrxml";

                String res = "C:\\Users\\danil\\Desktop\\prices.pdf";



               printpdf(data, tmp, res);

                log.info("pdf отчет создан");
            }
        });


    }

    private void saveRoomToTable(List<String> save) {

        Vector workerOnTable = new Vector<String>(save);
        model.addRow(workerOnTable);
        guests = new JTable(model);
        scroll = new JScrollPane(guests);

        roomList.add(scroll, BorderLayout.CENTER);
        roomList.setVisible(true);
    }


    private void сompletiomRoomsFromBd() {
        List<HotelRoom> list = DataBaseController.getAllHotelRooms();
        for (HotelRoom room : list) {
            List<String> tmp = new ArrayList<>();
            tmp.add(Integer.toString(room.getId()));
            tmp.add(Integer.toString(room.getCapacity()));
            tmp.add(Integer.toString(room.getNumber()));
            if (room.getOccupied() == false) tmp.add("не занята");
            else tmp.add("занята");
            tmp.add(Integer.toString(room.getPrice()));
            List<Guest> tmp1 = room.getGuestbl();
            String str_id = "";
            String str_names = "";
            for (int i = 0; i < tmp1.size(); i++) {
                str_id = str_id + Integer.toString(tmp1.get(i).getGuestId()) + " ";
                str_names = str_names + tmp1.get(i).getFullName() + " ";
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
    private static void printpdf(String document, String template, String resultpath) {
        try {
            JRDataSource ds = new JRXmlDataSource(document, "/rooms/room");
            JasperReport jasperReport = JasperCompileManager.compileReport(template);
            JasperPrint print = JasperFillManager.fillReport(jasperReport, new HashMap<String, Object>(), ds);
            JRExporter exporter = null;
            exporter = new JRPdfExporter();
            exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, resultpath);
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
            exporter.exportReport();
            JasperViewer.viewReport(print, true);
        } catch (JRException e) {
            e.printStackTrace();
        }
    }
}

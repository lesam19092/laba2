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
import java.util.List;
import java.util.*;

public class HotelInterface extends JFrame {
    private JFrame hotelList;
    private DefaultTableModel model;

    private JScrollPane scroll;
    private JTable guests;
    private static final Logger log = Logger.getLogger(HotelInterface.class);


    private List<Guest> listLeft = new ArrayList<>();

    private int cash;

    private VerifyContoller verify = new VerifyContoller();

    private int days;
    private int guestleft;

    public void show() {


        log.info("------------------------- --------------------------");
        log.info("Открытыие пользовательского интерфейса");
        hotelList = new JFrame("Список гостей");
        hotelList.setSize(1100, 800);
        hotelList.setLocation(100, 100);
        hotelList.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton add = new JButton(new ImageIcon("C:\\Users\\danil\\Desktop\\add.jpg"));
        JButton redact = new JButton(new ImageIcon("C:\\Users\\danil\\Desktop\\redact.png"));
        JButton pechat = new JButton(new ImageIcon("C:\\Users\\danil\\Desktop\\pechat.png"));
        JButton delete = new JButton(new ImageIcon("C:\\Users\\danil\\Desktop\\delete.png"));
        JButton workList = new JButton(new ImageIcon("C:\\Users\\danil\\Desktop\\работники.png"));
        // JButton readXml = new JButton(new ImageIcon("C:\\Users\\danil\\Desktop\\xml.jpeg"));
        JButton createPdf = new JButton(new ImageIcon("C:\\Users\\danil\\Desktop\\pdf.jpeg"));
        JButton komnat = new JButton(new ImageIcon("C:\\Users\\danil\\Desktop\\комнаты.png"));
        JButton oneday = new JButton(new ImageIcon("C:\\Users\\danil\\Desktop\\plusone.png"));
        JButton dollar = new JButton(new ImageIcon("C:\\Users\\danil\\Desktop\\dollar.png"));
        JButton update = new JButton(new ImageIcon("C:\\Users\\danil\\Desktop\\update.jpg"));
        JButton statisctic = new JButton(new ImageIcon("C:\\Users\\danil\\Desktop\\stat.png"));


        add.setToolTipText("добавить гостя");
        redact.setToolTipText("редактировать таблицу");
        delete.setToolTipText("удалить гостя");

        pechat.setToolTipText("распечатать таблицу");
        // readXml.setToolTipText("считать xml файл");
        createPdf.setToolTipText("создать pdf отчет");

        workList.setToolTipText("открыть список работников");
        komnat.setToolTipText("открыть список комнат");

        oneday.setToolTipText("забрать у всех один день");
        dollar.setToolTipText("показать стоимость за сеанс");
        update.setToolTipText("обновить таблицу");
        statisctic.setToolTipText("получить статистику");


        JToolBar toolBar = new JToolBar("Панель инструментов");
        toolBar.add(add);
        toolBar.add(redact);
        toolBar.add(pechat);
        toolBar.add(delete);
        toolBar.add(workList);
        // toolBar.add(readXml);
        toolBar.add(createPdf);
        toolBar.add(komnat);
        toolBar.add(oneday);
        toolBar.add(dollar);
        toolBar.add(update);
        toolBar.add(statisctic);
        hotelList.setLayout(new BorderLayout());
        hotelList.add(toolBar, BorderLayout.NORTH);

        String[] colums = {"id", "Имя", "возраст", "комната", "длительность пребывания"};

        model = new DefaultTableModel(null, colums);
        guests = new JTable(model);
        scroll = new JScrollPane(guests);

        сompletiomGuestFromBd();

        hotelList.add(scroll, BorderLayout.CENTER);


        hotelList.setVisible(true);


        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                log.info("было начато сохранение гостя вручную");

                String name = JOptionPane.showInputDialog(
                        hotelList,
                        "Введите имя проживающего",
                        "авторизация",
                        JOptionPane.QUESTION_MESSAGE);
                boolean flag = verify.verifyName(hotelList, name);
                if (!flag) return;
                String tmp_Age = JOptionPane.showInputDialog(
                        hotelList,
                        "Введите возраст проживающего",
                        "авторизация",
                        JOptionPane.QUESTION_MESSAGE);
                boolean flag1 = verify.verifyAge(hotelList, tmp_Age);
                if (!flag1) return;
                String tmp_room = JOptionPane.showInputDialog(
                        hotelList,
                        "Введите комнату проживающего",
                        "авторизация",
                        JOptionPane.QUESTION_MESSAGE);
                boolean flag2 = verify.verifyRoom(hotelList, tmp_room);
                if (!flag2) return;
                String tmp_duration = JOptionPane.showInputDialog(
                        hotelList,
                        "Введите длительность пребывания",
                        "авторизация",
                        JOptionPane.QUESTION_MESSAGE);
                boolean flag3 = verify.verifyDuration(hotelList, tmp_duration);
                if (!flag3) return;


                Guest guest = new Guest();
                guest.setDuration(Integer.parseInt(tmp_duration));
                guest.setAge(Integer.parseInt(tmp_Age));
                guest.setFullName(name);
                guest.setNumberOfRoom(Integer.parseInt(tmp_room));


                if (DataBaseController.saveGuestToDB(guest) == true) {


                    List<String> save = new ArrayList<>();
                    save.add(Integer.toString(guest.getGuestId()));
                    save.add(name);
                    save.add(tmp_Age);
                    save.add(tmp_room);
                    save.add(tmp_duration);


                    saveGuestToTable(save);
                    log.info(String.format("Был добавлен гость с именем : %s , возраст : %s , комната : %s", name, tmp_Age, tmp_room));
                } else JOptionPane.showMessageDialog(hotelList, "комната переполена, попробуйте другую");
            }
        });
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                log.info("было начато удаление гостя по id");

                String tmp_id = JOptionPane.showInputDialog(
                        hotelList,
                        "Введите id проживающего",
                        "удаление",
                        JOptionPane.QUESTION_MESSAGE);
                int id = 0;
                try {
                    id = Integer.parseInt(tmp_id);
                    ChecksController.checkId(id);
                } catch (NumberFormatException exx) {
                    log.warn("введен некорректный id", new NumberFormatException());
                    JOptionPane.showMessageDialog(hotelList, "ввод некорректных данных для int");
                    return;
                } catch (ChecksController.MyException exception) {
                    log.warn("введен некорректный id", exception);
                    JOptionPane.showMessageDialog(hotelList, exception.getMessage());
                    return;
                } catch (InputMismatchException exp) {
                    log.warn("введен некорректный возраст", exp);
                    JOptionPane.showMessageDialog(hotelList, exp.toString());
                    return;
                }

                if (DataBaseController.getGuest(id) != null) {


                    String s_kapcha = verify.kapcha();

                    String tmp_kapcha = JOptionPane.showInputDialog(
                            hotelList,
                            "Введите капчу " + s_kapcha + " для подтверждения удаления",
                            "удаление",
                            JOptionPane.QUESTION_MESSAGE);

                    if (tmp_kapcha != null && s_kapcha.equals(tmp_kapcha)) {


                        DataBaseController.deleteGuestById(id);


                        for (int i = 0; i < model.getRowCount(); i++) {
                            if (Integer.parseInt(model.getValueAt(i, 0).toString()) == id) {
                                model.removeRow(i);
                                guestleft++;
                            }
                        }
                        JOptionPane.showMessageDialog(hotelList, "пользователь удален");
                        log.info("пользователь был удален из таблички");
                    } else {
                        JOptionPane.showMessageDialog(hotelList, "капча была введена неверно");
                    }
                } else {
                    JOptionPane.showMessageDialog(hotelList, "гость с данным Id не был найден");
                }
            }

        });
        redact.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                String tmp_id = JOptionPane.showInputDialog(
                        hotelList,
                        "Введите id проживающего",
                        "редактирование",
                        JOptionPane.QUESTION_MESSAGE);
                boolean flag5 = verify.verifyGuestId(hotelList, tmp_id);
                if (!flag5) return;


                String name = JOptionPane.showInputDialog(
                        hotelList,
                        "Введите имя проживающего",
                        "редактинование",
                        JOptionPane.QUESTION_MESSAGE);
                boolean flag1 = verify.verifyName(hotelList, name);
                if (!flag1) return;
                String tmp_Age = JOptionPane.showInputDialog(
                        hotelList,
                        "Введите возраст проживающего",
                        "редактирование",
                        JOptionPane.QUESTION_MESSAGE);
                boolean flag2 = verify.verifyAge(hotelList, tmp_Age);
                if (!flag2) return;
                String tmp_room = JOptionPane.showInputDialog(
                        hotelList,
                        "Введите комнату проживающего",
                        "редактирование",
                        JOptionPane.QUESTION_MESSAGE);
                boolean flag3 = verify.verifyRoom(hotelList, tmp_room);
                if (!flag3) return;
                String tmp_duration = JOptionPane.showInputDialog(
                        hotelList,
                        "Введите длительность пребывания",
                        "редактирование",
                        JOptionPane.QUESTION_MESSAGE);
                boolean flag4 = verify.verifyDuration(hotelList, tmp_duration);
                if (!flag4) return;


                boolean flag = DataBaseController.redactGuestById(Integer.parseInt(tmp_id), name, Integer.parseInt(tmp_Age), Integer.parseInt(tmp_room), Integer.parseInt(tmp_duration));

                Guest guest_tmp = DataBaseController.getGuest(Integer.parseInt(tmp_id));


                if (flag == true) {

                    for (int i = 0; i < model.getRowCount(); i++) {
                        if (Integer.parseInt(model.getValueAt(i, 0).toString()) == Integer.parseInt(tmp_id)) {
                            model.setValueAt(guest_tmp.getFullName(), i, 1);
                            model.setValueAt(guest_tmp.getAge(), i, 2);
                            model.setValueAt(guest_tmp.getNumberOfRoom(), i, 3);
                            model.setValueAt(guest_tmp.getDuration(), i, 4);
                        }
                    }

                    JOptionPane.showMessageDialog(hotelList, "данные были редактированы");
                    return;
                }

                JOptionPane.showMessageDialog(hotelList, "данные не были редактированы");
            }

        });
        workList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new WorkInterface().show();
            }
        });
        komnat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                new RoomInterface().show();
            }
        });
        pechat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                log.info("начало создания xml файла");
                Document doc = null;
                try {
                    DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                    doc = builder.newDocument();
                } catch (ParserConfigurationException exception) {
                    log.warn("ошибка при создании xml файла");
                    exception.printStackTrace();
                }
                log.info("осуществление переноса гостей ");
                Node guestlist = doc.createElement("guestlist");
                doc.appendChild(guestlist);
                for (int i = 0; i < model.getRowCount(); i++) {
                    Element guest = doc.createElement("guest");
                    guestlist.appendChild(guest);
                    guest.setAttribute("id", (String) model.getValueAt(i, 0));
                    guest.setAttribute("name", (String) model.getValueAt(i, 1));
                    guest.setAttribute("age", (String) model.getValueAt(i, 2));
                    guest.setAttribute("room", (String) model.getValueAt(i, 3));
                    guest.setAttribute("duration", model.getValueAt(i, 4).toString());

                }

                try {
                    Transformer trans = TransformerFactory.newInstance().newTransformer();
                    FileWriter fw = new FileWriter("laba.xml");
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
            }
        });
       /* readXml.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                log.info("начало считывания xml файла");
                Document doc = null;
                try {
                    DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                    doc = dBuilder.parse(new File("laba.xml"));
                    doc.getDocumentElement().normalize();
                } catch (ParserConfigurationException exception) {
                    log.warn("ошибка при считывании xml файла", exception);
                    exception.printStackTrace();
                } catch (SAXException exception) {
                    log.warn("ошибка при считывании xml файла", exception);
                    exception.printStackTrace();
                } catch (IOException exception) {
                    log.warn("ошибка при считывании xml файла", exception);
                    exception.printStackTrace();
                }
                NodeList nlGuest = doc.getElementsByTagName("guest");
                for (int temp = 0; temp < nlGuest.getLength(); temp++) {
                    Node elem = nlGuest.item(temp);
                    NamedNodeMap attrs = elem.getAttributes();
                    String id = attrs.getNamedItem("id").getNodeValue();
                    String name = attrs.getNamedItem("name").getNodeValue();
                    String age = attrs.getNamedItem("age").getNodeValue();
                    String room = attrs.getNamedItem("room").getNodeValue();
                    String duration = attrs.getNamedItem("duration").getNodeValue();

                    model.addRow(new String[]{id, name, age, room, duration});
                }
                log.info("xml файл был считан ");
            }
        });*/
        createPdf.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                log.info("начало создания pdf отчета");
                String data = "C:\\Users\\danil\\IdeaProjects\\laba2\\laba.xml";

                String res = "C:\\Users\\danil\\Desktop\\res.pdf";

                String tmp = "C:\\Users\\danil\\IdeaProjects\\laba2\\pdf.jrxml";


                printpdf(data, tmp, res);

                log.info("pdf отчет создан");
            }
        });
        oneday.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                days++;
                List<Guest> tmplist = DataBaseController.getAllGuests();
                for (Guest guest : tmplist) {
                    DataBaseController.dropDay(guest.getGuestId());
                    cash += guest.getHotelnomer().getPrice();
                }


                int a = model.getRowCount();

                List<Integer> list = new ArrayList<>();

                for (int i = 0; i < a; i++) {

                    int duration = Integer.parseInt(model.getValueAt(i, 4).toString());
                    if (duration == 1) {
                        Guest guest = new Guest();
                        guest.setGuestId(Integer.parseInt(model.getValueAt(i, 0).toString()));
                        guest.setFullName(model.getValueAt(i, 1).toString());
                        guest.setAge(Integer.parseInt(model.getValueAt(i, 2).toString()));
                        guest.setNumberOfRoom(Integer.parseInt(model.getValueAt(i, 3).toString()));
                        DataBaseController.plusDay(Integer.parseInt(model.getValueAt(i, 3).toString()));
                        listLeft.add(guest);
                        list.add(Integer.parseInt(model.getValueAt(i, 0).toString()));


                    } else {
                        model.setValueAt(duration - 1, i, 4);

                        DataBaseController.plusDay(Integer.parseInt(model.getValueAt(i, 3).toString()));

                    }

                }


                for (int j = 0; j < list.size(); j++) {
                    for (int i = 0; i < model.getRowCount(); i++) {
                        if (Integer.parseInt(model.getValueAt(i, 0).toString()) == list.get(j)) {
                            model.removeRow(i);
                            guestleft++;
                        }
                    }
                }

            }


        });
        dollar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(hotelList, String.format("вы зарабоатли %d, столько людей покинуло ваш отель  %d",cash,guestleft) );
            }
        });

        update.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.setRowCount(0);
                сompletiomGuestFromBd();
            }
        });

        statisctic.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                log.info("начало создания xml файла");
                Document doc = null;
                try {
                    DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                    doc = builder.newDocument();
                } catch (ParserConfigurationException exception) {
                    log.warn("ошибка при создании xml файла");
                    exception.printStackTrace();
                }
                log.info("осуществление переноса гостей ");
                Node statlist = doc.createElement("statlist");
                doc.appendChild(statlist);
                Element stat = doc.createElement("stata");
                statlist.appendChild(stat);
                stat.setAttribute("cash", Integer.toString(cash));
                stat.setAttribute("guestleft", Integer.toString(guestleft));

                try {
                    Transformer trans = TransformerFactory.newInstance().newTransformer();
                    FileWriter fw = new FileWriter("statistic.xml");
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


                List<HotelRoom> list = DataBaseController.getAllHotelRooms();
                System.out.println(days);
                for (int i = 0; i < list.size(); i++) {
                    System.out.println(list.get(i).getId() + " " + list.get(i).getNumber() + " " + list.get(i).getDays());
                }


                log.info("начало создания xml файла");
                Document moc = null;
                try {
                    DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                    moc = builder.newDocument();
                } catch (ParserConfigurationException exception) {
                    log.warn("ошибка при создании xml файла");
                    exception.printStackTrace();
                }



                log.info("осуществление переноса гостей ");
                Node statistika = moc.createElement("komnati");
                moc.appendChild(statistika);
                for (int i = 0; i < DataBaseController.getAllHotelRooms().size(); i++) {
                    Element komnata = moc.createElement("komnata");
                    statistika.appendChild(komnata);
                    komnata.setAttribute("id", Integer.toString(DataBaseController.getAllHotelRooms().get(i).getId()));
                    komnata.setAttribute("free", Integer.toString(days-DataBaseController.getAllHotelRooms().get(i).getDays()));
                    komnata.setAttribute("occupied", Integer.toString(DataBaseController.getAllHotelRooms().get(i).getDays()));

                }



                try {
                    Transformer trans = TransformerFactory.newInstance().newTransformer();
                    FileWriter fw = new FileWriter("rooms.xml");
                    trans.transform(new DOMSource(moc), new StreamResult(fw));
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

                log.info("начало создания pdf отчета");
                String data = "C:\\Users\\danil\\IdeaProjects\\laba2\\rooms.xml";

                String res = "C:\\Users\\danil\\Desktop\\ot4et.pdf";

                String tmp = "C:\\Users\\danil\\IdeaProjects\\laba2\\report1.jrxml";


                printpdfs(data, tmp, res);

                log.info("pdf отчет создан");








            }

        });
        log.info("пользовательский интерфейс был закрыт ");
    }

    private void сompletiomGuestFromBd() {
        completiomRoomsFromBd();
        List<Guest> guests = DataBaseController.getAllGuests();
        for (Guest guest : guests) {
            List<String> tmp = new ArrayList<>();
            tmp.add(Integer.toString(guest.getGuestId()));
            tmp.add(guest.getFullName());
            tmp.add(Integer.toString(guest.getAge()));
            tmp.add(Integer.toString(guest.getNumberOfRoom()));
            tmp.add(Integer.toString(guest.getDuration()));
            saveGuestToTable(tmp);
        }

    }

    private void completiomRoomsFromBd(){
        List<HotelRoom> roomList = DataBaseController.getAllHotelRooms();
        for (HotelRoom room : roomList){
            DataBaseController.setZeroDay(room.getId());
        }
    }


    private void saveGuestToTable(List<String> save) {

        Vector guestOnTable = new Vector<String>(save);
        model.addRow(guestOnTable);
        guests = new JTable(model);
        scroll = new JScrollPane(guests);

        hotelList.add(scroll, BorderLayout.CENTER);
        hotelList.setVisible(true);
    }


    private static void printpdf(String document, String template, String resultpath) {
        try {
            JRDataSource ds = new JRXmlDataSource(document, "/guestlist/guest");
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
    private static void printpdfs(String document, String template, String resultpath) {
        try {
            JRDataSource ds = new JRXmlDataSource(document, "/komnati/komnata");
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





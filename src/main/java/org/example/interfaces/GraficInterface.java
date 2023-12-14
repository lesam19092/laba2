package org.example.interfaces;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRXmlDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.view.JasperViewer;
import org.apache.log4j.Logger;
import org.example.Checks;
import org.example.DataBaseController;
import org.example.Guest;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

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
import java.io.*;
import java.util.List;
import java.util.*;

public class GraficInterface extends JFrame {
    private JFrame hotelList;
    private DefaultTableModel model;

    private JScrollPane scroll;
    private JTable guests;
    private static final Logger log = Logger.getLogger(GraficInterface.class);


    private List<Guest> listLeft = new ArrayList<>();

    private int cash;

    public void show() {


        log.info("---------------------------------------------------");
        log.info("Открытыие пользовательского интерфейса");
        hotelList = new JFrame("Список гостей");
        hotelList.setSize(1100, 800);
        hotelList.setLocation(100, 100);
        hotelList.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton save = new JButton(new ImageIcon("C:\\Users\\danil\\Desktop\\save.png"));
        JButton add = new JButton(new ImageIcon("C:\\Users\\danil\\Desktop\\add.jpg"));
        JButton redact = new JButton(new ImageIcon("C:\\Users\\danil\\Desktop\\redact.png"));
        JButton pechat = new JButton(new ImageIcon("C:\\Users\\danil\\Desktop\\pechat.png"));
        JButton delete = new JButton(new ImageIcon("C:\\Users\\danil\\Desktop\\delete.png"));
        JButton download = new JButton(new ImageIcon("C:\\Users\\danil\\Desktop\\donwload.png"));
        JButton workList = new JButton(new ImageIcon("C:\\Users\\danil\\Desktop\\работники.png"));
        JButton readXml = new JButton(new ImageIcon("C:\\Users\\danil\\Desktop\\xml.jpeg"));
        JButton createPdf = new JButton(new ImageIcon("C:\\Users\\danil\\Desktop\\pdf.jpeg"));
        JButton potok = new JButton(new ImageIcon("C:\\Users\\danil\\Desktop\\potok.jpg"));
        JButton komnat = new JButton(new ImageIcon("C:\\Users\\danil\\Desktop\\комнаты.png"));
        JButton oneday = new JButton(new ImageIcon("C:\\Users\\danil\\Desktop\\plusone.png"));
        JButton dollar = new JButton(new ImageIcon("C:\\Users\\danil\\Desktop\\dollar.png"));


        save.setToolTipText("Сохранить список гостей");
        add.setToolTipText("добавить гостя");
        redact.setToolTipText("редактировать таблицу");
        pechat.setToolTipText("распечатать таблицу");
        delete.setToolTipText("удалить гостя");
        download.setToolTipText("загрузить информацию");
        workList.setToolTipText("открыть список работников");
        readXml.setToolTipText("считать xml файл");
        createPdf.setToolTipText("создать pdf отчет");
        potok.setToolTipText("запуск всех потоков");
        komnat.setToolTipText("открыть список комнат");
        oneday.setToolTipText("забрать у всех один день");
        dollar.setToolTipText("показать стоимость за сеанс");


        JToolBar toolBar = new JToolBar("Панель инструментов");
        toolBar.add(save);
        toolBar.add(add);
        toolBar.add(redact);
        toolBar.add(pechat);
        toolBar.add(delete);
        toolBar.add(download);
        toolBar.add(workList);
        toolBar.add(readXml);
        toolBar.add(createPdf);
        toolBar.add(potok);
        toolBar.add(komnat);
        toolBar.add(oneday);
        toolBar.add(dollar);

        hotelList.setLayout(new BorderLayout());
        hotelList.add(toolBar, BorderLayout.NORTH);

        String[] colums = {"id", "Имя", "возраст", "комната", "длительность прибывания"};

        model = new DefaultTableModel(null, colums);
        guests = new JTable(model);
        scroll = new JScrollPane(guests);

        сompletiomGuestFromBd();

        hotelList.add(scroll, BorderLayout.CENTER);

        JComboBox name = new JComboBox(new String[]{"Имя", "Михаил Плюснин", "Гловацкий Даниил"});
        JTextField room = new JTextField("номер комнаты");
        JButton filter = new JButton("Поиск");
        JPanel filterPanel = new JPanel();
        filterPanel.add(name);
        filterPanel.add(room);
        filterPanel.add(filter);

        hotelList.add(filterPanel, BorderLayout.SOUTH);
        hotelList.setVisible(true);

        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                log.info("было начато сохранение таблицы в файл");
                FileDialog save = new FileDialog(hotelList, "Сохранение данных", FileDialog.SAVE);
                save.setFile("*.txt");
                save.setVisible(true);
                try {
                    if (save.getDirectory() != null && save.getFile() != null) {
                        String fileName = save.getDirectory() + save.getFile();
                        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
                        for (int i = 0; i < model.getRowCount(); i++) {
                            for (int j = 0; j < model.getColumnCount(); j++) {
                                writer.write(model.getValueAt(i, j) + " ");
                            }
                            writer.write("\n");
                        }
                        log.info(String.format("таблица была добавлена в %s файл", fileName));
                        JOptionPane.showMessageDialog(hotelList, "сохранено");
                        writer.close();
                    }

                } catch (IOException e) {
                    log.warn("ошибка при записи в файл", e);
                    e.printStackTrace();
                    return;
                }
            }

        });
        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                log.info("было начато сохранение гостя вручную");

                String name = JOptionPane.showInputDialog(
                        hotelList,
                        "Введите имя проживающего",
                        "авторизация",
                        JOptionPane.QUESTION_MESSAGE);
                boolean flag = verifyName(name);
                if (!flag) return;
                String tmp_Age = JOptionPane.showInputDialog(
                        hotelList,
                        "Введите возраст проживающего",
                        "авторизация",
                        JOptionPane.QUESTION_MESSAGE);
                boolean flag1 = verifyAge(tmp_Age);
                if (!flag1) return;
                String tmp_room = JOptionPane.showInputDialog(
                        hotelList,
                        "Введите комнату проживающего",
                        "авторизация",
                        JOptionPane.QUESTION_MESSAGE);
                boolean flag2 = verifyRoom(tmp_room);
                if (!flag2) return;
                String tmp_duration = JOptionPane.showInputDialog(
                        hotelList,
                        "Введите длительность пребывания",
                        "авторизация",
                        JOptionPane.QUESTION_MESSAGE);
                boolean flag3 = verifyDuration(tmp_duration);
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


                    saveToTable(save);
                    log.info(String.format("Был добавлен гость с именем : %s , возраст : %s , комната : %s", name, tmp_Age, tmp_room));
                } else JOptionPane.showMessageDialog(hotelList, "комната переполена, попробуйте другую");
            }
        });
        workList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new WorkInterface().show();
            }
        });
        download.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int count = 0;
                log.info("была начата загрузка данных с файла");
                FileDialog save = new FileDialog(hotelList, "Добавление данных", FileDialog.SAVE);
                save.setFile("*.txt");
                save.setVisible(true);
                String fileName = save.getDirectory() + save.getFile();
                if (fileName.equals("nullnull")) {
                    log.warn("не был выбран файл пользователем для загрузки информации");
                    JOptionPane.showMessageDialog(hotelList, "вы не выбрали файл");
                    return;
                }

                try {

                    BufferedReader reader = new BufferedReader(new FileReader(fileName));
                    int rows = model.getRowCount();
                    String guest;
                    do {
                        guest = reader.readLine();
                        if (guest != null) {
                            String[] splitGuest = guest.split(" ");
                            if (splitGuest.length == 5) {
                                List<String> tmp = new ArrayList<>();
                                tmp.add(splitGuest[0] + " " + splitGuest[1]);
                                tmp.add(splitGuest[2]);
                                tmp.add(splitGuest[3]);
                                tmp.add(splitGuest[4]);
                                String fullName = null;
                                int age = 0;
                                int room = 0;
                                int duration = 0;
                                try {
                                    fullName = tmp.get(0);
                                    Checks.checkName(fullName);
                                } catch (Checks.MyException exception) {
                                    log.warn("введено некорректное имя", exception);
                                    JOptionPane.showMessageDialog(hotelList, exception.getMessage());
                                    return;
                                } catch (NullPointerException ex) {
                                    log.warn("введена пустая строчка", ex);
                                    JOptionPane.showMessageDialog(hotelList, ex.toString());
                                    return;
                                }
                                try {
                                    age = Integer.parseInt(tmp.get(1));
                                    Checks.checkAge(age);
                                } catch (Checks.MyException exception) {
                                    log.warn("введен некорректный возраст", exception);
                                    JOptionPane.showMessageDialog(hotelList, exception.getMessage());
                                    return;
                                } catch (NumberFormatException ex) {
                                    log.warn("введен некорректный возраст", ex);
                                    JOptionPane.showMessageDialog(hotelList, "некорректный возраст в файле");
                                    return;
                                }
                                try {
                                    room = Integer.parseInt(tmp.get(2));
                                    Checks.checkRoom(room);
                                } catch (Checks.MyException exception) {
                                    log.warn("введена некорректная комната ", exception);
                                    JOptionPane.showMessageDialog(hotelList, exception.getMessage());
                                    return;
                                } catch (NumberFormatException ex) {
                                    log.warn("введена некорректная комната", ex);
                                    JOptionPane.showMessageDialog(hotelList, "некорректная комната в файле");
                                    return;
                                }
                                try {
                                    duration = Integer.parseInt(tmp.get(3));
                                    Checks.checkDuration(room);
                                } catch (Checks.MyException exception) {
                                    log.warn("введена некорректная длительность ", exception);
                                    JOptionPane.showMessageDialog(hotelList, exception.getMessage());
                                    return;
                                } catch (NumberFormatException ex) {
                                    log.warn("введена некорректная комната", ex);
                                    JOptionPane.showMessageDialog(hotelList, "некорректная длительность в файле");
                                    return;
                                }

                                Guest guest1 = new Guest();
                                guest1.setFullName(fullName);
                                guest1.setAge(age);
                                guest1.setNumberOfRoom(room);
                                guest1.setDuration(duration);
                                if (DataBaseController.saveGuestToDB(guest1) == true) {
                                    tmp.add(0, Integer.toString(guest1.getGuestId()));
                                    count++;
                                    saveToTable(tmp);
                                    log.info(String.format("Был добавлен гость с id : %s , именем : %s , возраст : %s , комната : %s , длительностью пребывания : %s ", tmp.get(0), tmp.get(1), tmp.get(2), tmp.get(3), tmp.get(4)));
                                } else
                                    JOptionPane.showMessageDialog(hotelList, "комната переполена, попробуйте другую");
                            } else if (splitGuest.length == 4) {
                                List<String> tmp = new ArrayList<>();

                                String fullName = null;
                                int age = 0;
                                int room = 0;
                                String type = null;
                                int duration = 0;
                                try {
                                    fullName = splitGuest[0];
                                    Checks.checkName(fullName);
                                    tmp.add(fullName);
                                } catch (Checks.MyException exception) {
                                    log.warn("введено некорректное имя", exception);
                                    JOptionPane.showMessageDialog(hotelList, exception.getMessage());
                                    return;
                                } catch (NullPointerException ex) {
                                    log.warn("введена пустая строчка", ex);
                                    JOptionPane.showMessageDialog(hotelList, ex.toString());
                                    return;
                                }
                                try {
                                    age = Integer.parseInt(splitGuest[1]);
                                    Checks.checkAge(age);
                                    tmp.add(splitGuest[1]);
                                } catch (Checks.MyException exception) {
                                    log.warn("введен некорректный возраст", exception);
                                    JOptionPane.showMessageDialog(hotelList, exception.getMessage());
                                    return;
                                } catch (NumberFormatException ex) {
                                    log.warn("введен некорректный возраст", ex);
                                    JOptionPane.showMessageDialog(hotelList, "некорректный возраст в файле");
                                    return;
                                }
                                try {
                                    room = Integer.parseInt(splitGuest[2]);
                                    Checks.checkRoom(room);
                                    tmp.add(splitGuest[2]);
                                } catch (Checks.MyException exception) {
                                    log.warn("введена некорректная комната ", exception);
                                    JOptionPane.showMessageDialog(hotelList, exception.getMessage());
                                    return;
                                } catch (NumberFormatException ex) {
                                    log.warn("введена некорректная комната", ex);
                                    JOptionPane.showMessageDialog(hotelList, "некорректная комната в файле");
                                    return;
                                }
                                try {
                                    duration = Integer.parseInt(splitGuest[3]);
                                    Checks.checkDuration(duration);
                                    tmp.add(splitGuest[3]);
                                } catch (Checks.MyException exception) {
                                    log.warn("введена некорректная длительность ", exception);
                                    JOptionPane.showMessageDialog(hotelList, exception.getMessage());
                                    return;
                                } catch (NumberFormatException ex) {
                                    log.warn("введена некорректная длительность", ex);
                                    JOptionPane.showMessageDialog(hotelList, "некорректная длительность в файле");
                                    return;
                                }

                                Guest guest1 = new Guest();
                                guest1.setFullName(fullName);
                                guest1.setAge(age);
                                guest1.setNumberOfRoom(room);
                                guest1.setDuration(duration);
                                if (DataBaseController.saveGuestToDB(guest1) == true) {
                                    tmp.add(0, Integer.toString(guest1.getGuestId()));


                                    saveToTable(tmp);
                                    count++;
                                    log.info(String.format("Был добавлен гость с id : %s , именем : %s , возраст : %s , комната : %s , длительность пребывания : %s ", tmp.get(0), tmp.get(1), tmp.get(2), tmp.get(3), tmp.get(4)));
                                } else
                                    JOptionPane.showMessageDialog(hotelList, "комната переполена, попробуйте другую");
                            }
                        }
                    } while (guest != null);
                    reader.close();
                    if (count == 1) log.info(String.format("было добавлена 1 гость"));
                    else log.info(String.format("было добавлено %d гостя", count));
                } catch (FileNotFoundException exception) {
                    log.warn("не найден файл ", exception);
                    exception.printStackTrace();
                    return;
                } catch (IOException exception) {
                    log.warn("ошибка при считывании файла", exception);
                    exception.printStackTrace();
                    return;
                }

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
                    guest.setAttribute("typeOfRoom", (String) model.getValueAt(i, 4));

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
        readXml.addActionListener(new ActionListener() {
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
                    model.addRow(new String[]{id, name, age, room});
                }
                log.info("xml файл был считан ");
            }
        });
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
        potok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                log.info("начало выполнение многопоточки");

                Thread donwloadFromXml = new Thread(new Runnable() {
                    public void run() {
                        log.info("начало считывания xml файла в потоке");
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
                            model.addRow(new String[]{id, name, age, room});
                        }
                        log.info("xml файл был считан в потоке");

                    }
                });
                donwloadFromXml.start();
                try {
                    donwloadFromXml.join();
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
                Thread donwloadToXml = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        log.info("начало создания xml файла в потоке");
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
                            guest.setAttribute("typeOfRoom", (String) model.getValueAt(i, 4));

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

                        log.info("xml файл создан в потоке");

                    }
                });
                donwloadToXml.start();
                try {
                    donwloadToXml.join();
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }

                Thread createPdf = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        log.info("начало создания pdf отчета в потоке");
                        String data = "C:\\Users\\danil\\IdeaProjects\\laba2\\laba.xml";

                        String res = "C:\\Users\\danil\\Desktop\\res.pdf";

                        String tmp = "C:\\Users\\danil\\IdeaProjects\\laba2\\pdf.jrxml";


                        printpdf(data, tmp, res);

                        log.info("pdf отчет создан в потоке");
                    }
                });
                createPdf.start();
                log.info("конец выполнение многопоточки");
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
                    Checks.checkId(id);
                } catch (NumberFormatException exx) {
                    log.warn("введен некорректный id", new NumberFormatException());
                    JOptionPane.showMessageDialog(hotelList, "ввод некорректных данных для int");
                    return;
                } catch (Checks.MyException exception) {
                    log.warn("введен некорректный id", exception);
                    JOptionPane.showMessageDialog(hotelList, exception.getMessage());
                    return;
                } catch (InputMismatchException exp) {
                    log.warn("введен некорректный возраст", exp);
                    JOptionPane.showMessageDialog(hotelList, exp.toString());
                    return;
                }
                DataBaseController.deleteById(id);


                for (int i = 0; i < model.getRowCount(); i++) {
                    if (Integer.parseInt(model.getValueAt(i, 0).toString()) == id) {
                        model.removeRow(i);
                    }
                }
                log.info("пользователь был удален из таблички");
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
                int id = 0;
                try {
                    id = Integer.parseInt(tmp_id);
                    Checks.checkId(id);
                } catch (NumberFormatException exx) {
                    log.warn("введен некорректный id", new NumberFormatException());
                    JOptionPane.showMessageDialog(hotelList, "ввод некорректных данных для int");
                    return;
                } catch (Checks.MyException exception) {
                    log.warn("введен некорректный id", exception);
                    JOptionPane.showMessageDialog(hotelList, exception.getMessage());
                    return;
                } catch (InputMismatchException exp) {
                    log.warn("введен некорректный id", exp);
                    JOptionPane.showMessageDialog(hotelList, exp.toString());
                    return;
                }


                String name = JOptionPane.showInputDialog(
                        hotelList,
                        "Введите имя проживающего",
                        "редактирование",
                        JOptionPane.QUESTION_MESSAGE);

                try {
                    Checks.checkName(name);
                } catch (Checks.MyException exception) {
                    log.warn("введено некорректное имя", exception);
                    JOptionPane.showMessageDialog(hotelList, exception.getMessage());
                    return;
                } catch (NullPointerException ex) {
                    log.warn("введено некорректное имя", ex);
                    JOptionPane.showMessageDialog(hotelList, "имя не может быть пустым");
                    return;
                }
                String tmp_Age = JOptionPane.showInputDialog(
                        hotelList,
                        "Введите возраст проживающего",
                        "редактирование",
                        JOptionPane.QUESTION_MESSAGE);

                int age = 0;
                try {
                    age = Integer.parseInt(tmp_Age);
                    Checks.checkAge(age);
                } catch (NumberFormatException exx) {
                    log.warn("введен некорректный возраст", new NumberFormatException());
                    JOptionPane.showMessageDialog(hotelList, "ввод некорректных данных для int");
                    return;
                } catch (Checks.MyException exception) {
                    log.warn("введен некорректный возраст", exception);
                    JOptionPane.showMessageDialog(hotelList, exception.getMessage());
                    return;
                } catch (InputMismatchException exp) {
                    log.warn("введен некорректный возраст", exp);
                    JOptionPane.showMessageDialog(hotelList, exp.toString());
                    return;
                }


                String tmp_room = JOptionPane.showInputDialog(
                        hotelList,
                        "Введите комнату проживающего",
                        "редактирование",
                        JOptionPane.QUESTION_MESSAGE);


                int room = 0;
                try {
                    room = Integer.parseInt(tmp_room);
                    Checks.checkRoom(room);
                } catch (NumberFormatException exx) {
                    log.warn("введен некорректный возраст", new NumberFormatException());
                    JOptionPane.showMessageDialog(hotelList, "ввод некорректных данных для int");
                    return;
                } catch (Checks.MyException exception) {
                    log.warn("введен некорректный возраст", exception);
                    JOptionPane.showMessageDialog(hotelList, exception.getMessage());
                    return;
                } catch (InputMismatchException exp) {
                    log.warn("введен некорректный возраст", exp);
                    JOptionPane.showMessageDialog(hotelList, exp.toString());
                    return;
                }







                boolean flag = DataBaseController.redactById(id, name, age, room);

                Guest guest_tmp = DataBaseController.getGuest(id);


                if (flag == true) {

                    for (int i = 0; i < model.getRowCount(); i++) {
                        if (Integer.parseInt(model.getValueAt(i, 0).toString()) == id) {
                            model.setValueAt(guest_tmp.getFullName(), i, 1);
                            model.setValueAt(guest_tmp.getAge(), i, 2);
                            model.setValueAt(guest_tmp.getNumberOfRoom(), i, 3);
                        }
                    }

                    JOptionPane.showMessageDialog(hotelList, "данные были редактированы");
                    return;
                }

                JOptionPane.showMessageDialog(hotelList, "данные не были редактированы");
            }

        });
        komnat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new RoomInterface().show();
            }
        });
        oneday.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
                        listLeft.add(guest);
                        list.add(Integer.parseInt(model.getValueAt(i, 0).toString()));
                    } else model.setValueAt(duration - 1, i, 4);
                }

                for (int j = 0; j < list.size(); j++) {
                    for (int i = 0; i < model.getRowCount(); i++) {
                        if (Integer.parseInt(model.getValueAt(i, 0).toString()) == list.get(j)) {
                            model.removeRow(i);
                        }
                    }
                }

            }


        });
        dollar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(hotelList, cash);
            }
        });
        log.info("пользовательский интерфейс был закрыт ");
    }

    private void сompletiomGuestFromBd() {
        List<Guest> list = DataBaseController.getAllGuests();
        for (Guest guest : list) {
            List<String> tmp = new ArrayList<>();
            tmp.add(Integer.toString(guest.getGuestId()));
            tmp.add(guest.getFullName());
            tmp.add(Integer.toString(guest.getAge()));
            tmp.add(Integer.toString(guest.getNumberOfRoom()));
            tmp.add(Integer.toString(guest.getDuration()));

            saveToTable(tmp);
        }

    }

    private boolean verifyName(String name) {

        try {
            Checks.checkName(name);
        } catch (Checks.MyException exception) {
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

    private boolean verifyAge(String tmp_Age) {
        int age = 0;
        try {
            age = Integer.parseInt(tmp_Age);
            Checks.checkAge(age);
        } catch (NumberFormatException exx) {
            log.warn("введен некорректный возраст", new NumberFormatException());
            JOptionPane.showMessageDialog(hotelList, "ввод некорректных данных для int");
            return false;
        } catch (Checks.MyException exception) {
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

    private boolean verifyRoom(String tmp_room) {
        int room = 0;
        try {
            room = Integer.parseInt(tmp_room);
            Checks.checkRoom(room);
        } catch (NumberFormatException exx) {
            log.warn("введен некорректный возраст", new NumberFormatException());
            JOptionPane.showMessageDialog(hotelList, "ввод некорректных данных для int");
            return false;
        } catch (Checks.MyException exception) {
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

    private boolean verifyDuration(String tmp_duration) {
        int duration = 0;
        try {
            duration = Integer.parseInt(tmp_duration);
            Checks.checkDuration(duration);
        } catch (NumberFormatException exx) {
            log.warn("введен некорректный возраст", new NumberFormatException());
            JOptionPane.showMessageDialog(hotelList, "ввод некорректных данных для int");
            return false;
        } catch (Checks.MyException exception) {
            log.warn("введена некорректная длительность", exception);
            JOptionPane.showMessageDialog(hotelList, exception.getMessage());
            return false;
        } catch (InputMismatchException exp) {
            log.warn("введена некорректная длительность возраст", exp);
            JOptionPane.showMessageDialog(hotelList, exp.toString());
            return false;
        }
        return true;
    }

    private void saveToTable(List<String> save){

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


}





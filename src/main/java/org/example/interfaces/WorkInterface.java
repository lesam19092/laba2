package org.example.interfaces;

import org.apache.log4j.Logger;
import org.example.Controllers.ChecksController;
import org.example.Controllers.DataBaseController;
import org.example.Controllers.VerifyContoller;
import org.example.Entities.Worker;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Vector;

public class WorkInterface extends JFrame {
    private JFrame worklist;
    private DefaultTableModel model;

    private VerifyContoller verify = new VerifyContoller();


    private JScrollPane scroll;
    private JTable guests;
    private static final Logger log = Logger.getLogger(HotelInterface.class);

    public void show() {


        log.info("---------------------------------------------------");

        log.info("Открытыие пользовательского интерфейса");
        worklist = new JFrame("Список работников");
        worklist.setSize(1100, 800);
        worklist.setLocation(100, 100);

        JButton add = new JButton(new ImageIcon("C:\\Users\\danil\\Desktop\\add.jpg"));
        JButton redact = new JButton(new ImageIcon("C:\\Users\\danil\\Desktop\\redact.png"));
        JButton delete = new JButton(new ImageIcon("C:\\Users\\danil\\Desktop\\delete.png"));


        add.setToolTipText("добавить работника");
        redact.setToolTipText("редактировать таблицу");
        delete.setToolTipText("удалить работника");


        JToolBar toolBar = new JToolBar("Панель инструментов");
        toolBar.add(add);
        toolBar.add(redact);
        toolBar.add(delete);

        worklist.setLayout(new BorderLayout());
        worklist.add(toolBar, BorderLayout.NORTH);

        String[] colums = {"id", "возраст", "Имя", "профессия"};

        model = new DefaultTableModel(null, colums);
        guests = new JTable(model);
        scroll = new JScrollPane(guests);

        сompletiomWorkFromBd();

        worklist.add(scroll, BorderLayout.CENTER);
        worklist.setVisible(true);


        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                log.info("было начато сохранение работника вручную");


                String tmp_Age = JOptionPane.showInputDialog(
                        worklist,
                        "Введите возраст работника",
                        "авторизация",
                        JOptionPane.QUESTION_MESSAGE);
                boolean flag = verify.verifyAge(worklist, tmp_Age);
                if (!flag) return;


                String name = JOptionPane.showInputDialog(
                        worklist,
                        "Введите имя работника",
                        "авторизация",
                        JOptionPane.QUESTION_MESSAGE);
                boolean flag1 = verify.verifyName(worklist, name);
                if (!flag1) return;

                String profession = JOptionPane.showInputDialog(
                        worklist,
                        "Введите профессию работника",
                        "авторизация",
                        JOptionPane.QUESTION_MESSAGE);
                boolean flag2 = verify.verifyProfession(worklist, profession);
                if (!flag2) return;


                Worker worker = new Worker();


                worker.setAge(Integer.parseInt(tmp_Age));
                worker.setFullName(name);
                worker.setProfession(profession);

                DataBaseController.saveWorkerToDB(worker);


                List<String> save = new ArrayList<>();
                save.add(Integer.toString(worker.getId()));
                save.add(Integer.toString(worker.getAge()));
                save.add(worker.getFullName());
                save.add(worker.getProfession());


                saveWorkerToTable(save);
                log.info(String.format("Был добавлен работник с именем : %s , возраст : %s , профессией : %s", name, tmp_Age, profession));


            }
        });
        redact.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String tmp_id = JOptionPane.showInputDialog(
                        worklist,
                        "Введите id работающего",
                        "редактирование",
                        JOptionPane.QUESTION_MESSAGE);
                boolean flag5 = verify.verifyWorkerId(worklist, tmp_id);
                if (!flag5) return;


                String tmp_Age = JOptionPane.showInputDialog(
                        worklist,
                        "Введите возраст проживающего",
                        "редактирование",
                        JOptionPane.QUESTION_MESSAGE);
                boolean flag2 = verify.verifyAge(worklist, tmp_Age);
                if (!flag2) return;

                String name = JOptionPane.showInputDialog(
                        worklist,
                        "Введите имя работающего",
                        "редактинование",
                        JOptionPane.QUESTION_MESSAGE);
                boolean flag1 = verify.verifyName(worklist, name);
                if (!flag1) return;


                String profession = JOptionPane.showInputDialog(
                        worklist,
                        "Введите профессию работающего",
                        "редактинование",
                        JOptionPane.QUESTION_MESSAGE);
                boolean flag3 = verify.verifyProfession(worklist, profession);
                if (!flag3) return;


                DataBaseController.redactWorkerById(Integer.parseInt(tmp_id), Integer.parseInt(tmp_Age), name, profession);

                Worker worker_tmp = DataBaseController.getWorker(Integer.parseInt(tmp_id));


                for (int i = 0; i < model.getRowCount(); i++) {
                    if (Integer.parseInt(model.getValueAt(i, 0).toString()) == Integer.parseInt(tmp_id)) {
                        model.setValueAt(worker_tmp.getAge(), i, 1);
                        model.setValueAt(worker_tmp.getFullName(), i, 2);
                        model.setValueAt(worker_tmp.getProfession(), i, 3);
                    }
                }

                JOptionPane.showMessageDialog(worklist, "данные были редактированы");


            }
        });
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                log.info("было начато удаление работника по id");

                String tmp_id = JOptionPane.showInputDialog(
                        worklist,
                        "Введите id работника",
                        "удаление",
                        JOptionPane.QUESTION_MESSAGE);
                int id = 0;
                try {
                    id = Integer.parseInt(tmp_id);
                    ChecksController.checkId(id);
                } catch (NumberFormatException exx) {
                    log.warn("введен некорректный id", new NumberFormatException());
                    JOptionPane.showMessageDialog(worklist, "ввод некорректных данных для int");
                    return;
                } catch (ChecksController.MyException exception) {
                    log.warn("введен некорректный id", exception);
                    JOptionPane.showMessageDialog(worklist, exception.getMessage());
                    return;
                } catch (InputMismatchException exp) {
                    log.warn("введен некорректный возраст", exp);
                    JOptionPane.showMessageDialog(worklist, exp.toString());
                    return;
                }

                if (DataBaseController.getWorker(id) != null) {


                    String s_kapcha = verify.kapcha();

                    String tmp_kapcha = JOptionPane.showInputDialog(
                            worklist,
                            "Введите капчу " + s_kapcha + " для подтверждения удаления",
                            "удаление",
                            JOptionPane.QUESTION_MESSAGE);

                    if (tmp_kapcha != null && s_kapcha.equals(tmp_kapcha)) {


                        DataBaseController.deleteWorkerById(id);


                        for (int i = 0; i < model.getRowCount(); i++) {
                            if (Integer.parseInt(model.getValueAt(i, 0).toString()) == id) {
                                model.removeRow(i);
                            }
                        }
                        JOptionPane.showMessageDialog(worklist, "работник удален");
                        log.info("пользователь был удален из таблички");
                    } else {
                        JOptionPane.showMessageDialog(worklist, "капча была введена неверно");
                    }
                } else {
                    JOptionPane.showMessageDialog(worklist, "работник с данным Id не был найден");
                }


            }

        });


    }

    private void сompletiomWorkFromBd() {
        List<Worker> workers = DataBaseController.getAllWorker();
        for (Worker worker : workers) {
            List<String> tmp = new ArrayList<>();

            tmp.add(Integer.toString(worker.getId()));
            tmp.add(Integer.toString(worker.getAge()));
            tmp.add(worker.getFullName());
            tmp.add(worker.getProfession());

            saveWorkerToTable(tmp);
        }

    }

    private void saveWorkerToTable(List<String> save) {

        Vector workerOnTable = new Vector<String>(save);
        model.addRow(workerOnTable);
        guests = new JTable(model);
        scroll = new JScrollPane(guests);

        worklist.add(scroll, BorderLayout.CENTER);
        worklist.setVisible(true);
    }


}

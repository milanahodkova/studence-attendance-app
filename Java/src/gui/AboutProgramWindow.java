package gui;

import utils.ImageScaller;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * JFrame class
 * Displays essential information about program
 *
 * @author M.O. Hodkova
 * @version 1.0
 */
public class AboutProgramWindow extends JFrame {
    private JPanel pnlMain;
    private JPanel pnlTop;
    private JPanel pnlMiddle;
    private JPanel pnlBottom;
    private Dimension dialogSize = new Dimension(720, 330);

    /**
     * Constructor for creating an object
     * @param mainWindow parent window (class GUI.MainWindow)
     */
    public AboutProgramWindow(MainWindow mainWindow){
        super("О программе");

        pnlMain = new JPanel(new BorderLayout(10, 10));
        pnlMain.add(createTopPanel(), BorderLayout.NORTH);
        pnlMain.add(createMiddlePanel(), BorderLayout.CENTER);
        pnlMain.add(createBottomPanel(), BorderLayout.SOUTH);

        setupDialog();
    }

    /**
     * Sets main settings to dialog about application
     */
    private void setupDialog() {
        setLayout(new BorderLayout());
        setSize(dialogSize);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        add(pnlMain);
        setLocationRelativeTo(null);
        pack();
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                enableFrame(false);
            }
        });
    }

    /**
     * enables or disable JFrame instance
     * @param active true - show, false - hide
     */
    public void enableFrame(boolean active) {
        if (active) {
            setSize(dialogSize);
            setLocationRelativeTo(null);
        }
        setVisible(active);
    }

    /**
     * creates necessary UI elements for top panel
     * @return created JPanel instance
     */
    private JPanel createTopPanel(){
        pnlTop = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel lblProjectName = new JLabel(
                "Приложение \"Посещаемость " +
                        "лекционных занятий\"");
        lblProjectName.setFont(new Font("Arial", Font.BOLD, 16));
        pnlTop.add(lblProjectName);
        return pnlTop;
    }

    /**
     * creates necessary UI elements for middle panel
     * @return created JPanel instance
     */
    private JPanel createMiddlePanel(){
        Image imgPhoto = ImageScaller.scaleImage(
                "/resources/images/attendance.png",
                new Dimension(170, 170),
                Image.SCALE_SMOOTH);

        JLabel lblProgramPreview = new JLabel(new ImageIcon(imgPhoto));
        lblProgramPreview.setBorder(
                BorderFactory.createBevelBorder(BevelBorder.LOWERED));

        JPanel pnlPreview = new JPanel(new BorderLayout());
        pnlPreview.add(lblProgramPreview);

        JPanel pnlProgramInfo = new JPanel(new BorderLayout(0,0));
        JTextArea txtAreaProgramInfo = new JTextArea(15, 1);
        txtAreaProgramInfo.setEditable(false);
        txtAreaProgramInfo.setLineWrap(true);
        txtAreaProgramInfo.setFont(new Font("Arial", Font.PLAIN, 14));
        txtAreaProgramInfo.setForeground(Color.black);
        txtAreaProgramInfo.setBorder(
                BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        txtAreaProgramInfo.setBackground(new Color(238, 238, 238));

        txtAreaProgramInfo.setText("Программа позволяет:\n" +
                "   -Вести учет посещаемости лекционных занятий\n" +
                "   -Добавлять/удалять даты лекционных занятий\n" +
                "   -Выводить список студентов, посещавших\n" +
                "занятия по заданной дате\n" +
                "   -Добавлять/удалять группы\n" +
                "   -Добавлять/удалять студентов\n" +
                "   -Сортировать по количеству посещений и по фамилии\n" +
                "   -Формировать отчет пропусков\n" +
                "   -Выводить на печать");

        pnlProgramInfo.add(txtAreaProgramInfo, BorderLayout.CENTER);
        pnlProgramInfo.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        pnlPreview.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        pnlMiddle = new JPanel(new BorderLayout(10, 10));
        pnlMiddle.add(pnlPreview, BorderLayout.WEST);
        pnlMiddle.add(pnlProgramInfo, BorderLayout.CENTER);

        return pnlMiddle;
    }

    /**
     * creates necessary UI elements for bottom panel
     * @return created JPanel instance
     */
    private JPanel createBottomPanel(){
        pnlBottom = new JPanel(new BorderLayout());

        JPanel pnlAppVersion = new JPanel(new FlowLayout(FlowLayout.CENTER,10,10));

        JLabel lblAppVersion = new JLabel("Версия " +
                Application.VERSION, SwingConstants.CENTER);
        lblAppVersion.setFont(new Font("Arial", Font.PLAIN, 14));
        lblAppVersion.setPreferredSize(new Dimension(150,25));
        lblAppVersion.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        pnlAppVersion.add(lblAppVersion);

        JPanel pnlExit = new JPanel(new FlowLayout(FlowLayout.CENTER,10,10));
        JButton btnExit = new JButton("Назад");
        btnExit.setFont(new Font("Arial", Font.BOLD, 12));
        btnExit.setPreferredSize(new Dimension(100,25));
        btnExit.addActionListener(new ExitDialogListener(this));
        pnlExit.add(btnExit);

        pnlBottom.add(pnlExit, BorderLayout.EAST);
        pnlBottom.add(pnlAppVersion, BorderLayout.WEST);
        return pnlBottom;
    }

    /**
     * Add exit dialog button listener
     */
    class ExitDialogListener implements ActionListener {
        private JFrame frame;
        public ExitDialogListener(JFrame frame){
            this.frame = frame;
        }

        public void actionPerformed(ActionEvent e) {
            frame.setVisible(false);
        }
    }
}
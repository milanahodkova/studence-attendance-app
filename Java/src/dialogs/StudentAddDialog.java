package dialogs;

import database.DatabaseManager;
import entities.Student;
import gui.Application;
import gui.ComboBoxes;
import gui.MainWindow;
import gui.Tables;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * JDialog class used to input and add Student.
 *
 * @author Hodkova M.O.
 * @version 1.0
 */
public class StudentAddDialog extends JDialog {
    private JPanel pnlMain;
    private MainWindow mainWindow;
    private JPanel pnlMiddle;
    private JPanel pnlBottom;
    private JTextField txtStudent = new JTextField();
    private Dimension dialogSize = new Dimension(400, 140);

    /**
     * Creates a new instance of StudentAddDialog.
     *
     * @param mainWindow the parent window
     */
    public StudentAddDialog(MainWindow mainWindow) {
        super(mainWindow, "Добавить студента");
        this.mainWindow = mainWindow;
        pnlMain = new JPanel(new BorderLayout(10, 10));

        pnlMain.add(createMiddlePanel(), BorderLayout.CENTER);
        pnlMain.add(createBottomPanel(), BorderLayout.SOUTH);

        setUpDialog();
    }

    /**
     * Sets up UI necessary settings for student add dialog.
     */
    private void setUpDialog() {
        setLayout(new BorderLayout());
        setSize(dialogSize);
        setResizable(false);
        setModalityType(ModalityType.APPLICATION_MODAL);
        add(pnlMain);
        setLocationRelativeTo(getOwner());
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                enableFrame(false);
            }
        });
    }

    /**
     * Enables or disables the dialog instance.
     *
     * @param active true to enable, false to disable
     */
    public void enableFrame(boolean active) {
        if (active) {
            setSize(dialogSize);
            setLocationRelativeTo(getOwner());
        } else {
            txtStudent.setText("");
        }

        setVisible(active);
    }

    /**
     * creates necessary UI objects for bottom panel
     */
    private JPanel createBottomPanel() {
        pnlBottom = new JPanel(new BorderLayout());
        Dimension btnPreferredSize = new Dimension(110, 30);

        JPanel pnlAddGroup = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnAddGroup = new JButton("Добавить");
        btnAddGroup.setFont(new Font("Arial", Font.BOLD, 12));
        btnAddGroup.setBackground(Application.MAIN_COLOR);
        btnAddGroup.setPreferredSize(btnPreferredSize);
        pnlAddGroup.add(btnAddGroup);
        btnAddGroup.addActionListener(new AddStudentClickListener(this));

        JPanel pnlCancel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnCancel = new JButton("Отмена");
        btnCancel.setFont(new Font("Arial", Font.BOLD, 12));
        btnCancel.setBackground(Application.MAIN_COLOR);
        btnCancel.setPreferredSize(btnPreferredSize);
        btnCancel.addActionListener(new CancelDialogListener(this));
        pnlCancel.add(btnCancel);

        pnlBottom.add(pnlAddGroup, BorderLayout.WEST);
        pnlBottom.add(pnlCancel, BorderLayout.EAST);
        return pnlBottom;
    }

    /**
     * Creates necessary UI objects for middle panel.
     */
    private JPanel createMiddlePanel() {
        pnlMiddle = new JPanel(new BorderLayout(10, 10));
        JPanel pnlFieldHolder = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel lblAddGroup = new JLabel("Введите ФИО: ");
        lblAddGroup.setFont(new Font("Arial", Font.BOLD, 12));
        txtStudent.setPreferredSize(new Dimension(250, 25));
        txtStudent.setFont(new Font("Arial", Font.BOLD, 14));
        pnlFieldHolder.setBorder(new EmptyBorder(15, 10, 0, 10));
        pnlFieldHolder.add(lblAddGroup);
        pnlFieldHolder.add(txtStudent);
        pnlMiddle.add(pnlFieldHolder, BorderLayout.CENTER);
        return pnlMiddle;
    }

    /**
     * Listener for the Cancel button.
     */
    class CancelDialogListener implements ActionListener {
        private JDialog dialog;

        public CancelDialogListener(JDialog dialog) {
            this.dialog = dialog;
        }

        public void actionPerformed(ActionEvent e) {
            txtStudent.setText("");
            dialog.setVisible(false);
        }
    }

    /**
     * Listener for the Add button.
     */
    class AddStudentClickListener implements ActionListener {
        private JDialog dialog;

        public AddStudentClickListener(JDialog dialog) {
            this.dialog = dialog;
        }

        public void actionPerformed(ActionEvent e) {
            if (txtStudent.getText().equals("")) {
                JOptionPane.showMessageDialog(null,
                        "Заполните данные!",
                        "Ошибка операции", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                for (Student item : DatabaseManager.getStudentsFromGroup(DatabaseManager.getGroups()
                        .get(ComboBoxes.getComboBoxGroups().getSelectedIndex()).getGroupNumber())) {
                    {
                        if (item.getFIO().equals(txtStudent.getText())) {
                            JOptionPane.showMessageDialog(null,
                                    "Такой студент уже добавлен!",
                                    "Ошибка операции", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }
                }

                Student newStudent = new Student(txtStudent.getText(), DatabaseManager.getGroups()
                        .get(ComboBoxes.getComboBoxGroups().getSelectedIndex()), "");

                DatabaseManager.addStudent(newStudent);
                Tables.refreshStudents();
                Tables.outputLecturesToTable();
                Tables.setTableAttendanceAppearance();

                JOptionPane.showMessageDialog(null, "Cтудент " + txtStudent.getText() + " добавлен!",
                        "Результат", JOptionPane.INFORMATION_MESSAGE);
                txtStudent.setText("");
                dialog.setVisible(false);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null,
                        "Ошибка операции " + " ",
                        "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}


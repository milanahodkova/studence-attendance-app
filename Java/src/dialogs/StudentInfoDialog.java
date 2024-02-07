package dialogs;

import database.DatabaseManager;
import entities.*;
import gui.ColumnStatusCellRender;
import gui.ComboBoxes;
import gui.MainWindow;
import gui.Tables;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * A dialog for displaying information about a student's attendance.
 *
 * @author M.O. Hodkova
 * @version 1.0
 */
public class StudentInfoDialog extends JDialog {
    private JPanel pnlMain;
    private MainWindow mainWindow;
    private JPanel pnlMiddle;
    private Dimension dialogSize = new Dimension(500, 475);
    private static String studentFIO;
    private Group studentGroup;
    private JComboBox jcbSubjects;
    private JTable table;
    private JLabel lblFIO;
    private JLabel lblSkippedLectures;
    private JLabel lblPercentageAttendance;
    private DefaultTableModel dataModel;
    private int selectedRow;
    private Tables tables;
    private ComboBoxes comboBoxes;


    /**
     * Creates a new StudentInfoDialog with the specified parent window.
     * @param mainWindow The parent MainWindow.
     */
    public StudentInfoDialog(MainWindow mainWindow) {
        super(mainWindow, "Карточка студента");
        this.mainWindow = mainWindow;
        pnlMain = new JPanel(new BorderLayout(10, 10));
        selectedRow = Tables.getSelectedTableStudentsRow();
        studentFIO = getStudentsDataModel().getValueAt(selectedRow,
                Tables.getSelectedTableStudentsColumn()).toString();
        studentGroup = ComboBoxes.getSelectedGroup();
        pnlMain.add(createMiddlePanel(), BorderLayout.CENTER);

        setUpDialog();
    }

    /**
     * Sets up the necessary UI settings for the StudentInfoDialog.
     */
    private void setUpDialog() {
        setLayout(new BorderLayout());
        setSize(dialogSize);
        setResizable(false);
        setModalityType(ModalityType.MODELESS);
        add(pnlMain);
        setLocationRelativeTo(getOwner());
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    setPreviousStudentInfo();
                }
                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    setNextStudentInfo();
                }
            }
        });
        setFocusable(true);
        requestFocusInWindow();
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                enableFrame(false);
            }
        });
    }

    /**
     * Enables or disables the StudentInfoDialog instance.
     * @param active Whether to enable or disable the dialog.
     */
    public void enableFrame(boolean active) {
        if (active) {
            setSize(dialogSize);
            setLocationRelativeTo(getOwner());
        }
        setVisible(active);
    }

    /**
     * Creates the JPanel for displaying the student's attendance information.
     * @return The JPanel that displays the student's attendance information.
     */
    private JPanel createMiddlePanel() {
        pnlMiddle = new JPanel(new BorderLayout(10, 10));

        JPanel pnlFieldsHolder = new JPanel(new BorderLayout());
        JPanel pnlStudentInfo = new JPanel(new GridLayout(2, 1, 10, 10));
        lblFIO = new JLabel("ФИО: " + studentFIO);
        lblFIO.setFont(new Font("Arial", Font.BOLD, 14));
        JLabel lblGroup = new JLabel("Группа: " + studentGroup.getGroupNumber());
        lblGroup.setFont(new Font("Arial", Font.BOLD, 13));
        pnlStudentInfo.add(lblFIO);
        pnlStudentInfo.add(lblGroup);

        JPanel pnlAttendanceInfo = new JPanel(new BorderLayout());
        JPanel pnlSubjectsHolder = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 10));
        ArrayList<Subject> subjects = new ArrayList<>(DatabaseManager.getSubjects());
        JLabel lblSubject = new JLabel("Предмет: ");
        lblSubject.setFont(new Font("Arial", Font.BOLD, 13));
        jcbSubjects = new JComboBox(subjects.toArray());
        jcbSubjects.setPreferredSize(new Dimension(120, 25));
        jcbSubjects.setSelectedItem(subjects.get(ComboBoxes.getComboBoxSubjects().getSelectedIndex()));
        jcbSubjects.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lblSkippedLectures.setText("Всего пропущено(ч): " + getHoursOfSkippedLectures()
                        + "/" + getHoursOfLectures());
                lblPercentageAttendance.setText("Посещаемость: " + getPercentageAttendance() + "%");
                outputStudentAttendToTable();
            }
        });
        pnlSubjectsHolder.add(lblSubject);
        pnlSubjectsHolder.add(jcbSubjects);
        pnlSubjectsHolder.setBorder(new EmptyBorder(30, 0, 0, 10));

        JPanel pnlTable = new JPanel(new BorderLayout(10, 10));
        table = new JTable();
        dataModel = (DefaultTableModel) table.getModel();
        dataModel.addColumn("Дата");
        dataModel.addColumn(" ");
        table.setFocusable(false);
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(table);
        scrollPane.setPreferredSize(new Dimension(350, 200));
        outputStudentAttendToTable();

        JPanel pnlCalcOfAttend = new JPanel(new GridLayout(2, 1, 10, 10));
        lblSkippedLectures = new JLabel("Всего пропущено(ч): " + getHoursOfSkippedLectures()
                + "/" + getHoursOfLectures());
        lblSkippedLectures.setFont(new Font("Arial", Font.BOLD, 13));
        lblPercentageAttendance = new JLabel("Посещаемость: " + getPercentageAttendance() + "%");
        lblPercentageAttendance.setFont(new Font("Arial", Font.BOLD, 13));
        pnlCalcOfAttend.add(lblSkippedLectures);
        pnlCalcOfAttend.add(lblPercentageAttendance);
        pnlTable.add(scrollPane, BorderLayout.CENTER);
        pnlTable.add(pnlCalcOfAttend, BorderLayout.SOUTH);

        pnlStudentInfo.setBorder(new EmptyBorder(20,0,0,0));
        pnlAttendanceInfo.setBorder(new EmptyBorder(0, 0, 30, 0));
        pnlFieldsHolder.setBorder(new EmptyBorder(15, 15, 15, 15));

        pnlAttendanceInfo.add(pnlSubjectsHolder, BorderLayout.NORTH);
        pnlAttendanceInfo.add(pnlTable, BorderLayout.CENTER);

        JPanel pnl = new JPanel(new BorderLayout());
        pnl.add(pnlStudentInfo,BorderLayout.NORTH);
        pnl.add(pnlAttendanceInfo,BorderLayout.CENTER);

        pnlFieldsHolder.add(pnl, BorderLayout.CENTER);

        pnlMiddle.add(pnlFieldsHolder, BorderLayout.CENTER);
        return pnlMiddle;
    }

    /**
     * Gets the DefaultTableModel for the student's data.
     * @return The DefaultTableModel for the student's data.
     */
    private DefaultTableModel getStudentsDataModel(){
        return Tables.getStudentsDataModel();
    }

    /**
     * Calculates the percentage of attendance for the student.
     * @return The percentage of attendance for the student.
     */
    private int getPercentageAttendance() {
        double hoursOfAttendedLectures = getHoursOfLectures() - getHoursOfSkippedLectures();
        double percentage = (hoursOfAttendedLectures / getHoursOfLectures()) * 100;
        return (int) percentage;
    }

    /**
     * Calculates the number of hours of skipped lectures for the student.
     * @return The number of hours of skipped lectures for the student.
     */
    private int getHoursOfSkippedLectures() {
        Student student = new Student(studentFIO, studentGroup);
        int skippedHours = DatabaseManager.getHoursOfSkippedLectures(student,
                DatabaseManager.getSubjects().get(jcbSubjects.getSelectedIndex()));
        return skippedHours;
    }

    /**
     * Calculates the number of hours of lectures for the selected subject.
     * @return The number of hours of lectures for the selected subject.
     */
    private int getHoursOfLectures() {
        int hoursOfLectures = DatabaseManager.getHoursOfLectures(DatabaseManager.getSubjects().get(jcbSubjects.getSelectedIndex()),
                studentGroup);
        return hoursOfLectures;
    }

    /**
     * Sets the information for the previous student in the table.
     */
    private void setPreviousStudentInfo() {
        if (selectedRow > 0) {
            selectedRow--;
            studentFIO = getStudentsDataModel().getValueAt(selectedRow,
                    tables.getSelectedTableStudentsColumn()).toString();
            lblFIO.setText("ФИО: " + studentFIO);
            lblSkippedLectures.setText("Всего пропущено(ч): " + getHoursOfSkippedLectures()
                    + "/" + getHoursOfLectures());
            lblPercentageAttendance.setText("Посещаемость: " + getPercentageAttendance() + "%");
            outputStudentAttendToTable();
        }
    }

    /**
     * Sets the next student's information in the dialog if there is one available.
     */
    private void setNextStudentInfo() {
        if (selectedRow < tables.getTableStudents().getRowCount() - 1) {
            selectedRow++;
            studentFIO =getStudentsDataModel().getValueAt(selectedRow,
                    tables.getSelectedTableStudentsColumn()).toString();
            lblFIO.setText("ФИО: " + studentFIO);
            lblSkippedLectures.setText("Всего пропущено(ч): " + getHoursOfSkippedLectures()
                    + "/" + getHoursOfLectures());
            lblPercentageAttendance.setText("Посещаемость: " + getPercentageAttendance() + "%");
            outputStudentAttendToTable();
        }
    }

    /**
     * Outputs the student's attendance records to the JTable.
     */
    private void outputStudentAttendToTable() {
        dataModel.setRowCount(0);
        DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        ArrayList<Lecture> existingLectures = DatabaseManager.getLecturesBySubjectAndGroup(DatabaseManager.getSubjects()
                .get(jcbSubjects.getSelectedIndex()),studentGroup);
        Collections.sort(existingLectures, new Comparator<Lecture>() {
            @Override
            public int compare(Lecture o1, Lecture o2) {
                if (o1.getDate().before(o2.getDate())) {
                    return -1;
                }
                return 1;
            }
        });
        Student student = new Student(studentFIO, studentGroup);

        if (!existingLectures.isEmpty()) {
            for (Lecture item : existingLectures) {
                String dateItem = formatter.format(item.getDate());

                StudentLectureRecord studentLectureRecord = DatabaseManager.getStudentLecture(item,
                        student);
                if(studentLectureRecord != null) {
                    dataModel.addRow(new Object[]{dateItem, studentLectureRecord.getPresence()});
                    table.getColumnModel().getColumn(1).setCellRenderer(new ColumnStatusCellRender());
                }
            }
        }
    }
}



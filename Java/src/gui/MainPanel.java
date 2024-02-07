package gui;

import com.sun.tools.javac.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class MainPanel {
    private JPanel pnlMain;
    private JPanel pnlCenter;
    private JPanel pnlBottom;
    private static JMenuItem mniDeleteGroup;
    private static JMenuItem mniAddGroup;
    private static JMenuItem mniAddSubject;
    private static JMenuItem mniDeleteSubject;
    private static JButton btnDeleteStudent;
    private static JButton btnAddStudent;
    private static JButton btnSaveToFile;
    private static JButton btnExit;
    private static JButton btnAboutProgram;

    public MainPanel(MainWindow mainWindow){
    }
    /**
     * Creates the main panels for the main window.
     */
    public void createMainPanels() {
        pnlMain = new JPanel(new BorderLayout(0, 0));
        pnlCenter = new JPanel(new BorderLayout(0, 10));
        pnlBottom = new JPanel(new BorderLayout());
        pnlBottom.setBackground(Application.MAIN_COLOR);
    }

    public void addPanelsToMainPanel(){
        pnlMain.add(pnlCenter, BorderLayout.CENTER);
        pnlMain.add(pnlBottom, BorderLayout.SOUTH);
    }
    /**
     * Creates the components for the top part of the center panel.
     */
    void createCenterUpComponents() {
        // Create a panel for the groups
        JPanel pnlGroups = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlGroups.setBackground(Application.MAIN_COLOR);

        // Add a label for selecting a group
        JLabel lblGroupSelect = new JLabel("Группа:");
        lblGroupSelect.setFont(new Font("Arial", Font.BOLD, 12));
        lblGroupSelect.setForeground(Color.black);

        ComboBoxes.createCmbGroups();
        ComboBoxes.createCmbSubjects();
  /*      // Create a drop-down list for selecting a group
        ArrayList<Group> groups = new ArrtoArray());
        cmbGroups.setFont(new Font("ArialayList<>(DatabaseManager.getGroups());
        cmbGroups = new JComboBox(groups.", Font.PLAIN, 12));
        cmbGroups.setPreferredSize(new Dimension(120, 25));
        cmbGroups.setToolTipText("Выбрать группу");*/

        // Create a popup menu for the group
        JPopupMenu pmnGroup = new JPopupMenu();
        mniAddGroup = new JMenuItem("Добавить");
        mniDeleteGroup = new JMenuItem("Удалить");
        ComboBoxes.setupContextMenuForComboBox(ComboBoxes.getComboBoxGroups(),pmnGroup);

        ComboBoxes.setupActionListenerForComboBox(ComboBoxes.getComboBoxGroups(),Tables.getDataModelStudents());
   /*     cmbGroups.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedGroupIndex = cmbGroups.getSelectedIndex();
                addStudentsToTableStudents(dataModelStudents, selectedGroupIndex);
                outputLecturesToTable();
                setTableAttendanceAppearance();
            }
        });*/

        // Add items to the popup menu for the group
        pmnGroup.add(mniAddGroup);
        pmnGroup.addSeparator();
        pmnGroup.add(mniDeleteGroup);

        // Add the drop-down list and label to the group management panel
        pnlGroups.add(lblGroupSelect);
        pnlGroups.add(ComboBoxes.getComboBoxGroups());

        // Create a panel for the subjects
        JPanel pnlSubjects = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlSubjects.setBackground(Application.MAIN_COLOR);

        // Add a label for selecting a subject
        JLabel lblSubjectSelect = new JLabel("Предмет:");
        lblSubjectSelect.setFont(new Font("Arial", Font.BOLD, 12));
        lblSubjectSelect.setForeground(Color.black);

   /*     // Create a drop-down list for selecting a subject
        ArrayList<Subject> subjects = new ArrayList<>(DatabaseManager.getSubjects());
        cmbSubjects = new JComboBox(subjects.toArray());
        cmbSubjects.setFont(new Font("Arial", Font.PLAIN, 12));
        cmbSubjects.setPreferredSize(new Dimension(120, 25));
        cmbSubjects.setToolTipText("Выбрать предмет");*/

        // Create a popup menu for the subject
        JPopupMenu pmnSubject = new JPopupMenu();
        mniAddSubject = new JMenuItem("Добавить");
        mniDeleteSubject = new JMenuItem("Удалить");

        ComboBoxes.getComboBoxSubjects().addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    int selectedSubjectIndex = ComboBoxes.getComboBoxSubjects().getSelectedIndex();
                    pmnSubject.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
        ComboBoxes.getComboBoxSubjects().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Tables.outputLecturesToTable();
                Tables.setTableAttendanceAppearance();
            }
        });

        // Add items to the popup menu for the lecture
        pmnSubject.add(mniAddSubject);
        pmnSubject.addSeparator();
        pmnSubject.add(mniDeleteSubject);

        // Add the drop-down list and label to the lecture panel
        pnlSubjects.add(lblSubjectSelect);
        pnlSubjects.add(ComboBoxes.getComboBoxSubjects());

        // Create a panel for the top part of the center
        JPanel pnlUpCenter = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlUpCenter.setBackground(Application.MAIN_COLOR);

        // Add the group and subject panels to the top center panel
        pnlUpCenter.add(pnlGroups);
        pnlUpCenter.add(pnlSubjects);
        pnlCenter.add(pnlUpCenter, BorderLayout.NORTH);
    }

    /**
     * Creates the center components of the main GUI window, including the tables and control buttons.
     */
    void createCenterComponents() {
        JPanel pnlTables = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));


        pnlTables.add(Tables.createStudentTableComponents());
        pnlTables.add(Tables.createAttendanceTableComponents());
        pnlTables.setBorder(BorderFactory.createEmptyBorder(10, 20, 50, 0));

        JPanel pnlButtons = createControlComponents();
        pnlCenter.add(pnlTables, BorderLayout.WEST);
        pnlCenter.add(pnlButtons, BorderLayout.EAST);

        Tables.outputLecturesToTable();
        Tables.setTableAttendanceAppearance();
    }

    /**
     * Creates and adds the bottom components to the main panel.
     */
    void createBottomComponents() {
        JPanel leftDownPanel = new JPanel(new BorderLayout());
        JPanel btnHolderPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        leftDownPanel.setBackground(Application.MAIN_COLOR);
        btnHolderPanel.setBackground(Application.MAIN_COLOR);

        JButton btnAboutApplication = createAboutApplicationButton();
        btnHolderPanel.add(btnAboutApplication);

        leftDownPanel.add(btnHolderPanel, BorderLayout.WEST);

        //add right down exit button
        JPanel rightDownPanel = new JPanel(new BorderLayout());

        JPanel pnlExitHolder = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnExit = createExitButton();
        pnlExitHolder.add(btnExit);
        rightDownPanel.add(pnlExitHolder, BorderLayout.EAST);

        rightDownPanel.setBackground(Application.MAIN_COLOR);
        pnlExitHolder.setBackground(Application.MAIN_COLOR);

        pnlBottom.add(leftDownPanel, BorderLayout.WEST);
        pnlBottom.add(rightDownPanel, BorderLayout.EAST);
    }

    public JPanel getPnlMain() {
        return pnlMain;
    }
    /**
     * This method creates a JPanel that contains buttons for managing the student table.
     *
     * @return Panel containing buttons for managing tables.
     */
    private JPanel createControlComponents() {
        btnAddStudent = new JButton("Добавить студента");
        btnAddStudent.setFont(new Font("Arial", Font.PLAIN, 12));
        btnAddStudent.setBackground(new Color(225, 225, 225));
        btnAddStudent.setBorder(BorderFactory.createRaisedBevelBorder());
        btnAddStudent.setPreferredSize(new Dimension(140, 25));

        btnDeleteStudent = new JButton("Удалить студента");
        btnDeleteStudent.setFont(new Font("Arial", Font.PLAIN, 12));
        btnDeleteStudent.setBackground(new Color(225, 225, 225));
        btnDeleteStudent.setBorder(BorderFactory.createRaisedBevelBorder());
        btnDeleteStudent.setPreferredSize(new Dimension(140, 25));

        JButton btnSaveChanges = new JButton("Сохранить изменения");
        btnSaveChanges.setFont(new Font("Arial", Font.BOLD, 12));
        btnSaveChanges.setBackground(new Color(225, 225, 225));
        btnSaveChanges.setBorder(BorderFactory.createRaisedBevelBorder());
        btnSaveChanges.addActionListener(new SavingAttendanceListener());
        btnSaveChanges.setPreferredSize(new Dimension(140, 25));

        btnSaveToFile = new JButton("Сохранить в Файл");
        btnSaveToFile.setFont(new Font("Arial", Font.PLAIN, 12));
        btnSaveToFile.setBackground(new Color(225, 225, 225));
        btnSaveToFile.setBorder(BorderFactory.createRaisedBevelBorder());
        btnSaveToFile.setPreferredSize(new Dimension(140, 25));

        JButton btnPrintTable = new JButton("Вывести на печать");
        btnPrintTable.setFont(new Font("Arial", Font.PLAIN, 12));
        btnPrintTable.setBackground(new Color(225, 225, 225));
        btnPrintTable.setBorder(BorderFactory.createRaisedBevelBorder());
        btnPrintTable.addActionListener(new Tables.PrintTableListener());
        btnPrintTable.setPreferredSize(new Dimension(140, 25));


        JPanel pnlButtons = new JPanel(new BorderLayout());
        pnlButtons.setBorder(BorderFactory.createEmptyBorder(10, 20, 18, 20));
        JPanel pnlStudentManagement = new JPanel(new GridLayout(2, 1, 10, 5));
        pnlStudentManagement.add(btnAddStudent);
        pnlStudentManagement.add(btnDeleteStudent);
        pnlButtons.add(pnlStudentManagement, BorderLayout.NORTH);

        JPanel pnlTableManagement = new JPanel(new GridLayout(3, 1, 10, 5));
        pnlTableManagement.add(btnSaveChanges);
        pnlTableManagement.add(btnSaveToFile);
        pnlTableManagement.add(btnPrintTable);
        pnlButtons.add(pnlTableManagement, BorderLayout.SOUTH);

        return pnlButtons;
    }
    /**
     * Creates a button for displaying information about the author of the application.
     *
     * @return the "About Author" button
     */
    private JButton createAboutAuthorButton() {
        JButton btnAboutAuthor = new JButton("Об Авторе");
        btnAboutAuthor.setFont(new Font("Arial", Font.BOLD, 12));
        btnAboutAuthor.setPreferredSize(new Dimension(120, 30));
        return btnAboutAuthor;
    }

    /**
     * Сreates a button to get info about application
     *
     * @return the "About GUI.Application" button
     */
    private JButton createAboutApplicationButton() {
        btnAboutProgram = new JButton("О программе");
        btnAboutProgram.setFont(new Font("Arial", Font.BOLD, 12));
        btnAboutProgram.setPreferredSize(new Dimension(120, 30));

        return btnAboutProgram;
    }

    /**
     * Creates an "Exit" button with the specified font, size, and text.
     *
     * @return the "Exit" button
     */
    private JButton createExitButton() {
        btnExit = new JButton("Выход");
        btnExit.setFont(new Font("Arial", Font.BOLD, 12));
        btnExit.setPreferredSize(new Dimension(120, 30));
        return btnExit;
    }

    /**
     * Listens for clicks on the "Save Attendance" button and performs actions to save attendance data
     * and update the table appearance.
     */
    class SavingAttendanceListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Tables.getSorterAttend().setSortKeys(null);
            Tables.saveAttendance();
            Tables.outputLecturesToTable();
            Tables.setTableAttendanceAppearance();
            Tables.saveDataModel();

        }
    }

    public static JButton getBtnDeleteStudent() {
        return btnDeleteStudent;
    }

    public static JButton getBtnAddStudent() {
        return btnAddStudent;
    }

    public static JButton getBtnExit() {
        return btnExit;
    }
    public static JButton getBtnAboutProgram() {
        return btnAboutProgram;
    }

    public static JMenuItem getMniDeleteSubject() {
        return mniDeleteSubject;
    }

    public static JMenuItem getMniDeleteGroup() {
        return mniDeleteGroup;
    }

    public static JMenuItem getMniAddGroup() {
        return mniAddGroup;
    }

    public static JMenuItem getMniAddSubject() {
        return mniAddSubject;
    }

    public static JButton getBtnSaveToFile() {
        return btnSaveToFile;
    }

}

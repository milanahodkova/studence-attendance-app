package gui;

import database.DatabaseManager;
import dialogs.LectureAddDialog;
import dialogs.StudentInfoDialog;
import entities.Lecture;
import entities.Student;
import entities.StudentLectureRecord;

import javax.swing.*;
import javax.swing.event.RowSorterEvent;
import javax.swing.event.RowSorterListener;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.print.PrinterException;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class Tables {
    private static MainWindow mainWindow;
    private static DefaultTableModel dataModelAttendance;
    private static JTable tblAttendance;
    private static JMenuItem mniDeleteLecture;
    private static int clickedIndex;
    private static JScrollPane scrTable;
    private static JScrollBar scbVertical = new JScrollBar(JScrollBar.VERTICAL);
    public static TableRowSorter<TableModel> getSorterAttend() {
        return sorterAttend;
    }
    private static TableRowSorter<TableModel> sorterAttend;
    private static DefaultTableModel tempDataModelStudents;
    private static ArrayList<Integer> index2 = new ArrayList<Integer>();
    private static TableRowSorter<TableModel> sorterStudents;
    private static Map<Integer, Integer> studentsMap = new HashMap<Integer, Integer>();
    private static DefaultTableModel dataModelStudents;
    private static DefaultTableModel tempDataModelAttendance;
    private static JTable tblStudents;
    private static StudentInfoDialog studentInfoDialog;
    private static LectureAddDialog lectureAddDialog;
    private static JScrollPane scrStudents;
    private static Map<Integer, Integer> dataMap = new HashMap<Integer, Integer>();

    public Tables(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }

    /**
     * Creates and returns the components for the attendance table.
     * Adds a RowSorterListener to sorterAttend to handle sorting events.
     *
     * @return a JScrollPane containing the attendance table.
     */
    static JScrollPane createAttendanceTableComponents() {
        dataModelAttendance = new DefaultTableModel();
        tblAttendance = new JTable(dataModelAttendance);
        tblAttendance.addKeyListener(new KeyChecker());
        tblAttendance.getTableHeader().addMouseListener(new MouseHeaderChecker(mainWindow));

        ComboBoxes.createAndPopulateCmbAttendance();

        JPopupMenu pmnLecture = new JPopupMenu();
        mniDeleteLecture = new JMenuItem("Удалить");
        pmnLecture.add(mniDeleteLecture);

        JTableHeader header = tblAttendance.getTableHeader();
        header.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                clickedIndex = tblAttendance.convertColumnIndexToModel(tblAttendance.columnAtPoint(e.getPoint()));
                if (clickedIndex != tblAttendance.getColumnCount() - 1 &&
                        clickedIndex != tblAttendance.getColumnCount() - 2) {
                    if (e.getButton() == MouseEvent.BUTTON3) {
                        pmnLecture.show(e.getComponent(), e.getX(), e.getY());
                    }
                }
            }
        });


        scrTable = new JScrollPane(tblAttendance);
        scrTable.setVerticalScrollBar(scbVertical);

        JScrollBar scbHorizontal = new JScrollBar(JScrollBar.HORIZONTAL);
        scrTable.setHorizontalScrollBar(scbHorizontal);
        setTableAttendanceAppearance();

        sorterAttend = new TableRowSorter<>(dataModelAttendance);
        tblAttendance.setRowSorter(sorterAttend);
        tempDataModelStudents = new DefaultTableModel();
        sorterAttend.addRowSorterListener(new RowSorterListener() {
            @Override
            public void sorterChanged(RowSorterEvent e) {
                index2.clear();
                sorterStudents.setSortKeys(null);

                if (e.getType() == RowSorterEvent.Type.SORTED) {
                    int rowCount = sorterAttend.getViewRowCount();
                    for (int i = 0; i < rowCount; i++) {
                        int viewIndex = sorterAttend.convertRowIndexToView(i);
                        studentsMap.put(i, viewIndex);
                    }

                    Map<Integer, Integer> top = studentsMap.entrySet().stream()
                            .sorted(Map.Entry.comparingByValue())
                            .collect(Collectors.toMap(
                                    Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

                    for (Map.Entry<Integer, Integer> entry : top.entrySet()) {
                        index2.add(entry.getKey());
                    }
                    if (tempDataModelStudents.getRowCount() == 0) {
                        saveDataModel();
                        saveDataModelStudents();
                    }
                    for (int i = 0; i < dataModelStudents.getRowCount(); i++) {
                        for (int j = 0; j < dataModelStudents.getColumnCount(); j++) {
                            dataModelStudents.setValueAt(tempDataModelStudents.getValueAt(index2.get(i), j), i, j);
                        }
                    }
                }
            }
        });

        return scrTable;
    }

    /**
     * This method creates a JScrollPane that contains a JTable which shows student data.
     * Adds a RowSorterListener to sorterStudents to handle sorting events.
     *
     * @return JScrollPane containing a JTable with student data.
     */
    static JScrollPane createStudentTableComponents() {
        dataModelStudents = new DefaultTableModel();
        tblStudents = new JTable(dataModelStudents);
        tblStudents.addMouseListener(new MouseChecker(mainWindow));

        dataModelStudents.addColumn("№");
        dataModelStudents.addColumn("Ф.И.О.");
        addStudentsToTableStudents(dataModelStudents, ComboBoxes.createCmbGroups().getSelectedIndex());


        //scbVertical = new JScrollBar(JScrollBar.VERTICAL);
        scrStudents = new JScrollPane(tblStudents);
        setTableStudentsAppearance();
        scrStudents.setVerticalScrollBar(scbVertical);

        sorterStudents = new TableRowSorter<>(dataModelStudents);
        tblStudents.setRowSorter(sorterStudents);
        tempDataModelAttendance = new DefaultTableModel();

        sorterStudents.addRowSorterListener(new RowSorterListener() {
            @Override
            public void sorterChanged(RowSorterEvent e) {
                if (e.getType() == RowSorterEvent.Type.SORTED) {

                    index2.clear();
                    sorterAttend.setSortKeys(null);

                    int rowCount = sorterStudents.getViewRowCount();
                    for (int i = 0; i < rowCount; i++) {
                        int viewIndex = sorterStudents.convertRowIndexToView(i);
                        dataMap.put(i, viewIndex);
                    }

                    Map<Integer, Integer> top = dataMap.entrySet().stream()
                            .sorted(Map.Entry.comparingByValue())
                            .collect(Collectors.toMap(
                                    Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

                    for (Map.Entry<Integer, Integer> entry : top.entrySet()) {
                        index2.add(entry.getKey());
                    }
                    if (tempDataModelAttendance.getRowCount() == 0) {
                        saveDataModel();
                    }
                    for (int i = 0; i < dataModelAttendance.getRowCount(); i++) {
                        for (int j = 0; j < dataModelAttendance.getColumnCount(); j++) {
                            dataModelAttendance.setValueAt(tempDataModelAttendance.getValueAt(index2.get(i), j), i, j);
                        }
                    }
                }
            }
        });

        return scrStudents;
    }

    /**
     * This method populates the student table with data for the selected group.
     *
     * @param dataModel          The DefaultTableModel for the student table.
     * @param selectedGroupIndex The index of the selected group in the list of groups.
     */
    protected static void addStudentsToTableStudents(DefaultTableModel dataModel, int selectedGroupIndex) {
        dataModel.setRowCount(0);
        int count = 0;
        //System.out.println(DatabaseManager.getGroups().get(selectedGroupIndex).getGroupNumber());
        if (selectedGroupIndex != -1) {
            for (Student student : DatabaseManager.getStudentsFromGroup(DatabaseManager.getGroups()
                    .get(selectedGroupIndex).getGroupNumber())) {
                count++;
                dataModel.addRow(new Object[]{count, student.getFIO()});
            }
        }
    }

    /**
     * This method sets the appearance of the attendance table.
     */
    public static void setTableAttendanceAppearance() {
        tblAttendance.setFont(new Font("Arial", Font.PLAIN, 14));
        tblAttendance.setRowHeight(25);
        tblAttendance.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (int i = 0; i < tblAttendance.getColumnCount(); i++) {
            tblAttendance.getColumnModel().getColumn(i).setPreferredWidth(55);
        }

        tblAttendance.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        if (tblAttendance.getColumnCount() != 0) {
            tblAttendance.getColumnModel().getColumn(tblAttendance.getColumnCount() - 1).setCellRenderer(centerRenderer);
            sorterAttend.setSortable(tblAttendance.getColumnCount() - 2, false);
        }

        scrTable.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrTable.setViewportView(tblAttendance);
        scrTable.setPreferredSize(new Dimension(730, 610));
    }

    /**
     * This method is used to save a copy of the original attendance table data before it is sorted.
     */
    static void saveDataModel() {
        tempDataModelAttendance = new DefaultTableModel();
        // Копируем столбцы
        for (int i = 0; i < dataModelAttendance.getColumnCount(); i++) {
            tempDataModelAttendance.addColumn(dataModelAttendance.getColumnName(i));
        }

        // Копируем данные
        for (int i = 0; i < dataModelAttendance.getRowCount(); i++) {
            Object[] rowData = new Object[dataModelAttendance.getColumnCount()];
            for (int j = 0; j < dataModelAttendance.getColumnCount(); j++) {
                rowData[j] = dataModelAttendance.getValueAt(i, j);
            }
            tempDataModelAttendance.addRow(rowData);
        }
    }

    /**
     * This method is used to save a copy of the original students table data before it is sorted.
     */
    private static void saveDataModelStudents() {
        tempDataModelStudents = new DefaultTableModel();
        for (int i = 0; i < dataModelStudents.getColumnCount(); i++) {
            tempDataModelStudents.addColumn(dataModelStudents.getColumnName(i));
        }

        for (int i = 0; i < dataModelStudents.getRowCount(); i++) {
            Object[] rowData = new Object[dataModelStudents.getColumnCount()];
            for (int j = 0; j < dataModelStudents.getColumnCount(); j++) {
                rowData[j] = dataModelStudents.getValueAt(i, j);
            }
            tempDataModelStudents.addRow(rowData);
        }
    }

    static class KeyChecker extends KeyAdapter {
        private MainWindow owner;

        @Override
        public void keyPressed(KeyEvent event) {
            if (event.getKeyCode() == KeyEvent.VK_SPACE) {
                String comboBoxValue = (String) tblAttendance.getValueAt(tblAttendance.getSelectedRow(), tblAttendance.getSelectedColumn());
                int newComboBoxItem = 0;
                if (comboBoxValue != null) {
                    switch (comboBoxValue) {
                        case " ":
                            newComboBoxItem = 1;
                            break;
                        case "2":
                            newComboBoxItem = 2;
                            break;
                        case "1":
                            newComboBoxItem = 0;
                            break;
                    }

                    tblAttendance.setValueAt(ComboBoxes.getCmbAttendance().getItemAt(newComboBoxItem), tblAttendance.getSelectedRow(), tblAttendance.getSelectedColumn());
                    tblAttendance.clearSelection();
                }
            }

            if (event.getKeyCode() == KeyEvent.VK_PAGE_UP && tblStudents.getSelectedColumn() == 1) {
                showStudentInfoCard(owner);
                studentInfoDialog = new StudentInfoDialog(owner);
                studentInfoDialog.enableFrame(true);
            }
        }

        private void showStudentInfoCard(MainWindow owner) {
            this.owner = owner;

        }
    }

    /**
     * Listens for mouse clicks on the table header and displays a dialog for adding a new lecture
     * if the click occurred on the second-to-last column of the table.
     */
    static class MouseHeaderChecker extends MouseAdapter {
        private MainWindow owner;

        public MouseHeaderChecker(MainWindow owner) {
            this.owner = owner;
        }

        @Override
        public void mouseClicked(MouseEvent event) {
            clickedIndex = tblAttendance.convertColumnIndexToModel(tblAttendance.columnAtPoint(event.getPoint()));
            if (clickedIndex == tblAttendance.getColumnCount() - 2) {
                lectureAddDialog = new LectureAddDialog(owner);
                lectureAddDialog.enableFrame(true);
                owner.setEnabled(true);
            }
        }
    }

    /**
     * Listens for mouse clicks on the table and displays a dialog with information about the selected student
     * if the click occurred on the second column of the table.
     */
    static class MouseChecker extends MouseAdapter {
        private MainWindow owner;

        public MouseChecker(MainWindow owner) {
            this.owner = owner;
        }

        @Override
        public void mouseClicked(MouseEvent event) {

            int column = tblStudents.getSelectedColumn(); // select a column

            if (column == 1) {
                studentInfoDialog = new StudentInfoDialog(owner);
                studentInfoDialog.enableFrame(true);
                owner.setEnabled(true);
            }
        }
    }

    /**
     * This method sets the appearance of the student table.
     */
    public static void setTableStudentsAppearance() {
        tblStudents.setFont(new Font("Arial", Font.PLAIN, 14));
        tblStudents.setRowHeight(25);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tblStudents.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tblStudents.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tblStudents.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        tblStudents.getColumnModel().getColumn(0).setPreferredWidth(30);
        tblStudents.getColumnModel().getColumn(1).setPreferredWidth(275);
        scrStudents.setViewportView(tblStudents);
        scrStudents.setPreferredSize(new Dimension(308, 610));
    }

    /**
     * This method populates the attendance table with data for the selected subject and group.
     */
    public static void outputLecturesToTable() {

        dataModelAttendance.setColumnCount(0);
        dataModelAttendance.setRowCount(tblStudents.getRowCount());

        if (ComboBoxes.getComboBoxGroups().getSelectedIndex() != -1 && ComboBoxes.getComboBoxSubjects().getSelectedIndex() != -1) {
            ArrayList<Lecture> existingLectures = DatabaseManager.getLecturesBySubjectAndGroup(DatabaseManager.getSubjects()
                    .get(ComboBoxes.getComboBoxSubjects().getSelectedIndex()), DatabaseManager.getGroups()
                    .get(ComboBoxes.getComboBoxGroups().getSelectedIndex()));
            DateFormat formatter = new SimpleDateFormat("dd.MM.yy");
            if (existingLectures == null) return;
            int column = 0;
            Collections.sort(existingLectures, new Comparator<Lecture>() {
                @Override
                public int compare(Lecture o1, Lecture o2) {
                    if (o1.getDate().before(o2.getDate())) {
                        return -1;
                    }
                    return 1;
                }
            });

            for (Lecture item : existingLectures) {
                int i = 0;
                String date = formatter.format(item.getDate());
                dataModelAttendance.addColumn(date);
                for (var student : dataModelStudents.getDataVector()) {
                    StudentLectureRecord studentLectureRecord = DatabaseManager.getStudentLecture(item, new Student(student.get(1).toString(),
                            DatabaseManager.getGroups().get(ComboBoxes.getComboBoxGroups().getSelectedIndex())));
                    if (studentLectureRecord != null) {
                        dataModelAttendance.setValueAt(studentLectureRecord.getPresence(), i, column);
                    } else {
                        dataModelAttendance.setValueAt(ComboBoxes.getCmbAttendance().getItemAt(0), i, column);
                    }
                    i++;
                }
                column++;
            }
            dataModelAttendance.addColumn("Добавить");
            dataModelAttendance.addColumn("Всего");

            for (int i = 0; i < tblAttendance.getColumnCount() - 1; i++) {
                tblAttendance.getColumnModel().getColumn(i).setCellEditor(new DefaultCellEditor(ComboBoxes.getCmbAttendance()));
                tblAttendance.getColumnModel().getColumn(i).setCellRenderer(new ColumnStatusCellRender());
            }

            for (int i = 0; i < dataModelAttendance.getRowCount(); i++) {
                dataModelAttendance.setValueAt(ComboBoxes.getCmbAttendance().getItemAt(0), i, tblAttendance.getColumnCount() - 2);
            }

            for (int i = 0; i < dataModelAttendance.getRowCount(); i++) {
                dataModelAttendance.setValueAt(sumStudentAttendance(i), i, tblAttendance.getColumnCount() - 1);
            }

            for (int i = 0; i < tblAttendance.getColumnCount(); i++) {
                sorterAttend.setSortable(i, true);
            }
            sorterAttend.setComparator(tblAttendance.getColumnCount() - 1, new Comparator<Integer>() {
                @Override
                public int compare(Integer o1, Integer o2) {
                    return o1.compareTo(o2);
                }

            });
        }
    }

    /**
     * Sums the attendance values for the specified row in the attendance data model.
     *
     * @param row the row index to sum attendance values for
     * @return the total attendance for the specified row
     */
    private static int sumStudentAttendance(int row) {
        int total = 0;
        for (int i = 0; i < dataModelAttendance.getColumnCount() - 1; i++) {
            try {
                total += Integer.parseInt(dataModelAttendance.getValueAt(row, i).toString());
            } catch (Exception e) {
                continue;
            }
        }
        return total;
    }

    /**
     * Saves the attendance data to the database, either by updating existing lectures or adding new ones.
     * The method also adds new students to the database if they have not already been added.
     */
    public static void saveAttendance() {
        DateFormat formatter = new SimpleDateFormat("dd.MM.yy");
        boolean flag = true;
        int count = 0;

        if (ComboBoxes.getComboBoxSubjects().getSelectedIndex() != -1) {
            ArrayList<Lecture> existingLectures = DatabaseManager.getLecturesBySubject(DatabaseManager.getSubjects()
                    .get(ComboBoxes.getComboBoxSubjects().getSelectedIndex()));
            for (int i = 0; i < dataModelAttendance.getColumnCount(); i++) {
                flag = true;
                String date = tblAttendance.getColumnModel().getColumn(i).getHeaderValue().toString();
                if (date == null) break;
                Lecture lecture;
                try {
                    lecture = new Lecture(DatabaseManager.getSubjects().get(ComboBoxes.getComboBoxSubjects().getSelectedIndex()),
                            formatter.parse(date));
                } catch (ParseException ex) {
                    break;
                }

                for (int j = 0; j < existingLectures.size(); j++) {
                    Lecture item = existingLectures.get(j);
                    String dateItem = formatter.format(item.getDate());
                    if (item.getSubject().getSubjectName().equals(lecture.getSubject().getSubjectName())
                            && ((date.equals(dateItem)))) {
                        lecture = item;
                        for (var vector : dataModelStudents.getDataVector()) {

                                Student student = new Student(vector.get(1).toString(),
                                        DatabaseManager.getGroups().get(ComboBoxes.getComboBoxGroups().getSelectedIndex()),
                                        (dataModelAttendance.getDataVector().elementAt(count)).elementAt(i).toString());
                                if (DatabaseManager.isStudentAdded(student)) {
                                    DatabaseManager.addStudentLectureAttendance(student, lecture);
                                } else {
                                    DatabaseManager.addStudent(student);
                                    DatabaseManager.addStudentLectureAttendance(student, lecture);
                                }
                            count++;
                        }
                        flag = false;
                        count = 0;
                        break;
                    }

                }
                if (flag) {
                    DatabaseManager.addLecture(lecture);
                    for (var student : dataModelStudents.getDataVector()) {
                        DatabaseManager.addStudentLectureAttendance(new Student(student.get(1).toString(), DatabaseManager.getGroups()
                                .get(ComboBoxes.getComboBoxGroups().getSelectedIndex()), (dataModelAttendance.getDataVector().elementAt(count))
                                .elementAt(i).toString()), lecture);
                        count++;
                    }
                }
            }
        }
    }

    /**
     * Refreshes the attendance table by re-outputting the lecture data to the table
     * and updating the appearance of the table based on the attendance values.
     */
    public static void refreshTable() {
        outputLecturesToTable();
        setTableAttendanceAppearance();
    }
    /**
     * Listens for clicks on the "Print" button and prints the table data to a printer or a PDF file.
     */
    static class PrintTableListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            DefaultTableModel mergedModel = new DefaultTableModel();
            for (int i = 0; i < dataModelStudents.getColumnCount(); i++) {
                mergedModel.addColumn(dataModelStudents.getColumnName(i));
            }
            for (int i = 0; i < dataModelAttendance.getColumnCount(); i++) {
                if (i != dataModelAttendance.getColumnCount() - 2) {
                    mergedModel.addColumn(dataModelAttendance.getColumnName(i));
                }
            }
            for (int i = 0; i < dataModelStudents.getRowCount(); i++) {
                Object[] rowData = new Object[mergedModel.getColumnCount()];
                for (int j = 0; j < dataModelStudents.getColumnCount(); j++) {
                    if (i < dataModelStudents.getRowCount()) {
                        rowData[j] = dataModelStudents.getValueAt(i, j);
                    }

                }
                for (int j = 0; j < dataModelAttendance.getColumnCount(); j++) {
                    if (i < dataModelAttendance.getRowCount() && j != dataModelAttendance.getColumnCount() - 2) {
                        String columnName = dataModelAttendance.getColumnName(j);
                        int columnIndex = getColumnIndex(mergedModel, columnName);
                        rowData[columnIndex] = dataModelAttendance.getValueAt(i, j);
                    }
                }
                ((DefaultTableModel) mergedModel).addRow(rowData);
            }

            JTable mergedTable = new JTable(mergedModel);
            mergedTable.setFont(new Font("Arial", Font.PLAIN, 14));
            mergedTable.setRowHeight(25);
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);
            mergedTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            mergedTable.getColumnModel().getColumn(0).setPreferredWidth(30);
            mergedTable.getColumnModel().getColumn(1).setPreferredWidth(275);
            mergedTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
            for (int i = 2; i < mergedTable.getColumnCount(); i++) {
                mergedTable.getColumnModel().getColumn(i).setPreferredWidth(55);
            }
            JScrollPane scrollPane = new JScrollPane(mergedTable);
            scrollPane.setPreferredSize(new Dimension(1038, 585));
            JPanel pnlMergedTable = new JPanel(new BorderLayout());
            pnlMergedTable.add(scrollPane);

            JFrame tempFrame = new JFrame("Print Preview");
            tempFrame.getContentPane().add(pnlMergedTable);
            tempFrame.setSize(new Dimension(1040, 600));
            tempFrame.setLocationRelativeTo(null);
            tempFrame.setVisible(true);
            tempFrame.dispose();

            if (ComboBoxes.getComboBoxSubjects().getSelectedIndex() != -1 && ComboBoxes.getComboBoxGroups().getSelectedIndex() != -1) {
                MessageFormat header = new MessageFormat("Посещаемость лекционных занятий ( " +
                        ComboBoxes.getSelectedSubject().getSubjectName() + " ," +
                        ComboBoxes.getSelectedGroup().getGroupNumber() + " )");
                try {
                    mergedTable.print(JTable.PrintMode.FIT_WIDTH, header, null, true, null,
                            true, null);
                } catch (PrinterException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }

        private int getColumnIndex(TableModel model, String columnName) {
            for (int i = 0; i < model.getColumnCount(); i++) {
                if (model.getColumnName(i).equals(columnName)) {
                    return i;
                }
            }
            return -1;
        }

    }
    /**
     * This method updates the student table.
     */
    public static void refreshStudents() {
        addStudentsToTableStudents(dataModelStudents, ComboBoxes.getComboBoxGroups().getSelectedIndex());
    }
    /**
     * Returns the index of the last clicked column in the attendance table.
     *
     * @return The index of the last clicked column in the attendance table
     */
    public static int getClickedIndex() {
        return clickedIndex;
    }
    public static JTable getTableAttendance() {
        return tblAttendance;
    }

    public static JTable getTableStudents() {
        return tblStudents;
    }

    public static int getSelectedTableStudentsRow() {
        return tblStudents.getSelectedRow();
    }

    public static int getSelectedTableStudentsColumn() {
        return tblStudents.getSelectedColumn();
    }
    public DefaultTableModel getDataModelAttendance() {
        return dataModelAttendance;
    }

    public static DefaultTableModel getStudentsDataModel() {
        return dataModelStudents;
    }

    public static DefaultTableModel getDataModelStudents() {
        return dataModelStudents;
    }
    public static JMenuItem getMniDeleteLecture() {
        return mniDeleteLecture;
    }

}

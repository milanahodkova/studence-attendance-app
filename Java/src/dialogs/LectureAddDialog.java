package dialogs;

import gui.*;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

/**
 * JDialog class used to input and add Lecture
 *
 * @author Hodkova M.O.
 * @version 1.0
 */
public class LectureAddDialog extends JDialog {
    private JPanel pnlMain;
    private MainWindow mainWindow;
    private JPanel pnlMiddle;
    private JPanel pnlBottom;
    private static JDatePickerImpl datePicker;
    private Date selectedDate;
    private Dimension dialogSize = new Dimension(350, 140);

    /**
     * Constructor for LectureAddDialog
     *
     * @param mainWindow MainWindow instance
     */
    public LectureAddDialog(MainWindow mainWindow) {
        super(mainWindow, "Добавить лекцию");
        this.mainWindow = mainWindow;
        pnlMain = new JPanel(new BorderLayout(10, 10));

        pnlMain.add(createMiddlePanel(), BorderLayout.CENTER);
        pnlMain.add(createBottomPanel(), BorderLayout.SOUTH);

        setUpDialog();
    }

    /**
     * Sets UI necessary settings for lectureAddDialog
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
     * enables or disable JDialog instance
     *
     * @param active flag to indicate whether to enable or disable the dialog
     */
    public void enableFrame(boolean active) {
        if (active) {
            setSize(dialogSize);
            setLocationRelativeTo(getOwner());
        }
        setVisible(active);
    }

    /**
     * creates necessary UI objects for bottom panel
     *
     * @return JPanel instance for bottom panel
     */
    private JPanel createBottomPanel() {
        pnlBottom = new JPanel(new BorderLayout());

        Dimension btnPreferredSize = new Dimension(110, 30);

        JPanel pnlAddLecture = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnAddGroup = new JButton("Добавить");
        btnAddGroup.setFont(new Font("Arial", Font.BOLD, 12));
        btnAddGroup.setBackground(Application.MAIN_COLOR);
        btnAddGroup.setPreferredSize(btnPreferredSize);
        pnlAddLecture.add(btnAddGroup);
        btnAddGroup.addActionListener(new AddLectureListener(this));

        JPanel pnlCancel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnCancel = new JButton("Отмена");
        btnCancel.setFont(new Font("Arial", Font.BOLD, 12));
        btnCancel.setBackground(Application.MAIN_COLOR);
        btnCancel.setPreferredSize(btnPreferredSize);
        btnCancel.addActionListener(new CancelDialogListener(this));
        pnlCancel.add(btnCancel);

        pnlBottom.add(pnlAddLecture, BorderLayout.WEST);
        pnlBottom.add(pnlCancel, BorderLayout.EAST);
        return pnlBottom;
    }

    /**
     * creates necessary UI objects for middle panel
     *
     * @return JPanel instance for middle panel
     */
    private JPanel createMiddlePanel() {
        pnlMiddle = new JPanel(new BorderLayout(10, 10));
        JPanel pnlFieldHolder = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel lblAddDate = new JLabel("Выберите дату: ");
        lblAddDate.setFont(new Font("Arial", Font.BOLD, 12));

        UtilDateModel model = new UtilDateModel();
        model.setSelected(true);
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
        datePicker.getComponent(0).setPreferredSize(new Dimension(120, 25));

        pnlFieldHolder.setBorder(new EmptyBorder(15, 10, 0, 10));
        pnlFieldHolder.add(lblAddDate);
        pnlFieldHolder.add(datePicker);
        pnlMiddle.add(pnlFieldHolder, BorderLayout.CENTER);
        return pnlMiddle;
    }

    /**
     * Cancel AddLectureDialog listener
     */
    class CancelDialogListener implements ActionListener {
        private JDialog dialog;

        /**
         * Constructor for CancelDialogListener
         *
         * @param dialog JDialog instance
         */
        public CancelDialogListener(JDialog dialog) {
            this.dialog = dialog;
        }

        public void actionPerformed(ActionEvent e) {
            dialog.setVisible(false);
        }
    }

    /**
     * Add Lecture Listener
     */
    class AddLectureListener implements ActionListener {
        private JDialog dialog;

        /**
         * Constructs a new AddLectureListener instance.
         *
         * @param dialog The JDialog instance to be hidden upon execution.
         */
        public AddLectureListener(JDialog dialog) {
            this.dialog = dialog;
        }

        public void actionPerformed(ActionEvent e) {
            selectedDate = (Date) datePicker.getModel().getValue();
            if (datePicker.getJFormattedTextField().getValue() == null) {
                JOptionPane.showMessageDialog(null,
                        "Заполните данные!",
                        "Ошибка операции", JOptionPane.ERROR_MESSAGE);
                return;
            }
            changeColumnDate();
            Tables.saveAttendance();
            Tables.refreshTable();
            dialog.setVisible(false);
        }
    }

    /**
     * Helper method for changing the column date in the main window table.
     */
    private void changeColumnDate() {
        DateFormat formatter = new SimpleDateFormat("dd.MM.yy");
        String date = formatter.format(selectedDate);
        getTableFromMainWindow().getColumnModel().getColumn(getTableFromMainWindow().getColumnCount() - 2).setHeaderValue(date);
        getTableFromMainWindow().getColumnModel().getColumn(getTableFromMainWindow().getColumnCount() - 2)
                .setCellEditor(new DefaultCellEditor(ComboBoxes.getCmbAttendance()));
        getTableFromMainWindow().getColumnModel().getColumn(getTableFromMainWindow().getColumnCount() - 2)
                .setCellRenderer(new ColumnStatusCellRender());
        for (int i = 0; i < getTableFromMainWindow().getRowCount(); i++) {
            getTableFromMainWindow().getModel().setValueAt(ComboBoxes.getCmbAttendance().getItemAt(0),
                    i, getTableFromMainWindow().getColumnCount() - 2);
        }

        //mainWindow.repaint();

    }

    /**
     * Helper method for retrieving the main window table.
     *
     * @return The JTable instance representing the main window table.
     */
    private JTable getTableFromMainWindow() {
        return Tables.getTableAttendance();
    }

    /**
     * AbstractFormatter implementation for formatting dates in the AddLectureDialog date picker.
     */
    class DateLabelFormatter extends JFormattedTextField.AbstractFormatter {
        private String datePattern = "dd.MM.yyyy";
        private SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);

        @Override
        public Object stringToValue(String text) throws ParseException {
            return dateFormatter.parseObject(text);
        }

        @Override
        public String valueToString(Object value) throws ParseException {
            if (value != null) {
                Calendar cal = (Calendar) value;
                return dateFormatter.format(cal.getTime());
            }
            return "";
        }
    }
}



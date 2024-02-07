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
 * A dialog used to delete a student from the database.
 *
 * @author Hodkova M.O.
 * @version 1.0
 */
public class StudentDeleteDialog extends JDialog {
    private JPanel pnlMain;
    private MainWindow mainWindow;
    private JPanel pnlMiddle;
    private JPanel pnlBottom;
    private Dimension dialogSize = new Dimension(350, 140);

    /**
     * Constructor for the delete student dialog.
     *
     * @param mainWindow the main window of the application
     */
    public StudentDeleteDialog(MainWindow mainWindow) {
        super(mainWindow, "Удалить студента");
        this.mainWindow = mainWindow;
        pnlMain = new JPanel(new BorderLayout(10, 10));

        pnlMain.add(createMiddlePanel(), BorderLayout.CENTER);
        pnlMain.add(createBottomPanel(), BorderLayout.SOUTH);

        setUpDialog();
    }

    /**
     * Sets up the necessary settings for the dialog.
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
     * Enables or disables dialog.
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
     * Creates the bottom panel of the dialog.
     *
     * @return the bottom panel of the dialog
     */
    private JPanel createBottomPanel() {
        pnlBottom = new JPanel(new BorderLayout());

        Dimension btnPreferredSize = new Dimension(110, 30);

        JPanel pnlDelete = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnDeleteGroup = new JButton("Да");
        btnDeleteGroup.setFont(new Font("Arial", Font.BOLD, 12));
        btnDeleteGroup.setBackground(Application.MAIN_COLOR);
        btnDeleteGroup.setPreferredSize(btnPreferredSize);
        pnlDelete.add(btnDeleteGroup);
        btnDeleteGroup.addActionListener(new DeleteStudentClickListener(this));

        JPanel pnlCancel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnCancel = new JButton("Нет");
        btnCancel.setFont(new Font("Arial", Font.BOLD, 12));
        btnCancel.setBackground(Application.MAIN_COLOR);
        btnCancel.setPreferredSize(btnPreferredSize);
        btnCancel.addActionListener(new CancelDialogListener(this));
        pnlCancel.add(btnCancel);

        pnlBottom.add(pnlDelete, BorderLayout.WEST);
        pnlBottom.add(pnlCancel, BorderLayout.EAST);
        return pnlBottom;
    }

    /**
     * Creates the middle panel of the dialog.
     *
     * @return the middle panel of the dialog
     */
    private JPanel createMiddlePanel() {
        pnlMiddle = new JPanel(new BorderLayout(10, 10));
        JPanel pnlLabelHolder = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel lblDeleteGroup = new JLabel("Вы действительно хотите удалить студента?");
        lblDeleteGroup.setFont(new Font("Arial", Font.BOLD, 12));
        pnlLabelHolder.setBorder(new EmptyBorder(15, 10, 0, 10));
        pnlLabelHolder.add(lblDeleteGroup);
        pnlMiddle.add(pnlLabelHolder, BorderLayout.CENTER);
        return pnlMiddle;
    }

    /**
     * ActionListener for the "Cancel" button.
     */
    class CancelDialogListener implements ActionListener {
        private JDialog dialog;

        public CancelDialogListener(JDialog dialog) {
            this.dialog = dialog;
        }

        public void actionPerformed(ActionEvent e) {
            dialog.setVisible(false);
        }
    }

    /**
     * ActionListener for the "Delete" button.
     */
    class DeleteStudentClickListener implements ActionListener {
        private JDialog dialog;

        public DeleteStudentClickListener(JDialog dialog) {
            this.dialog = dialog;
        }

        public void actionPerformed(ActionEvent e) {

            Student student = new Student(Tables.getStudentsDataModel().getValueAt(Tables.getTableStudents()
                    .getSelectedRow(), 1).toString(), DatabaseManager.getGroups()
                    .get(ComboBoxes.getComboBoxGroups().getSelectedIndex()));
            // Delete the student from the database
            DatabaseManager.deleteStudent(student);
            // Refresh the display of students in the main window
            Tables.refreshStudents();
            // Refresh the display of lectures in the main window
            Tables.outputLecturesToTable();
            // Update the appearance of the attendance table
            Tables.setTableAttendanceAppearance();

            dialog.setVisible(false);
        }
    }
}


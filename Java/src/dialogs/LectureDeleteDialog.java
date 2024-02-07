package dialogs;

import database.DatabaseManager;
import entities.Lecture;
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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * JDialog class used to delete Lecture
 *
 * @author Hodkova M.O.
 * @version 1.0
 */
public class LectureDeleteDialog extends JDialog {
    private final JPanel pnlMain;
    private final Dimension dialogSize = new Dimension(390, 140);

    /**
     * Constructor for LectureDeleteDialog
     *
     * @param mainWindow MainWindow instance
     */
    public LectureDeleteDialog(MainWindow mainWindow) {
        super(mainWindow, "Удалить лекцию");
        pnlMain = new JPanel(new BorderLayout(10, 10));

        pnlMain.add(createMiddlePanel(), BorderLayout.CENTER);
        pnlMain.add(createBottomPanel(), BorderLayout.SOUTH);

        setUpDialog();
    }

    /**
     * sets necessary settings for group delete dialog
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
     * @param active boolean value to enable/disable the frame
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
     * @return a JPanel instance
     */
    private JPanel createBottomPanel() {
        JPanel pnlBottom = new JPanel(new BorderLayout());

        Dimension btnPreferredSize = new Dimension(110, 30);

        JPanel pnlDelete = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnDeleteGroup = new JButton("Да");
        btnDeleteGroup.setFont(new Font("Arial", Font.BOLD, 12));
        btnDeleteGroup.setBackground(Application.MAIN_COLOR);
        btnDeleteGroup.setPreferredSize(btnPreferredSize);
        pnlDelete.add(btnDeleteGroup);
        btnDeleteGroup.addActionListener(new DeleteLectureClickListener(this));

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
     * Creates necessary UI objects for middle panel.
     *
     * @return a JPanel instance
     */
    private JPanel createMiddlePanel() {
        JPanel pnlMiddle = new JPanel(new BorderLayout(10, 10));
        JPanel pnlLabelHolder = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel lblSubject = new JLabel(ComboBoxes.getSelectedSubject().getSubjectName());

        String date = Tables.getTableAttendance().getColumnModel().getColumn(Tables.getClickedIndex()).getHeaderValue().toString();
        JLabel lblDateOfLecture = new JLabel(date);
        JLabel lblDeleteGroup = new JLabel("Вы действительно хотите удалить лекцию "+ lblSubject.getText() + " " +
                lblDateOfLecture.getText() + "?");
        lblDeleteGroup.setFont(new Font("Arial", Font.BOLD, 12));
        pnlLabelHolder.setBorder(new EmptyBorder(15, 10, 0, 10));
        pnlLabelHolder.add(lblDeleteGroup);
        pnlMiddle.add(pnlLabelHolder, BorderLayout.CENTER);
        return pnlMiddle;
    }

    /**
     * ActionListener for Cancel button
     */
    static class CancelDialogListener implements ActionListener {
        private final JDialog dialog;
        public CancelDialogListener(JDialog dialog) {
            this.dialog = dialog;
        }

        public void actionPerformed(ActionEvent e) {
            dialog.setVisible(false);
        }
    }

    /**
     * ActionListener for Delete button
     */
    static class DeleteLectureClickListener implements ActionListener {
        private final JDialog dialog;
        public DeleteLectureClickListener(JDialog dialog) {
            this.dialog = dialog;
        }

        public void actionPerformed(ActionEvent e) {
            DateFormat formatter = new SimpleDateFormat("dd.MM.yy");
            String date = Tables.getTableAttendance().getColumnModel().getColumn(Tables.getClickedIndex()).getHeaderValue().toString();

            try {
                DatabaseManager.deleteLecture(new Lecture(ComboBoxes.getSelectedSubject(),formatter.parse(date)));
                Tables.outputLecturesToTable();
                Tables.setTableAttendanceAppearance();
            } catch (ParseException ex) {
                throw new RuntimeException(ex);
            }

            dialog.setVisible(false);
        }

    }
}


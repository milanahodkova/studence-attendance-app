package dialogs;

import database.DatabaseManager;
import gui.Application;
import gui.ComboBoxes;
import gui.MainWindow;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * JDialog class used to delete a subject.
 *
 * @author Hodkova M.O.
 * @version 1.0
 */
public class SubjectDeleteDialog extends JDialog {
    private JPanel pnlMain;
    private MainWindow mainWindow;
    private JPanel pnlMiddle;
    private JPanel pnlBottom;
    private static final Dimension DIALOG_SIZE = new Dimension(350, 140);

    /**
     * Creates a new SubjectDeleteDialog.
     *
     * @param mainWindow the parent window
     */
    public SubjectDeleteDialog(MainWindow mainWindow) {
        super(mainWindow, "Удалить предмет");
        this.mainWindow = mainWindow;
        pnlMain = new JPanel(new BorderLayout(10, 10));

        pnlMain.add(createMiddlePanel(), BorderLayout.CENTER);
        pnlMain.add(createBottomPanel(), BorderLayout.SOUTH);

        setUpDialog();
    }

    /**
     * Sets up the necessary settings for this dialog.
     */
    private void setUpDialog() {
        setLayout(new BorderLayout());
        setSize(DIALOG_SIZE);
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
     * Enables or disables this dialog.
     *
     * @param active true to enable, false to disable
     */
    public void enableFrame(boolean active) {
        if (active) {
            setSize(DIALOG_SIZE);
            setLocationRelativeTo(getOwner());
        }
        setVisible(active);
    }

    /**
     * Creates the necessary UI objects for the bottom panel.
     *
     * @return the bottom panel
     */
    private JPanel createBottomPanel() {
        pnlBottom = new JPanel(new BorderLayout());

        Dimension btnPreferredSize = new Dimension(110, 30);

        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnDeleteSubject = new JButton("Да");
        btnDeleteSubject.setFont(new Font("Arial", Font.BOLD, 12));
        btnDeleteSubject.setBackground(Application.MAIN_COLOR);
        btnDeleteSubject.setPreferredSize(btnPreferredSize);
        pnlButtons.add(btnDeleteSubject);
        btnDeleteSubject.addActionListener(new DeleteSubjectButtonListener(this));

        JButton btnCancel = new JButton("Нет");
        btnCancel.setFont(new Font("Arial", Font.BOLD, 12));
        btnCancel.setBackground(Application.MAIN_COLOR);
        btnCancel.setPreferredSize(btnPreferredSize);
        btnCancel.addActionListener(new CancelDialogListener(this));
        pnlButtons.add(btnCancel);

        pnlBottom.add(pnlButtons, BorderLayout.CENTER);
        return pnlBottom;
    }

    /**
     * Creates the necessary UI objects for the middle panel.
     *
     * @return the middle panel
     */
    private JPanel createMiddlePanel() {
        pnlMiddle = new JPanel(new BorderLayout(10, 10));
        JPanel pnlLabelHolder = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel lblDeleteSubject = new JLabel("Вы действительно хотите удалить предмет?");
        lblDeleteSubject.setFont(new Font("Arial", Font.BOLD, 12));
        pnlLabelHolder.setBorder(new EmptyBorder(15, 10, 0, 10));
        pnlLabelHolder.add(lblDeleteSubject);
        pnlMiddle.add(pnlLabelHolder, BorderLayout.CENTER);
        return pnlMiddle;
    }

    /**
     * ActionListener for the "Нет" button.
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
     * ActionListener for the "Да" button.
     */
    class DeleteSubjectButtonListener implements ActionListener {
        private JDialog dialog;
        public DeleteSubjectButtonListener(JDialog dialog) {
            this.dialog = dialog;
        }

        public void actionPerformed(ActionEvent e){
            DatabaseManager.deleteSubject(ComboBoxes.getSelectedSubject());
            ComboBoxes.getComboBoxSubjects().removeItemAt(ComboBoxes.getComboBoxSubjects().getSelectedIndex());
            System.out.println(ComboBoxes.getComboBoxSubjects().getSelectedIndex());

            dialog.setVisible(false);
        }
    }
}

package dialogs;

import database.DatabaseManager;
import entities.Subject;
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
 * Dialog for adding a new subject.
 *
 * @author M.O. Hodkova
 * @version 1.0
 */
public class SubjectAddDialog extends JDialog {
    private JPanel pnlMain;
    private MainWindow mainWindow;
    private JPanel pnlMiddle;
    private JPanel pnlBottom;
    private JTextField txtSubject = new JTextField();
    private Dimension dialogSize = new Dimension(400, 140);

    /**
     * Creates a new SubjectAddDialog instance.
     *
     * @param mainWindow the parent window of the dialog.
     */
    public SubjectAddDialog(MainWindow mainWindow) {
        super(mainWindow, "Добавить предмет:");
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
            /**
             * Disables the frame when the window is closing.
             *
             * @param e the window event.
             */
            public void windowClosing(WindowEvent e) {
                enableFrame(false);
            }
        });
    }

    /**
     * Enables or disables the JDialog instance.
     *
     * @param active true to enable the dialog, false to disable it.
     */
    public void enableFrame(boolean active) {
        if (active) {
            setSize(dialogSize);
            setLocationRelativeTo(getOwner());
        } else {
            txtSubject.setText("");
        }

        setVisible(active);
    }

    /**
     * Creates the necessary UI objects for the bottom panel.
     *
     * @return the bottom panel.
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
        btnAddGroup.addActionListener(new AddSubjectClickListener(this));

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
     * Creates the middle panel of the dialog.
     *
     * @return the middle panel.
     */
    private JPanel createMiddlePanel() {
        pnlMiddle = new JPanel(new BorderLayout(10, 10));
        JPanel pnlFieldHolder = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel lblAddGroup = new JLabel("Введите название предмета: ");
        lblAddGroup.setFont(new Font("Arial", Font.BOLD, 12));
        txtSubject.setPreferredSize(new Dimension(170, 25));
        txtSubject.setFont(new Font("Arial", Font.BOLD, 14));
        pnlFieldHolder.setBorder(new EmptyBorder(15, 10, 0, 10));
        pnlFieldHolder.add(lblAddGroup);
        pnlFieldHolder.add(txtSubject);
        pnlMiddle.add(pnlFieldHolder, BorderLayout.CENTER);
        return pnlMiddle;
    }

    /**
     * Cancel AddGroupDialog listener.
     */
    class CancelDialogListener implements ActionListener {
        private JDialog dialog;

        /**
         * Creates a new CancelDialogListener instance.
         *
         * @param dialog the dialog to be canceled.
         */
        public CancelDialogListener(JDialog dialog) {
            this.dialog = dialog;
        }

        /**
         * Cancels the dialog.
         *
         * @param e the action event.
         */
        public void actionPerformed(ActionEvent e) {
            txtSubject.setText("");
            dialog.setVisible(false);
        }
    }

    /**
     * AddSubject click listener.
     */
    class AddSubjectClickListener implements ActionListener {
        private JDialog dialog;

        /**
         * Creates a new AddSubjectClickListener instance.
         *
         * @param dialog the dialog to add the subject to.
         */
        public AddSubjectClickListener(JDialog dialog) {
            this.dialog = dialog;
        }

        /**
         * Adds a new subject to the database.
         *
         * @param e the action event.
         */
        public void actionPerformed(ActionEvent e) {
            if (txtSubject.getText().equals("")) {
                JOptionPane.showMessageDialog(null,
                        "Заполните данные!",
                        "Ошибка операции", JOptionPane.ERROR_MESSAGE);
                return;
            }
            for (Subject item : DatabaseManager.getSubjects()) {
                {
                    if (item.getSubjectName().equals(txtSubject.getText())) {
                        JOptionPane.showMessageDialog(null,
                                "Такой учебный предмет уже добавлен!",
                                "Ошибка операции", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
            }
            Subject newSubject = new Subject(txtSubject.getText());
            DatabaseManager.addSubject(newSubject);
            ComboBoxes.getComboBoxSubjects().addItem(newSubject.getSubjectName());
            JOptionPane.showMessageDialog(null, txtSubject.getText() + " добавлен!",
                    "Результат", JOptionPane.INFORMATION_MESSAGE);
            txtSubject.setText("");
            dialog.setVisible(false);
        }
    }
}

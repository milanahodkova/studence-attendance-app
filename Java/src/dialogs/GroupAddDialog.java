package dialogs;

import database.DatabaseManager;
import entities.Group;
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
 * JDialog class used to input and add Group.
 *
 * @author Hodkova M.O.
 * @version 1.0
 */
public class GroupAddDialog extends JDialog {
    private JPanel pnlMain;
    private MainWindow mainWindow;
    private JPanel pnlMiddle;
    private JPanel pnlBottom;
    private JTextField txtGroup = new JTextField();
    private Dimension dialogSize = new Dimension(350, 140);

    /**
     * Constructor for GroupAddDialog.
     *
     * @param mainWindow the parent window for the dialog.
     */
    public GroupAddDialog(MainWindow mainWindow) {
        super(mainWindow, "Добавить группу");
        this.mainWindow = mainWindow;
        pnlMain = new JPanel(new BorderLayout(10, 10));

        pnlMain.add(createMiddlePanel(), BorderLayout.CENTER);
        pnlMain.add(createBottomPanel(), BorderLayout.SOUTH);

        setUpDialog();
    }

    /**
     * Sets UI necessary settings for group add dialog.
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
     * Enables or disables the dialog.
     *
     * @param active flag indicating whether to enable the dialog.
     */
    public void enableFrame(boolean active) {
        if (active) {
            setSize(dialogSize);
            setLocationRelativeTo(getOwner());
        } else {
            txtGroup.setText("");
        }

        setVisible(active);
    }

    /**
     * Creates necessary UI objects for bottom panel.
     *
     * @return the bottom panel.
     */
    private JPanel createBottomPanel() {
        pnlBottom = new JPanel(new BorderLayout());

        Dimension btnPrefferedSize = new Dimension(110, 30);

        JPanel pnlAddGroup = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnAddGroup = new JButton("Добавить");
        btnAddGroup.setFont(new Font("Arial", Font.BOLD, 12));
        btnAddGroup.setBackground(Application.MAIN_COLOR);
        btnAddGroup.setPreferredSize(btnPrefferedSize);
        pnlAddGroup.add(btnAddGroup);
        btnAddGroup.addActionListener(new AddGroupClickListener(this));

        JPanel pnlCancel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnCancel = new JButton("Отмена");
        btnCancel.setFont(new Font("Arial", Font.BOLD, 12));
        btnCancel.setBackground(Application.MAIN_COLOR);
        btnCancel.setPreferredSize(btnPrefferedSize);
        btnCancel.addActionListener(new CancelDialogListener(this));
        pnlCancel.add(btnCancel);

        pnlBottom.add(pnlAddGroup, BorderLayout.WEST);
        pnlBottom.add(pnlCancel, BorderLayout.EAST);
        return pnlBottom;
    }

    /**
     * Creates necessary UI objects for middle panel.
     *
     * @return the middle panel.
     */
    private JPanel createMiddlePanel() {
        pnlMiddle = new JPanel(new BorderLayout(10, 10));
        JPanel pnlFieldHolder = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel lblAddGroup = new JLabel("Введите номер группы: ");
        lblAddGroup.setFont(new Font("Arial", Font.BOLD, 12));
        txtGroup.setPreferredSize(new Dimension(100, 25));
        txtGroup.setFont(new Font("Arial", Font.BOLD, 14));
        pnlFieldHolder.setBorder(new EmptyBorder(15, 10, 0, 10));
        pnlFieldHolder.add(lblAddGroup);
        pnlFieldHolder.add(txtGroup);
        pnlMiddle.add(pnlFieldHolder, BorderLayout.CENTER);
        return pnlMiddle;
    }

    /**
     * ActionListener for cancel button in GroupAddDialog.
     */
    class CancelDialogListener implements ActionListener {
        private JDialog dialog;

        public CancelDialogListener(JDialog dialog) {
            this.dialog = dialog;
        }

        public void actionPerformed(ActionEvent e) {
            txtGroup.setText("");
            dialog.setVisible(false);
        }
    }

    /**
     * ActionListener for add group button in GroupAddDialog.
     */
    class AddGroupClickListener implements ActionListener {
        private JDialog dialog;

        public AddGroupClickListener(JDialog dialog) {
            this.dialog = dialog;
        }

        /**
         * Action performed when the add group button is clicked.
         * Validates input, adds the group to the database and updates the main window.
         *
         * @param e the action event.
         */
        public void actionPerformed(ActionEvent e) {
            if (txtGroup.getText().equals("")) {
                JOptionPane.showMessageDialog(null,
                        "Заполните данные!",
                        "Ошибка операции", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                for (Group item : DatabaseManager.getGroups()) {
                    {
                        if (item.getGroupNumber().equals(txtGroup.getText())) {
                            JOptionPane.showMessageDialog(null,
                                    "Группа с таким номером уже добавлена!",
                                    "Ошибка операции", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }
                }
                try {
                    Integer.parseInt(txtGroup.getText());
                } catch (Exception exception) {
                    JOptionPane.showMessageDialog(null,
                            "Проверьте правильность ввода данных",
                            "Ошибка операции", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Group newGroup = new Group(txtGroup.getText());
                DatabaseManager.addGroup(newGroup);
                ComboBoxes.getComboBoxGroups().addItem(newGroup.getGroupNumber());

                JOptionPane.showMessageDialog(null, "Группа " + txtGroup.getText() + " добавлена!",
                        "Результат", JOptionPane.INFORMATION_MESSAGE);
                txtGroup.setText("");
                dialog.setVisible(false);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null,
                        "Ошибка операции: " + ex.getMessage(),
                        "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

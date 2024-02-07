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
 * JDialog class used to delete a group.
 *
 * @author Hodkova M.O.
 * @version 1.0
 */
public class GroupDeleteDialog extends JDialog {
    private JPanel pnlMain;
    private MainWindow mainWindow;
    private JPanel pnlMiddle;
    private JPanel pnlBottom;
    private Dimension dialogSize = new Dimension(350, 140);

    /**
     * Constructor for GroupDeleteDialog.
     *
     * @param mainWindow The parent window of the dialog.
     */
    public GroupDeleteDialog(MainWindow mainWindow) {
        super(mainWindow, "Удалить группу");
        this.mainWindow = mainWindow;
        pnlMain = new JPanel(new BorderLayout(10, 10));

        pnlMain.add(createMiddlePanel(), BorderLayout.CENTER);
        pnlMain.add(createBottomPanel(), BorderLayout.SOUTH);

        setUpDialog();
    }

    /**
     * Sets up necessary settings for the group delete dialog.
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
     * Enables or disables the JDialog instance.
     *
     * @param active Whether the dialog enabled or disabled.
     */
    public void enableFrame(boolean active) {
        if (active) {
            setSize(dialogSize);
            setLocationRelativeTo(getOwner());
        }
        setVisible(active);
    }

    /**
     * Creates necessary UI objects for the bottom panel.
     *
     * @return The bottom panel containing UI objects.
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
        btnDeleteGroup.addActionListener(new DeleteGroupClickListener(this));

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
     * @return The middle panel containing UI objects.
     */
    private JPanel createMiddlePanel() {
        pnlMiddle = new JPanel(new BorderLayout(10, 10));
        JPanel pnlLabelHolder = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel lblDeleteGroup = new JLabel("Вы действительно хотите удалить группу?");
        lblDeleteGroup.setFont(new Font("Arial", Font.BOLD, 12));
        pnlLabelHolder.setBorder(new EmptyBorder(15, 10, 0, 10));
        pnlLabelHolder.add(lblDeleteGroup);
        pnlMiddle.add(pnlLabelHolder, BorderLayout.CENTER);
        return pnlMiddle;
    }

    /**
     * ActionListener implementation for the cancel button in GroupDeleteDialog.
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
     * ActionListener implementation for the delete button in GroupDeleteDialog.
     */
    class DeleteGroupClickListener implements ActionListener {
        private JDialog dialog;
        public DeleteGroupClickListener(JDialog dialog) {
            this.dialog = dialog;
        }

        public void actionPerformed(ActionEvent e){
            DatabaseManager.deleteGroup(ComboBoxes.getSelectedGroup());
            ComboBoxes.getComboBoxGroups().removeItemAt(ComboBoxes.getComboBoxGroups().getSelectedIndex());
            System.out.println(ComboBoxes.getComboBoxGroups().getSelectedIndex());

            dialog.setVisible(false);
        }
    }
}


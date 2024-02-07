package gui;

import database.DatabaseManager;
import entities.Group;
import entities.Subject;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class ComboBoxes {
    private static JComboBox cmbGroups;
    private static JComboBox cmbSubjects;
    private static JComboBox cmbAttendance;
    public ComboBoxes(){
    }
    public static JComboBox createCmbGroups(){
        // Create a drop-down list for selecting a group
        ArrayList<Group> groups = new ArrayList<>(DatabaseManager.getGroups());
        cmbGroups = new JComboBox(groups.toArray());
        cmbGroups.setFont(new Font("Arial", Font.PLAIN, 12));
        cmbGroups.setPreferredSize(new Dimension(120, 25));
        cmbGroups.setToolTipText("Выбрать группу");
        return cmbGroups;
    }

    public static JComboBox createCmbSubjects(){
        // Create a drop-down list for selecting a subject
        ArrayList<Subject> subjects = new ArrayList<>(DatabaseManager.getSubjects());
        cmbSubjects = new JComboBox(subjects.toArray());
        cmbSubjects.setFont(new Font("Arial", Font.PLAIN, 12));
        cmbSubjects.setPreferredSize(new Dimension(120, 25));
        cmbSubjects.setToolTipText("Выбрать предмет");
        return cmbSubjects;
    }

    public static JComboBox createAndPopulateCmbAttendance() {
        cmbAttendance = new JComboBox();
        cmbAttendance.addItem(" ");
        cmbAttendance.addItem("2");
        cmbAttendance.addItem("1");
        return cmbAttendance;
    }

    public static void setupContextMenuForComboBox(JComboBox cmb, JPopupMenu pmnGroup) {
        cmb.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    int selectedGroupIndex = cmb.getSelectedIndex();
                    pmnGroup.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
    }

    public static void setupActionListenerForComboBox(JComboBox cmb, DefaultTableModel tableModel) {
        cmb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedGroupIndex = cmb.getSelectedIndex();
                Tables.addStudentsToTableStudents(tableModel, selectedGroupIndex);
                Tables.outputLecturesToTable();
                Tables.setTableAttendanceAppearance();
            }
        });
    }
    public static Group getSelectedGroup() {
        return DatabaseManager.getGroups().get(cmbGroups.getSelectedIndex());
    }
    public static Subject getSelectedSubject() {
        return DatabaseManager.getSubjects().get(cmbSubjects.getSelectedIndex());
    }
    public static JComboBox getComboBoxGroups() {
        return cmbGroups;
    }
    public static JComboBox getComboBoxSubjects() {
        return cmbSubjects;
    }
    public static JComboBox getCmbAttendance() {
        return cmbAttendance;
    }

    public void setCmbAttendance(JComboBox cmbAttendance) {
        this.cmbAttendance = cmbAttendance;
    }
}

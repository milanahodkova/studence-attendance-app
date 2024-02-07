package gui;

import dialogs.*;
import excel.ExcelReader;
import excel.JTableToExcel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * The class represents an event handler for the components in the {@link MainWindow}.
 * It contains listeners for all the components used in the main window.
 *
 * @author M.O. Hodkova
 * @version 1.0
 */
public class MainWindowEventHandler {
    private final MainWindow mainWindow;

    /**
     * Constructs an instance of GUI.MainWindowEventHandler with the specified main window.
     *
     * @param mainWindow The main window to handle events for
     */
    public MainWindowEventHandler(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }

    /**
     * Adds listeners for most of the components in the main window.
     */
    public void addListeners() {
        MainPanel.getBtnDeleteStudent().addActionListener(new ButtonDeleteStudentListener(mainWindow));
        MainPanel.getBtnAddStudent().addActionListener(new ButtonAddStudentListener(mainWindow));
        MainPanel.getBtnAboutProgram().addActionListener(new AboutProgramButtonListener(mainWindow));
        mainWindow.getMniAboutProgram().addActionListener(new AboutProgramButtonListener(mainWindow));
        MainPanel.getBtnExit().addActionListener(new ButtonExitListener());
        MainPanel.getMniDeleteSubject().addActionListener(new ButtonDeleteSubjectListener(mainWindow));
        MainPanel.getMniDeleteGroup().addActionListener(new ButtonDeleteGroupListener(mainWindow));
        MainPanel.getMniAddGroup().addActionListener(new ButtonAddGroupListener(mainWindow));
        MainPanel.getMniAddSubject().addActionListener(new ButtonAddSubjectListener(mainWindow));
        Tables.getMniDeleteLecture().addActionListener(new ButtonDeleteLectureListener(mainWindow));
        mainWindow.getMniSave().addActionListener(new SaveFileListener(mainWindow));
        MainPanel.getBtnSaveToFile().addActionListener(new SaveFileListener(mainWindow));
        mainWindow.getMniLoad().addActionListener(new LoadFileListener(mainWindow));
    }

    /**
     * Listens for clicks on the "Delete Student" button and displays a dialog for deleting the selected student.
     */
    class ButtonDeleteStudentListener implements ActionListener {
        private MainWindow owner;
        private StudentDeleteDialog studentDeleteDialog;

        public ButtonDeleteStudentListener(MainWindow owner) {
            this.owner = owner;
        }

        public void actionPerformed(ActionEvent e) {
            if (Tables.getTableStudents().getSelectedRow() != -1) {
                if (studentDeleteDialog == null) {
                    studentDeleteDialog = new StudentDeleteDialog(owner);
                }
                studentDeleteDialog.enableFrame(true);
            }
        }
    }

    /**
     * Listens for clicks on the "Add Student" button and displays a dialog for adding a new student.
     */
    class ButtonAddStudentListener implements ActionListener {
        private MainWindow owner;
        private StudentAddDialog studentAddDialog;

        public ButtonAddStudentListener(MainWindow owner) {
            this.owner = owner;
        }

        public void actionPerformed(ActionEvent e) {
            if (studentAddDialog == null) {
                studentAddDialog = new StudentAddDialog(owner);
            }
            studentAddDialog.enableFrame(true);
        }
    }

    /**
     * Listens for clicks on the "About GUI.Application" button and displays a dialog
     * with information about the application.
     */
    class AboutProgramButtonListener implements ActionListener {
        private MainWindow owner;
        private AboutProgramWindow aboutProgramWindow;

        public AboutProgramButtonListener(MainWindow owner) {
            this.owner = owner;
        }

        public void actionPerformed(ActionEvent e) {
            if (aboutProgramWindow == null) {
                aboutProgramWindow = new AboutProgramWindow(owner);
            }
            aboutProgramWindow.enableFrame(true);
        }
    }

    /**
     * Listens for clicks on the "Exit" button and performs actions to close the main window.
     */
    class ButtonExitListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            mainWindow.doActionsAndCloseWindow();
        }
    }

    /**
     * Listens for clicks on the "Delete Subject" button and displays a dialog for deleting the selected subject.
     */
    class ButtonDeleteSubjectListener implements ActionListener {
        private MainWindow owner;
        private SubjectDeleteDialog subjectDeleteDialog;

        public ButtonDeleteSubjectListener(MainWindow owner) {
            this.owner = owner;
        }

        public void actionPerformed(ActionEvent e) {
            if (subjectDeleteDialog == null) {
                subjectDeleteDialog = new SubjectDeleteDialog(owner);
            }
            subjectDeleteDialog.enableFrame(true);
        }
    }

    /**
     * Listens for clicks on the "Delete Group" button and displays a dialog for deleting the selected group.
     */
    class ButtonDeleteGroupListener implements ActionListener {
        private MainWindow owner;
        private GroupDeleteDialog groupDeleteDialog;

        public ButtonDeleteGroupListener(MainWindow owner) {
            this.owner = owner;
        }

        public void actionPerformed(ActionEvent e) {
            if (groupDeleteDialog == null) {
                groupDeleteDialog = new GroupDeleteDialog(owner);
            }
            groupDeleteDialog.enableFrame(true);
        }
    }

    /**
     * Listens for clicks on the "Add Group" button and displays a dialog for adding a new group.
     */
    class ButtonAddGroupListener implements ActionListener {
        private MainWindow owner;
        private GroupAddDialog groupAddDialog;

        public ButtonAddGroupListener(MainWindow owner) {
            this.owner = owner;
        }

        public void actionPerformed(ActionEvent e) {
            if (groupAddDialog == null) {
                groupAddDialog = new GroupAddDialog(owner);
            }
            groupAddDialog.enableFrame(true);
        }
    }

    /**
     * Listens for clicks on the "Add Subject" button and displays a dialog for adding a new subject.
     */
    class ButtonAddSubjectListener implements ActionListener {
        private MainWindow owner;
        private SubjectAddDialog subjectAddDialog;

        public ButtonAddSubjectListener(MainWindow owner) {
            this.owner = owner;
        }

        public void actionPerformed(ActionEvent e) {
            if (subjectAddDialog == null) {
                subjectAddDialog = new SubjectAddDialog(owner);
            }
            subjectAddDialog.enableFrame(true);
        }
    }

    /**
     * Listens for clicks on the "Delete Lecture" button and displays a dialog for deleting the selected lecture.
     */
    class ButtonDeleteLectureListener implements ActionListener {
        private MainWindow owner;
        private LectureDeleteDialog lectureDeleteDialog;

        public ButtonDeleteLectureListener(MainWindow owner) {
            this.owner = owner;
        }

        public void actionPerformed(ActionEvent e) {
            if (lectureDeleteDialog == null) {
                lectureDeleteDialog = new LectureDeleteDialog(owner);
            }
            lectureDeleteDialog.enableFrame(true);
        }
    }

    /**
     * Listens for clicks on the "Save" button and saves the table data to an Excel file.
     */
    private class SaveFileListener implements ActionListener {
        private MainWindow owner;

        public SaveFileListener(MainWindow owner) {
            this.owner = owner;
        }

        public void actionPerformed(ActionEvent e) {
            new JTableToExcel(owner);
        }

    }

    /**
     * Listens for clicks on the "Load" button and loads table data from an Excel file.
     */
    private class LoadFileListener implements ActionListener {
        private MainWindow owner;

        public LoadFileListener(MainWindow owner) {
            this.owner = owner;
        }

        public void actionPerformed(ActionEvent e) {
            new ExcelReader(owner);
        }
    }

}

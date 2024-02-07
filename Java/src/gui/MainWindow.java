package gui;

import database.DatabaseManager;
import dialogs.LectureAddDialog;
import dialogs.StudentInfoDialog;
import entities.*;

import javax.swing.*;
import javax.swing.border.Border;
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

/**
 * Main window of GUI.Application
 *
 * @author M.O. Hodkova
 * @version 1.0
 */
public class MainWindow extends JFrame {
    private JMenuItem mniAboutProgram;
    private JMenuItem mniSave;
    private JMenuItem mniLoad;

    /**
     * Constructor for the main window of the application.
     */
    public MainWindow() {
        super(Application.NAME);
        //createMainPanels();
        MainPanel mainPanel = new MainPanel(this);
        Tables tables = new Tables(this);
        //create components
        mainPanel.createMainPanels();
        mainPanel.createCenterUpComponents();
        mainPanel.createBottomComponents();
        mainPanel.createCenterComponents();
        //add panels to main panel
        mainPanel.addPanelsToMainPanel();

        setUpFrame(mainPanel.getPnlMain());
        MainWindowEventHandler eventHandler = new MainWindowEventHandler(this);
        eventHandler.addListeners();
    }

    /**
     * Sets up the main window and adds the main panel to it.
     *
     * @param mainPanel The main panel of the window.
     */
    private void setUpFrame(JPanel mainPanel) {
        setLayout(new BorderLayout());
        add(mainPanel);
        addMenuBar();
        setSize(new Dimension((Application.WINDOW_SIZE)));
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                doActionsAndCloseWindow();
            }
        });
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Adds the menu bar to the main window.
     */
    private void addMenuBar() {
        JMenuBar mnuMain = new JMenuBar();
        mnuMain.add(createFileMenu());
        mnuMain.add(createInformationMenu());
        setJMenuBar(mnuMain);
    }

    /**
     * Creates the "File" menu.
     *
     * @return the "File" menu
     */
    private JMenu createFileMenu() {
        JMenu mnuFile = new JMenu("File");
        mnuFile.setFont(new Font("Arial", Font.PLAIN, 12));
        mniSave = new JMenuItem("Save");
        mniSave.setFont(new Font("Arial", Font.PLAIN, 12));

        mniLoad = new JMenuItem("Load");
        mniLoad.setFont(new Font("Arial", Font.PLAIN, 12));

        JMenuItem mniPrint = new JMenuItem("Print");
        mniPrint.setFont(new Font("Arial", Font.PLAIN, 12));
        mniPrint.addActionListener(new Tables.PrintTableListener());
        JMenuItem mniExit = new JMenuItem("Exit");
        mniExit.setFont(new Font("Arial", Font.PLAIN, 12));
        mniExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doActionsAndCloseWindow();
            }
        });
        mnuFile.add(mniSave);
        mnuFile.addSeparator();
        mnuFile.add(mniLoad);
        mnuFile.addSeparator();
        mnuFile.add(mniPrint);
        mnuFile.addSeparator();
        mnuFile.add(mniExit);
        return mnuFile;
    }

    /**
     * Creates the "Information" menu.
     *
     * @return The "Information" menu.
     */
    private JMenu createInformationMenu() {
        JMenu mnuInformation = new JMenu("Information");
        mnuInformation.setFont(new Font("Arial", Font.PLAIN, 12));

        mniAboutProgram = new JMenuItem("About program");
        mniAboutProgram.setFont(new Font("Arial", Font.PLAIN, 12));
        // mniAboutProgram.addActionListener(new AboutProgramButtonListener(this));

        mnuInformation.add(mniAboutProgram);
        return mnuInformation;
    }

    /**
     * Displays a confirmation dialog and closes the window if the user confirms the action.
     */
    void doActionsAndCloseWindow() {

        Object[] options = {"Да", "Нет!"};
        int n = JOptionPane
                .showOptionDialog((Component) null, "Вы действительно хотите выйти?",
                        "Подтверждение", JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, options,
                        null);
        //cases are ordered by options
        switch (n) {
            case 0:
                dispose();
                break;
            case 1:
                break;
        }
    }

  public JMenuItem getMniAboutProgram() {
      return mniAboutProgram;
  }

    public JMenuItem getMniSave() {
        return mniSave;
    }
    public JMenuItem getMniLoad() {
        return mniLoad;
    }
}



package gui;

import database.DatabaseManager;

import java.awt.*;
import java.sql.*;

/**
 * A utility class that stores constants for the student attendance log application.
 *
 * @author M.O. Hodkova
 * @version 1.0
 */
public class Application {
    /**
     * The size of the main application window.
     */
    public final static Dimension WINDOW_SIZE = new Dimension(1250, 790);

    /**
     * The name of the application.
     */
    public final static String NAME = "Student Attendance Log";

    /**
     * The version of the application.
     */
    public final static String VERSION = "1.0.0.2023";

    /**
     * The main color of the application.
     */
    public final static Color MAIN_COLOR = new Color(201, 202, 255);

    /**
     * The timeout for the splash screen in seconds.
     */
    public static final float SPLASH_SCREEN_AUTO_CLOSE_TIME = 60f;

    /**
     * This class should not be instantiated.
     *
     * @throws IllegalStateException if an attempt is made to instantiate this class.
     */
    private Application() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * The entry point for the application.
     *
     * @param args the command line arguments
     * @throws SQLException           if there is a database error
     * @throws ClassNotFoundException if the database driver class is not found
     */
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        DatabaseManager.setUpConnection();
        new MainWindow();
    }
}
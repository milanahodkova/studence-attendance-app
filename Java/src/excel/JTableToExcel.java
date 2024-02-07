package excel;

import gui.MainWindow;
import gui.Tables;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Class for exporting data from a JTable to an Excel file.
 *
 * @author M.O. Hodkova
 * @version 1.0
 */
public class JTableToExcel {
    private MainWindow mainWindow;

    /**
     * Creates a new instance of the JTableToExcel class.
     *
     * @param mainWindow the main window of the application
     */
    public JTableToExcel(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        export();
    }

    /**
     * Exports data from a JTable to an Excel file.
     */
    private void export() {
        try {
            JFileChooser jFileChooser = new JFileChooser();
            jFileChooser.showSaveDialog(mainWindow);
            File selectedFile = jFileChooser.getSelectedFile();

            if (selectedFile != null) {
                selectedFile = new File(selectedFile.toString() + ".xlsx");
                Workbook workbook = new XSSFWorkbook();
                Sheet sheet = workbook.createSheet("Student Attendance");
                sheet.setColumnWidth(1, 35 * 256);
                CellStyle centerStyle = workbook.createCellStyle();
                centerStyle.setAlignment(HorizontalAlignment.CENTER);
                Row rowCol = sheet.createRow(0);
                for (int i = 0; i < Tables.getTableStudents().getColumnCount(); i++) {
                    Cell cell = rowCol.createCell(i);
                    cell.setCellValue(Tables.getTableStudents().getColumnName(i));
                    if (i == 0) {
                        cell.setCellStyle(centerStyle);
                    }
                }
                for (int i = 0; i < Tables.getTableAttendance().getColumnCount(); i++) {
                    if (i != Tables.getTableAttendance().getColumnCount() - 2) {
                        Cell cell = rowCol.createCell(i+Tables.getTableStudents().getColumnCount());
                        cell.setCellValue(Tables.getTableAttendance().getColumnName(i));
                    }
                }
                for (int j = 0; j < Tables.getTableAttendance().getRowCount(); j++) {
                    Row row = sheet.createRow(j + 1);
                    for (int i = 0; i < Tables.getTableStudents().getColumnCount(); i++) {
                        Cell cell = row.createCell(i);
                        if (Tables.getTableStudents().getValueAt(j, i) != null) {
                            String value = Tables.getTableStudents().getValueAt(j, i).toString();
                            if (isDouble(value)) {
                                cell.setCellValue(Double.parseDouble(value));
                            } else {
                                cell.setCellValue(value);
                            }
                        }
                        if (i == 0) {
                            cell.setCellStyle(centerStyle);
                        }
                    }
                    for (int k = 0; k < Tables.getTableAttendance().getColumnCount(); k++) {
                        Cell cell = row.createCell(k+Tables.getTableStudents().getColumnCount());
                        if (Tables.getTableAttendance().getValueAt(j, k) != null && k != Tables.getTableAttendance().getColumnCount() - 2) {
                            if (isDouble(Tables.getTableAttendance().getValueAt(j, k).toString())) {
                                cell.setCellValue(Double.parseDouble(Tables.getTableAttendance().getValueAt(j, k).toString()));
                            } else {
                                cell.setCellValue(Tables.getTableAttendance().getValueAt(j, k).toString());
                            }
                            cell.setCellStyle(centerStyle);
                        }
                    }
                }
                FileOutputStream out = new FileOutputStream(new File(selectedFile.toString()));
                workbook.write(out);
                workbook.close();
                out.close();
                openFile(selectedFile.toString());
            }
        } catch (FileNotFoundException e) {
            System.out.println(e);
        } catch (IOException io) {
            System.out.println(io);
        }
    }

    /**
     * Opens a file on the computer at the specified path.
     *
     * @param filePath the path to the file
     */
    public void openFile(String filePath) {
        try {
            File path = new File(filePath);
            Desktop.getDesktop().open(path);
        } catch (IOException ioe) {
            System.out.println(ioe);
        }
    }

    /**
     * Checks if a string can be parsed as a double.
     *
     * @param str the string to check
     * @return true if the string can be parsed as a double, false otherwise
     */
    private boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}

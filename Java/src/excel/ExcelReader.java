package excel;

import gui.ColumnStatusCellRender;
import gui.ComboBoxes;
import gui.MainWindow;
import gui.Tables;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.util.Iterator;

/**
 * Class for reading data from an Excel file and populating a JTable.
 *
 * @author M.O. Hodkova
 * @version 1.0
 */
public class ExcelReader {
    private MainWindow mainWindow;
    private Tables tables;
    private ComboBoxes comboBoxes;
    /**
     * Creates a new instance of the ExcelReader class.
     *
     * @param mainWindow the main window of the application
     */
    public ExcelReader(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        read();
    }

    /**
     * Reads data from an Excel file and populates a JTable.
     */
    private void read()  {
        try {
            JFileChooser jFileChooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Файлы Excel", "xls", "xlsx");
            jFileChooser.setFileFilter(filter);
            int result = jFileChooser.showOpenDialog(mainWindow);
            if (result != JFileChooser.APPROVE_OPTION) {
                return;
            }
            File file = jFileChooser.getSelectedFile();

            String extension = getFileExtension(file);
            if (!extension.equals("xls") && !extension.equals("xlsx")) {
                JOptionPane.showMessageDialog(null, "Выберите файл с расширением .xls или .xlsx");
                return;
            }

            if (tables.getDataModelAttendance().getRowCount() > 0) {
                JOptionPane.showMessageDialog(null, "Таблица уже заполнена данными.");
                return;
            }

            FileInputStream excelFile = new FileInputStream(file);
            Workbook workbook = new XSSFWorkbook(excelFile);
            Sheet datatypeSheet = workbook.getSheetAt(0);
            Iterator<Row> iterator = datatypeSheet.iterator();

            while (iterator.hasNext()) {
                Row currentRow = iterator.next();
                Iterator<Cell> cellIterator = currentRow.iterator();
                Object[] rowData = new Object[currentRow.getLastCellNum()];

                int i = 0;
                while (cellIterator.hasNext()) {
                    Cell currentCell = cellIterator.next();
                    if (currentCell.getCellType() == CellType.STRING) {
                        rowData[i++] = currentCell.getStringCellValue();
                        System.out.print(currentCell.getStringCellValue());
                    } else if (currentCell.getCellType() == CellType.NUMERIC) {
                        int num = (int) currentCell.getNumericCellValue();
                        rowData[i++] = num;
                        System.out.print(num);
                    }
                }
                tables.getStudentsDataModel().addRow(rowData);
                System.out.println();
            }
            tables.getDataModelAttendance().addColumn("Добавить");
            tables.getDataModelAttendance().addColumn("Всего");
            tables.setTableAttendanceAppearance();
            for (int i = 0; i < tables.getTableAttendance().getColumnCount()-1; i++) {
                tables.getTableAttendance().getColumnModel().getColumn(i).
                        setCellEditor(new DefaultCellEditor(comboBoxes.getCmbAttendance()));
                tables.getTableAttendance().getColumnModel().getColumn(i).
                        setCellRenderer(new ColumnStatusCellRender());
            }
            tables.getDataModelAttendance().setRowCount(tables.getStudentsDataModel().getRowCount());
            for (int j = 0; j < tables.getDataModelAttendance().getRowCount(); j++) {
                tables.getDataModelAttendance().setValueAt(comboBoxes.getCmbAttendance().getItemAt(0), j, 0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the extension of a file.
     *
     * @param file the file
     * @return the extension of the file
     */
    private static String getFileExtension(File file) {
        String name = file.getName();
        int lastIndexOfDot = name.lastIndexOf(".");
        if (lastIndexOfDot == -1) {
            return "";
        }
        return name.substring(lastIndexOfDot + 1);
    }
}




package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * The ColumnStatusCellRenderer class represents a table cell renderer
 * that sets the background color of a cell based on its value.
 *
 * @author M.O. Hodkova
 * @version 1.0
 */
public class ColumnStatusCellRender extends DefaultTableCellRenderer {

    /**
     * Returns the component used to render the value of the specified cell in a table.
     *
     * @param table the table containing the cell
     * @param value the value of the cell
     * @param isSelected true if the cell is selected
     * @param hasFocus true if the cell has focus
     * @param row the row index of the cell
     * @param col the column index of the cell
     * @return the component used to render the value of the specified cell in a table
     */
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {

        JLabel cell = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
        cell.setHorizontalAlignment(SwingConstants.CENTER);
        if (value == null) return cell;
        if (value.toString().equals(" ")) {
            cell.setBackground(Color.WHITE);
        }
        switch (value.toString()) {
            case " ":
                cell.setBackground(Color.WHITE);
                break;
            case "2":
                cell.setBackground(new Color(251,89,89,175));
                break;
            case "1":
                cell.setBackground(new Color(204,204,204,150));
                break;
        }
        return cell;
    }
}

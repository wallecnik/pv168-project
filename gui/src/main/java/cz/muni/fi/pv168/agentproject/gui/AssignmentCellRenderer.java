package cz.muni.fi.pv168.agentproject.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * Created by Dužinka on 5. 5. 2015.
 */
public class AssignmentCellRenderer extends DefaultTableCellRenderer {

    int lastRow = -1;
    int lastColumn = -1;

    public Component getTableCellRendererComponent(JTable table,
                                                   Object value,
                                                   boolean isSelected,
                                                   boolean hasFocus,
                                                   int row,
                                                   int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        if (isSelected) {
            lastRow = row;
            lastColumn = column;
        }

        if (!table.hasFocus() && row == lastRow && column == lastColumn) {
            setBackground(table.getSelectionBackground());
        } else if (isSelected) {
            setBackground(table.getSelectionBackground());
        } else {
            setBackground(table.getBackground());
        }

        return this;
    }
}
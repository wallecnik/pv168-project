package cz.muni.fi.pv168.agentproject.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Used for sorting based on table headers.
 * Possible future modifications for agent and assignment tables.
 * For that, "ID" in switch construct must be conditioned.
 *
 * Created by Dužinka on 5. 5. 2015.
 */
public class TableHeaderMouseListener extends MouseAdapter {

    private JTable table;

    public TableHeaderMouseListener(JTable table) {
        this.table = table;
    }

    public void mouseClicked(MouseEvent event) {
        Point point = event.getPoint();
        int column = table.columnAtPoint(point);

        switch(table.getColumnName(column)) {
            case "ID":
                ((MissionTableModel) table.getModel()).sortById();
                break;
            case "Goal":
                ((MissionTableModel) table.getModel()).sortByGoal();
                break;
            case "Required agents":
                ((MissionTableModel) table.getModel()).sortByRequiredAgents();
                break;
            case "Completed":
                ((MissionTableModel) table.getModel()).sortByCompleted();
                break;
            default:
                JOptionPane.showMessageDialog(table, "Clicked table header: " + table.getColumnName(column));
                break;
        }
    }
}

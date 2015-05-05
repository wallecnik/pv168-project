package cz.muni.fi.pv168.agentproject.gui;

import cz.muni.fi.pv168.agentproject.db.Agent;
import cz.muni.fi.pv168.agentproject.db.AgentManager;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.time.Instant;
import java.util.List;

/**
 * Implementation of TableModel for displaying Agents.
 *
 * @author Wallecnik
 * @version 1.0-SNAPSHOT
 */
public class AgentTableModel extends AbstractTableModel {
    //TODO: add logging

    private static final int COLUMN_COUNT = 3;

    private AgentManager manager;

    private List<Agent> agents;

    /**
     * Constructor for objects of class AgentTableModel. This method registers the given manager
     * and populates the table. This method should be run at the initialization part of the application
     *
     * @param manager manager to be used for database operations
     */
    public AgentTableModel(AgentManager manager) {
        this.manager = manager;
        agents = manager.findAllAgents();
    }

    /**
     * Adds a new Agent at the end of the table.
     * Database operation runs on a separate thread.
     *
     * @param agent An agent to be inserted
     */
    public void addAgent(Agent agent) {
        if (agent != null) {
            agents.add(agent);
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    manager.createAgent(agent);
                }
            });
            fireTableRowsInserted(agents.size() - 1, agents.size() - 1);
        }
    }

    /**
     * Returns the number of rows in the model. A
     * <code>JTable</code> uses this method to determine how many rows it
     * should display.  This method should be quick, as it
     * is called frequently during rendering.
     *
     * @return the number of rows in the model
     * @see #getColumnCount
     */
    @Override
    public int getRowCount() {
        return agents.size();
    }

    /**
     * Returns the number of columns in the model. A
     * <code>JTable</code> uses this method to determine how many columns it
     * should create and display by default.
     *
     * @return the number of columns in the model
     * @see #getRowCount
     */
    @Override
    public int getColumnCount() {
        return COLUMN_COUNT;
    }

    /**
     * Returns the most specific superclass for all the cell values
     * in the column.  This is used by the <code>JTable</code> to set up a
     * default renderer and editor for the column.
     *
     * @param columnIndex the index of the column
     * @return the common ancestor class of the object values in the model.
     */
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return Integer.class;
            case 1:
                return String.class;
            case 2:
                return Instant.class;
            default:
                throw new IllegalArgumentException("columnIndex");
        }
    }

    /**
     * Returns the name of the column at <code>columnIndex</code>.  This is used
     * to initialize the table's column header name.  Note: this name does
     * not need to be unique; two columns in a table can have the same name.
     *
     * @param columnIndex the index of the column
     * @return the name of the column
     */
    @Override
    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return "Id";
            case 1:
                return "Name";
            case 2:
                return "Born";
            default:
                throw new IllegalArgumentException("columnIndex");
        }
    }

    /**
     * Returns the value for the cell at <code>columnIndex</code> and
     * <code>rowIndex</code>.
     *
     * @param rowIndex    the row whose value is to be queried
     * @param columnIndex the column whose value is to be queried
     * @return the value Object at the specified cell
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Agent agent = agents.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return agent.getId();
            case 1:
                return agent.getName();
            case 2:
                return agent.getBorn();
            default:
                throw new IllegalArgumentException("columnIndex");
        }
    }

    /**
     * Sets the value in the cell at <code>columnIndex</code> and
     * <code>rowIndex</code> to <code>aValue</code>.
     *
     * @param aValue      the new value
     * @param rowIndex    the row whose value is to be changed
     * @param columnIndex the column whose value is to be changed
     * @see #getValueAt
     * @see #isCellEditable
     */
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Agent agent = agents.get(rowIndex);
        switch (columnIndex) {
            case 1:
                agent.setName((String) aValue);
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        manager.updateAgent(agent);
                    }
                });
                break;
            default:
                throw new IllegalArgumentException("columnIndex");
        }
        fireTableCellUpdated(rowIndex, columnIndex);
    }

    /**
     * Returns true if the cell at <code>rowIndex</code> and
     * <code>columnIndex</code>
     * is editable.  Otherwise, <code>setValueAt</code> on the cell will not
     * change the value of that cell.
     *
     * @param rowIndex    the row whose value to be queried
     * @param columnIndex the column whose value to be queried
     * @return true if the cell is editable
     * @see #setValueAt
     */
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return false;
            case 1:
                return true;
            case 2:
                return false;
            default:
                throw new IllegalArgumentException("columnIndex");
        }
    }

    /**
     * Removes the specified row from the table and the database.
     * Database operation runs on a separate thread.
     *
     * @param row A row to be deleted
     */
    public void removeRow(int row) {
        if (row == -1) {
            return;
        }

        Agent agent = agents.get(row);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                manager.deleteAgent(agent);
            }
        });
        agents.remove(row);
        fireTableRowsDeleted(row, row);
    }

    public Agent getAgent(int row) {
        return agents.get(row);
    }

    public int getAgentIndex(Agent agent) {
        return agents.indexOf(agent);
    }
}
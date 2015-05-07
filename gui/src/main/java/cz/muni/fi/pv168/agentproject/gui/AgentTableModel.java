package cz.muni.fi.pv168.agentproject.gui;

import cz.muni.fi.pv168.agentproject.db.Agent;
import cz.muni.fi.pv168.agentproject.db.AgentManager;
import cz.muni.fi.pv168.agentproject.db.Constants;
import cz.muni.fi.pv168.agentproject.db.ServiceFailureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Implementation of TableModel for displaying Agents.
 *
 * @author Wallecnik
 * @version 1.0-SNAPSHOT
 */
public class AgentTableModel extends AbstractTableModel {

    private static final Logger log = LoggerFactory.getLogger(AgentTableModel.class);

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
        log.debug("creating AgentTableModel");
        this.manager = manager;
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground()  {
                try {
                    agents = manager.findAllAgents();
                } catch (ServiceFailureException e) {
                    log.error("Database error", e);
                    Gui.alert(Gui.getStrings().getString("gui.alert.database_error"));
                    System.exit(-1);
                }
                return null;
            }

            @Override
            protected void done() {
                super.done();
                AgentTableModel.this.fireTableDataChanged();
            }
        };
        worker.execute();
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
                return String.class;
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
                return Gui.getStrings().getString("gui.table.agents.header.id");
            case 1:
                return Gui.getStrings().getString("gui.table.agents.header.name");
            case 2:
                return Gui.getStrings().getString("gui.table.agents.header.born");
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
        switch (columnIndex) {
            case 0:
                return getAgent(rowIndex).getId();
            case 1:
                return getAgent(rowIndex).getName();
            case 2:
                return DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).format(
                        ZonedDateTime.ofInstant(getAgent(rowIndex).getBorn(), ZoneId.systemDefault()));
            default:
                throw new IllegalArgumentException("columnIndex");
        }
    }

    /**
     * Sets the value in the cell at <code>columnIndex</code> and
     * <code>rowIndex</code> to <code>aValue</code>.
     * Database operation runs on a separate thread via SwingWorker.
     *
     * @param aValue      the new value
     * @param rowIndex    the row whose value is to be changed
     * @param columnIndex the column whose value is to be changed
     * @see #getValueAt
     * @see #isCellEditable
     */
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Agent agent = getAgent(rowIndex);
        switch (columnIndex) {
            case 1:
                if (verifyNameAndAlert((String) aValue)) {
                    agent.setName((String) aValue);
                    SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                        @Override
                        protected Void doInBackground() {
                            try {
                                manager.updateAgent(agent);
                                agents = manager.findAllAgents();
                            } catch (ServiceFailureException e) {
                                log.error("Database error", e);
                                Gui.alert(Gui.getStrings().getString("gui.alert.database_error"));
                            } catch (IllegalArgumentException e) {
                                log.error("", e);
                                Gui.alert(Gui.getStrings().getString("gui.alert.unspecified_error"));
                            }
                            return null;
                        }

                        @Override
                        protected void done() {
                            super.done();
                            AgentTableModel.this.fireTableDataChanged();
                        }
                    };
                    worker.execute();
                }
                break;
            default:
                throw new IllegalArgumentException("columnIndex");
        }
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
     * Adds a new Agent at the end of the table.
     * Database operation runs on a separate thread via SwingWorker.
     *
     * @param agent An agent to be inserted
     */
    public void addAgent(Agent agent) {
        if (agent != null) {
            SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground()  {
                    try {
                        manager.createAgent(agent);
                        agents = manager.findAllAgents();
                    } catch (ServiceFailureException e) {
                        log.error("Database error", e);
                        Gui.alert(Gui.getStrings().getString("gui.alert.database_error"));
                    } catch (IllegalArgumentException e) {
                        log.error("", e);
                        Gui.alert(Gui.getStrings().getString("gui.alert.unspecified_error"));
                    }
                    return null;
                }

                @Override
                protected void done() {
                    super.done();
                    AgentTableModel.this.fireTableDataChanged();
                }
            };
            worker.execute();
        }
    }

    /**
     * Removes the specified row from the table and the database.
     * Database operation runs on a separate thread via SwingWorker.
     *
     * @param row A row to be deleted
     */
    public void removeRow(int row) {
        if (row < 0) {
            return;
        }

        Agent agent = getAgent(row);
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                try {
                    manager.deleteAgent(agent);
                    agents = manager.findAllAgents();
                } catch (ServiceFailureException e) {
                    log.error("Database error", e);
                    Gui.alert(Gui.getStrings().getString("gui.alert.database_error"));
                } catch (IllegalArgumentException e) {
                    log.error("", e);
                    Gui.alert(Gui.getStrings().getString("gui.alert.unspecified_error"));
                }
                return null;
            }

            @Override
            protected void done() {
                super.done();
                AgentTableModel.this.fireTableDataChanged();
            }
        };
        worker.execute();
    }

    public Agent getAgent(int row) {
        return agents.get(row);
    }

    public int getAgentIndex(Agent agent) {
        return agents.indexOf(agent);
    }

    private boolean verifyNameAndAlert(String name) {
        if (name == null) {
            Gui.alert(Gui.getStrings().getString("gui.alert.agents.name.null"));
            return false;
        }
        if (name.equals("")) {
            Gui.alert(Gui.getStrings().getString("gui.alert.agents.name.empty"));
            return false;
        }
        if (name.length() > Constants.AGENT_NAME_MAX_LENGTH) {
            Gui.alert(Gui.getStrings().getString("gui.alert.agents.name.long"));
            return false;
        }
        if (!Pattern.matches(Constants.AGENT_NAME_REGEX, name)) {
            Gui.alert(Gui.getStrings().getString("gui.alert.agents.name.illegal"));
            return false;
        }
        return true;
    }

}
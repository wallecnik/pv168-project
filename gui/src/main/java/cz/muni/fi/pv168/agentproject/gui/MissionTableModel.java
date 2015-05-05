package cz.muni.fi.pv168.agentproject.gui;

import cz.muni.fi.pv168.agentproject.db.Mission;
import cz.muni.fi.pv168.agentproject.db.MissionManager;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.List;


public class MissionTableModel extends AbstractTableModel {

    private static final int COLUMN_COUNT = 4;

    private MissionManager manager;

    private List<Mission> missions;

    public MissionTableModel(MissionManager manager) {
        this.manager = manager;
        missions = manager.findAllMissions();
    }

    @Override
    public int getRowCount() {
        return missions.size();
    }

    /**
     * Id, goal, required agents, completed
     * @return
     */
    @Override
    public int getColumnCount() {
        return COLUMN_COUNT;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return Long.class;
            case 1:
                return String.class;
            case 2:
                return Integer.class;
            case 3:
                return Boolean.class;
            default:
                throw new IllegalArgumentException("columnIndex");
        }
    }

    @Override
    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return "ID";
            case 1:
                return "Goal";
            case 2:
                return "Required agents";
            case 3:
                return "Completed";
            default:
                throw new IllegalArgumentException("columnIndex");
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Mission mission = missions.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return mission.getId();
            case 1:
                return mission.getGoal();
            case 2:
                return mission.getRequiredAgents();
            case 3:
                return mission.isCompleted();
            default:
                throw new IllegalArgumentException("columnIndex");
        }
    }

    @Override
    public void setValueAt(Object newValue, int rowIndex, int columnIndex) {
        Mission mission = missions.get(rowIndex);
        switch (columnIndex) {
            case 1:
                mission.setGoal((String) newValue);
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        manager.updateMission(mission);
                    }
                });
                break;
            case 2:
                mission.setRequiredAgents((int) newValue);
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        manager.updateMission(mission);
                    }
                });
                break;
            case 3:
                mission.setCompleted((boolean) newValue);
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        manager.updateMission(mission);
                    }
                });
                break;
            default:
                throw new IllegalArgumentException("columnIndex");
        }
        fireTableCellUpdated(rowIndex, columnIndex);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return false;
            case 1:
                return true;
            case 2:
                return true;
            case 3:
                return true;
            default:
                throw new IllegalArgumentException("columnIndex");
        }
    }

    public void addMission(Mission mission) {
        if (mission != null) {
            missions.add(mission);
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    manager.createMission(mission);
                }
            });
            fireTableRowsInserted(missions.size() - 1, missions.size() - 1);
        }
    }

    public void removeMission(int row) {
        if (row == -1) {
            return;
        }

        Mission mission = missions.get(row);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                manager.deleteMission(mission);
            }
        });
        missions.remove(row);
        fireTableRowsDeleted(row, row);
    }

    public void sortById() {
        missions = manager.sortById();
    }

    public void sortByGoal() {
        missions = manager.sortByGoal();
    }

    public void sortByRequiredAgents() {
        missions = manager.sortByRequiredAgents();
    }

    public void sortByCompleted() {
        missions = manager.sortByCompleted();
    }
}

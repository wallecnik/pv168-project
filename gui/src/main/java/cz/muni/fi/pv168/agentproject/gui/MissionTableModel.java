package cz.muni.fi.pv168.agentproject.gui;

import cz.muni.fi.pv168.agentproject.db.Mission;
import cz.muni.fi.pv168.agentproject.db.MissionManager;

import javax.swing.table.AbstractTableModel;
import java.util.List;

/**
 * TOHLE JE OPRAVDU TORZO, NEPOUZITELNE, PREPSAT
 */
public class MissionTableModel extends AbstractTableModel {

    private static final int COLUMN_COUNT = 3;

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

    @Override
    public int getColumnCount() {
        return COLUMN_COUNT;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Mission mission = missions.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return mission.getId();
            case 1:
                return mission.getRequiredAgents();
            case 2:
                return mission.getGoal();
            case 3:
                return mission.isCompleted();
            default:
                throw new IllegalArgumentException("columnIndex");
        }
    }
}

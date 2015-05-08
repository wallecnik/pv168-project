package cz.muni.fi.pv168.agentproject.gui;

import cz.muni.fi.pv168.agentproject.db.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.List;


public class MissionTableModel extends AbstractTableModel {

    private static final Logger log = LoggerFactory.getLogger(MissionTableModel.class);

    private static final int COLUMN_COUNT = 4;

    private MissionManager manager;

    private List<Mission> missions;

    public MissionTableModel(MissionManager manager) {
        log.debug("creating AgentTableModel");
        this.manager = manager;
        this.manager = manager;
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground()  {
                try {
                    missions = manager.findAllMissions();
                } catch (ServiceFailureException e) {
                    log.error("Database error", e);
                    Gui.alert(Gui.getStrings().getString("gui.alert.database_error"));
                }
                return null;
            }

            @Override
            protected void done() {
                super.done();
                MissionTableModel.this.fireTableDataChanged();
            }
        };
        worker.execute();
    }

    @Override
    public int getRowCount() {
        if (missions == null) {
            return 0;
        }
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
                return Gui.getStrings().getString("gui.table.missions.header.id");
            case 1:
                return Gui.getStrings().getString("gui.table.missions.header.goal");
            case 2:
                return Gui.getStrings().getString("gui.table.missions.header.required_agents");
            case 3:
                return Gui.getStrings().getString("gui.table.missions.header.completed");
            default:
                throw new IllegalArgumentException("columnIndex");
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Mission mission = getMission(rowIndex);
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
        Mission mission = getMission(rowIndex);
        switch (columnIndex) {
            case 1:
                if (verifyGoal((String) newValue)) {
                    mission.setGoal((String) newValue);
                    SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                        @Override
                        protected Void doInBackground()  {
                            try {
                                manager.updateMission(mission);
                                missions = manager.findAllMissions();
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
                            MissionTableModel.this.fireTableDataChanged();
                        }
                    };
                    worker.execute();
                }
                break;

            case 2:
                if (verifyRequiredAgents((int) newValue)) {
                    mission.setRequiredAgents((int) newValue);
                    SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                        @Override
                        protected Void doInBackground()  {
                            try {
                                manager.updateMission(mission);
                                missions = manager.findAllMissions();
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
                            MissionTableModel.this.fireTableDataChanged();
                        }
                    };
                    worker.execute();
                }
                break;

            case 3:
                mission.setCompleted((boolean) newValue);
                SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground()  {
                        try {
                            manager.updateMission(mission);
                            missions = manager.findAllMissions();
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
                        MissionTableModel.this.fireTableDataChanged();
                    }
                };
                worker.execute();
                break;

            default:
                throw new IllegalArgumentException("columnIndex");
        }
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
            SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground()  {
                    try {
                        manager.createMission(mission);
                        missions = manager.findAllMissions();
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
                    MissionTableModel.this.fireTableDataChanged();
                }
            };
            worker.execute();
        }
    }

    public void removeMission(int row) {
        if (row < 0) {
            return;
        }

        Mission mission = getMission(row);
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground()  {
                try {
                    manager.deleteMission(mission);
                    missions = manager.findAllMissions();
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
                MissionTableModel.this.fireTableDataChanged();
            }
        };
        worker.execute();
    }

    public Mission getMission(int row) {
        return missions.get(row);
    }

    public void sortById() {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground()  {
                try {
                    missions = manager.sortById();
                } catch (ServiceFailureException e) {
                    log.error("Database error", e);
                    Gui.alert(Gui.getStrings().getString("gui.alert.database_error"));
                }
                return null;
            }

            @Override
            protected void done() {
                super.done();
                MissionTableModel.this.fireTableDataChanged();
            }
        };
        worker.execute();
    }

    public void sortByGoal() {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground()  {
                try {
                    missions = manager.sortByGoal();
                } catch (ServiceFailureException e) {
                    log.error("Database error", e);
                    Gui.alert(Gui.getStrings().getString("gui.alert.database_error"));
                }
                return null;
            }

            @Override
            protected void done() {
                super.done();
                MissionTableModel.this.fireTableDataChanged();
            }
        };
        worker.execute();
    }

    public void sortByRequiredAgents() {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground()  {
                try {
                    missions = manager.sortByRequiredAgents();
                } catch (ServiceFailureException e) {
                    log.error("Database error", e);
                    Gui.alert(Gui.getStrings().getString("gui.alert.database_error"));
                }
                return null;
            }

            @Override
            protected void done() {
                super.done();
                MissionTableModel.this.fireTableDataChanged();
            }
        };
        worker.execute();
    }

    public void sortByCompleted() {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground()  {
                try {
                    missions = manager.sortByCompleted();
                } catch (ServiceFailureException e) {
                    log.error("Database error", e);
                    Gui.alert(Gui.getStrings().getString("gui.alert.database_error"));
                }
                return null;
            }

            @Override
            protected void done() {
                super.done();
                MissionTableModel.this.fireTableDataChanged();
            }
        };
        worker.execute();
    }

    public int getMissionIndex(Mission mission) {
        return missions.indexOf(mission);
    }

    /**
     * Checks whether the input for field "Goal" is valid or not.
     * @return true if everything went as expected
     */
    private boolean verifyGoal(String goal) {
        if (goal == null) {
            Gui.alert(Gui.getStrings().getString("gui.alert.missions.goal.null"));
            return false;
        }
        if (goal.equals("")) {
            Gui.alert(Gui.getStrings().getString("gui.alert.missions.goal.empty"));
            return false;
        }
        if (goal.length() > Constants.MISSION_GOAL_MAX_LENGTH) {
            Gui.alert(Gui.getStrings().getString("gui.alert.missions.goal.long"));
            return false;
        }

        return true;
    }

    // TODO: Don't allow to go below the number of current assignments
    private boolean verifyRequiredAgents(int requiredAgents) {
        if (requiredAgents <= 0) {
            Gui.alert(Gui.getStrings().getString("gui.alert.missions.required_agents"));
            return false;
        }
        return true;
    }
}

package cz.muni.fi.pv168.agentproject.gui;

import cz.muni.fi.pv168.agentproject.db.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Dužinka on 5. 5. 2015.
 */
public class AssignmentTableManager {

    private static final Logger log = LoggerFactory.getLogger(AssignmentTableManager.class);

    private AssignmentManager manager;
    private Gui gui;

    Set<Assignment> assignments;

    public AssignmentTableManager (AssignmentManager manager, Gui gui) {
        this.manager = manager;
        this.gui = gui;
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground(){
                try {
                    assignments = manager.findAllAssignments();
                } catch (ServiceFailureException e) {
                    log.error("Database error", e);
                    Gui.alert(Gui.getStrings().getString("gui.alert.database_error"));
                }
                return null;
            }
        };
        worker.execute();
    }

    public void addAssignment(Agent agent, Mission mission) {
        Instant startTimeNow = Instant.now();
        Assignment assignment = new Assignment(null, agent, mission, startTimeNow);
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground(){
                try {
                    manager.createAssignment(assignment);
                    assignments = manager.findAllAssignments();
                } catch (ServiceFailureException e) {
                    log.error("Database error", e);
                    Gui.alert(Gui.getStrings().getString("gui.alert.database_error"));
                } catch (IllegalArgumentException e) {
                    log.error("", e);
                    Gui.alert(Gui.getStrings().getString("gui.alert.assignments.duplicate"));
                }
                return null;
            }
        };
        worker.execute();
    }

    public void removeAssignment(Agent agent, Mission mission){
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground(){
                try {
                    List<Assignment> commonAssignment = new ArrayList<>(manager.findAssignmentsForAgent(agent));
                    commonAssignment.retainAll(manager.findAssignmentsForMission(mission));
                    if (commonAssignment.size() == 1) {
                        Assignment assignment = (commonAssignment.get(0));
                        manager.deleteAssignment(assignment);
                        assignments = manager.findAllAssignments();
                    }
                    else {
                        Gui.alert(Gui.getStrings().getString("gui.alert.assignments.not_exist"));
                    }
                } catch (ServiceFailureException e) {
                    log.error("Database error", e);
                    Gui.alert(Gui.getStrings().getString("gui.alert.database_error"));
                } catch (IllegalArgumentException e) {
                    log.error("", e);
                    Gui.alert(Gui.getStrings().getString("gui.alert.unspecified_error"));
                }
                return null;
            }
        };
        worker.execute();
    }

    public void showMissionsForAgent(Agent agent, int indexAgent) {
        List<Mission> missions = new ArrayList<>();
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground(){
                try {
                    List<Assignment> agentAssignments = manager.findAssignmentsForAgent(agent);
                    for (Assignment assignment : agentAssignments) {
                        missions.add(assignment.getMission());
                    }
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
                // Refresh both tables selection
                gui.getMissionsTable().clearSelection();
                gui.getAgentsTable().setRowSelectionInterval(indexAgent, indexAgent);
                if (missions != null) {

                    // For each mission in assignment, find its row index in mission table and add it to selection
                    for (Mission mission : missions) {
                        int indexMission = ((MissionTableModel) gui.getMissionsTable().getModel()).getMissionIndex(mission);
                        gui.getMissionsTable().addRowSelectionInterval(indexMission, indexMission);
                    }
                }
            }
        };
        worker.execute();
    }

    public List<Agent> showAgentsForMission(Mission mission, int indexMission) {
        List<Agent> agents = new ArrayList<>();
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                try {
                    List<Assignment> missionAssignments = manager.findAssignmentsForMission(mission);
                    for (Assignment assignment : missionAssignments) {
                        agents.add(assignment.getAgent());
                    }
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
                // Refresh both tables selection
                gui.getAgentsTable().clearSelection();
                gui.getMissionsTable().setRowSelectionInterval(indexMission, indexMission);
                if (agents != null) {

                    // For each agent in assignment, find its row index in agent table and add it to selection
                    for (Agent agent : agents) {
                        int indexAgent = ((AgentTableModel) gui.getAgentsTable().getModel()).getAgentIndex(agent);
                        gui.getAgentsTable().addRowSelectionInterval(indexAgent, indexAgent);
                    }
                }
            }
        };
        worker.execute();
        return agents;
    }
}

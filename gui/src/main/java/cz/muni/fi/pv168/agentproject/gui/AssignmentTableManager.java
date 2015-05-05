package cz.muni.fi.pv168.agentproject.gui;

import cz.muni.fi.pv168.agentproject.db.Agent;
import cz.muni.fi.pv168.agentproject.db.Assignment;
import cz.muni.fi.pv168.agentproject.db.AssignmentManager;
import cz.muni.fi.pv168.agentproject.db.Mission;

import javax.swing.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * Created by Dužinka on 5. 5. 2015.
 */
public class AssignmentTableManager {

    private AssignmentManager manager;

    Set<Assignment> assignments;

    public AssignmentTableManager (AssignmentManager manager) {
        this.manager = manager;
        assignments = manager.findAllAssignments();
    }

    public void addAssignment(Agent agent, Mission mission) throws IllegalArgumentException {
        Instant startTimeNow = Instant.now();
        Assignment assignment = new Assignment(null, agent, mission, startTimeNow);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                manager.createAssignment(assignment);
            }
        });
        assignments.add(assignment);
    }

    public void removeAssignment(Agent agent, Mission mission) throws NoSuchElementException {
        List<Assignment> commonAssignment = new ArrayList<>(manager.findAssignmentsForAgent(agent));
        commonAssignment.retainAll(manager.findAssignmentsForMission(mission));
        if (commonAssignment.size() == 1) {
            Assignment assignment = (commonAssignment.get(0));
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    manager.deleteAssignment(assignment);
                }
            });
            assignments.remove(assignment);
        }
        else {
            throw new NoSuchElementException("No such assignment exists");      // TODO: Logging
        }
    }

    public List<Mission> showMissionsForAgent(Agent agent) {
        List<Assignment> agentAssignments = manager.findAssignmentsForAgent(agent);
        List<Mission> missions = new ArrayList<>();
        for (Assignment assignment : agentAssignments) {
            missions.add(assignment.getMission());
        }
        return missions;
    }

    public List<Agent> showAgentsForMission(Mission mission) {
        List<Assignment> missionAssignments = manager.findAssignmentsForMission(mission);
        List<Agent> agents = new ArrayList<>();
        for (Assignment assignment : missionAssignments) {
            agents.add(assignment.getAgent());
        }
        return agents;
    }
}

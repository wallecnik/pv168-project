package cz.muni.fi.agentproject;

import java.util.List;
import java.util.Set;

/**
 * @author  Wallecnik
 * @version 31.22.2015
 */
public interface AssignmentManager {

    void createAssignment(Assignment assignment);

    Assignment findAssignmentById(Long id);

    void updateAssignment(Assignment assignment);

    void deleteAssignment(Assignment assignment);

    Set<Assignment> findAllAssignments();

    Set<Assignment> findAssignmentsForAgent(Agent agent);

    Set<Assignment> findAssignmentsforMission(Mission mission);

}

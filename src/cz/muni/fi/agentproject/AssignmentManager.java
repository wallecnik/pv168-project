package cz.muni.fi.agentproject;

import java.util.List;

/**
 * @author  Wallecnik
 * @version 31.22.2015
 */
public interface AssignmentManager {

    void createAssignment(Assignment assignment);

    Assignment findAssignmentById(Long id);

    void updateAssignment(Assignment assignment);

    void deleteAssignment(Assignment assignment);

    List<Assignment> findAllAssignments();

    List<Assignment> findAssignmentsForAgent(Agent agent);

    List<Assignment> findAssignmentsforMission(Mission mission);

}

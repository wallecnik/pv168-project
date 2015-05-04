package cz.muni.fi.pv168.agentproject.db;

import java.util.List;
import java.util.Set;

/**
 * Interface for basic CRUD operations for Assignment instance.
 *
 * Implementation should check the instance's state before saving them
 * in order to store valid data
 *
 * @author  Wallecnik
 * @version 24.3.2015
 */
public interface AssignmentManager {

    /**
     * Adds a new assignment. Provided Assignment should have valid values associated.
     *
     * @param assignment an instance of Assignment to store
     * @throws IllegalArgumentException if the Assignment has invalid data
     */
    void createAssignment(Assignment assignment) throws IllegalArgumentException;

    /**
     * Returns the Assignment associated with the given id or null if the id was not found.
     *
     * @param id an id to look for
     * @return an instance of Assignment or null
     * @throws IllegalArgumentException if the given id is null or negative
     */
    Assignment findAssignmentById(Long id) throws IllegalArgumentException;

    /**
     * Updates an existing Assignment. Stored Assignment is found by id in the given Assignment.
     * Provided Assignment should have valid data associated.
     *
     * @param assignment an instance of Assignment to update
     * @throws IllegalArgumentException if the Assignment has invalid data
     */
    void updateAssignment(Assignment assignment) throws IllegalArgumentException;

    /**
     * Deletes assignment. Stored assignment is found by id associated with the given assignment.
     *
     * @param assignment an instance of assignment to delete with valid id
     * @throws IllegalArgumentException if the given assignment has invalid data
     */
    void deleteAssignment(Assignment assignment) throws IllegalArgumentException;

    /**
     * Returns a Set of all assignments. If there are none, then an empty collection is returned.
     *
     * @return Set of all assignments
     */
    Set<Assignment> findAllAssignments();

    /**
     * Returns a Set of all assignments assigned to a specific agent. Agent should have valid id.
     *
     * @param agent instance of an Agent with valid id
     * @return Set of assignments for an agent
     * @throws IllegalArgumentException if the given Agent has invalid data
     */
    List<Assignment> findAssignmentsForAgent(Agent agent) throws IllegalArgumentException;

    /**
     * Returns a Set of all assignments assigned to a specific mission. Mission should have valid id.
     *
     * @param mission instance of a Mission with valid id
     * @return Set of assignments for a Mission
     * @throws IllegalArgumentException if the given Mission has invalid data
     */
    List<Assignment> findAssignmentsForMission(Mission mission) throws IllegalArgumentException;

}

package cz.muni.fi.agentproject;

import javax.sql.DataSource;
import java.sql.*;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class manages the database for creating, retrieving, updating and deleting Assignments.
 * This implementation follows recommendations given by AssignmentManager interface's JavaDoc.
 *
 * This class uses thread safe DataSource interface for connecting to database.
 *
 * @author  Wallecnik
 * @version 29.3.2015
 */
public class AssignmentManagerImpl extends AbstractManager implements AssignmentManager {

    public static final Logger logger = Logger.getLogger(AssignmentManagerImpl.class.getName());

    private DataSource dataSource;
    private AgentManager agentManager;
    private MissionManager missionManager;

    /**
     * Constructor for objects of this class. Takes one argument implementing DataSource interface
     * which it uses for connecting to database.
     *
     * @param dataSource reference to object of type DataSource
     */
    public AssignmentManagerImpl(DataSource dataSource, AgentManager agentManager, MissionManager missionManager) {
        this.dataSource = dataSource;
        this.agentManager = agentManager;
        this.missionManager = missionManager;
    }

    /**
     * Adds a new Assignment into the database. Provided Assignment must have valid values
     * associated. If not, the method throws an IllegalArgumentException. Possible
     * causes are, that an assignment ends sooner than it starts or agent or mission
     * have invalid id's. Complete list would be too long - see validateAssignment() method
     * for all of them.
     *
     * @param assignment a well formed instance of Assignment to store
     * @throws IllegalArgumentException  if the Assignment has invalid data
     * @throws ServiceFailureException   if an unpredictable error occurred
     */
    @Override
    public void createAssignment(Assignment assignment) throws IllegalArgumentException {

        validateAssignment(assignment);
        if (assignment.getId() != null) {
            throw new IllegalArgumentException("is is not null");
        }

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     DbHelper.SQL_INSERT_INTO_ASSIGNMENT, Statement.RETURN_GENERATED_KEYS)) {

            ps.setLong(1, assignment.getAgent().getId());
            ps.setLong(2, assignment.getMission().getId());
            ps.setTimestamp(3, Timestamp.from(assignment.getStartTime()));
            if (assignment.getEndTime() != null) {
                ps.setTimestamp(4, Timestamp.from(assignment.getEndTime()));
            }
            else {
                ps.setTimestamp(4, null);
            }
            ps.setLong(5, assignment.getMission().getId());
            ps.setLong(6, assignment.getMission().getId());
            ps.setLong(7, assignment.getAgent().getId());
            ps.setLong(8, assignment.getMission().getId());

            int addedRows = ps.executeUpdate();
            if (addedRows == 0) {
                //The database protection probably aborted insertion
                throw new IllegalArgumentException("Error. Probable cause: Attempting to insert an agent twice or more agent to a mission. " +
                        "Assignment: " + assignment);
            }
            if (addedRows != 1) {
                throw new ServiceFailureException("Unable to insert assignment " + assignment);
            }

            ResultSet keyRS = ps.getGeneratedKeys();
            Long newId = this.getKeyFromRS(keyRS, assignment);
            assignment.setId(newId);

        } catch (SQLException sqle) {
            logger.log(Level.SEVERE, null, sqle);
            throw new ServiceFailureException("Error when inserting assignment " + assignment, sqle);
        }

    }

    /**
     * Returns the Assignment from the database associated with the given id
     * or null if the id was not found.
     *
     * @param id an id to look for
     * @return an instance of Assignment or null
     * @throws IllegalArgumentException  if the given id is null or negative
     * @throws ServiceFailureException   if an unpredictable error occurred
     */
    @Override
    public Assignment findAssignmentById(Long id) throws IllegalArgumentException {

        Assignment retVal = null;

        if (id == null) {
            throw new IllegalArgumentException("id is null");
        }
        if (id <= 0) {
            throw new IllegalArgumentException("id is negative");
        }

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     DbHelper.SQL_SELECT_SINGLE_ASSIGNMENT)) {

            ps.setLong(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.first()) {
                retVal = resultSetToAssignment(rs);
            }

        } catch (SQLException sqle) {
            logger.log(Level.SEVERE, null, sqle);
            throw new ServiceFailureException("Error when looking up an assignment with id" + id, sqle);
        }

        return retVal;
    }

    /**
     * Updates an Assignment in the database. Provided Assignment must have valid values
     * associated. If not, the method throws an IllegalArgumentException. Possible
     * causes are, that an assignment ends sooner than it starts or agent or mission
     * have invalid id's. Complete list would be too long - see validateAssignment() method
     * for all of them.
     *
     * @param assignment an instance of Assignment to update
     * @throws IllegalArgumentException  if the Assignment has invalid data
     * @throws ServiceFailureException   if an unpredictable error occurred
     */
    @Override
    public void updateAssignment(Assignment assignment) throws IllegalArgumentException {

        validateAssignment(assignment);

        if (assignment.getId() == null) {
            throw new IllegalArgumentException("id is null");
        }
        if (assignment.getId() <= 0) {
            throw new IllegalArgumentException("id is negative");
        }

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     DbHelper.SQL_UPDATE_SINGLE_ASSIGNMENT)) {

            ps.setLong(1, assignment.getAgent().getId());
            ps.setLong(2, assignment.getMission().getId());
            ps.setTimestamp(3, Timestamp.from(assignment.getStartTime()));
            if (assignment.getEndTime() != null) {
                ps.setTimestamp(4, Timestamp.from(assignment.getEndTime()));
            }
            else {
                ps.setTimestamp(4, null);
            }
            ps.setLong(5, assignment.getId());
            ps.setLong(6, assignment.getMission().getId());
            ps.setLong(7, assignment.getMission().getId());
            ps.setLong(8, assignment.getAgent().getId());
            ps.setLong(9, assignment.getMission().getId());
            ps.setLong(10, assignment.getId());

            int addedRows = ps.executeUpdate();
            if (addedRows == 0) {
                //The database protection probably aborted insertion
                throw new IllegalArgumentException("Error. Probable cause: Attempting to update an agent twice or more agent to a mission. " +
                        "Assignment: " + assignment);
            }
            if (addedRows != 1) {
                throw new ServiceFailureException("Unable to update assignment " + assignment);
            }

        } catch (SQLException sqle) {
            logger.log(Level.SEVERE, null, sqle);
            throw new ServiceFailureException("Error when updating assignment " + assignment, sqle);
        }

    }

    /**
     * Deletes assignment. Stored assignment does not have to be well formed, valid id
     * associated with the given assignment is enough.
     *
     * @param assignment an instance of assignment to delete with valid id
     * @throws IllegalArgumentException  if the given assignment has invalid data
     * @throws ServiceFailureException   if an unpredictable error occurred
     */
    @Override
    public void deleteAssignment(Assignment assignment) throws IllegalArgumentException {

        if (assignment == null) {
            throw new IllegalArgumentException("assignment is null");
        }
        if (assignment.getId() == null) {
            throw new IllegalArgumentException("id is null");
        }
        if (assignment.getId() <= 0) {
            throw new IllegalArgumentException("id is negative");
        }

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     DbHelper.SQL_DELETE_SINGLE_ASSIGNMENT)) {

            ps.setLong(1, assignment.getId());

            int addedRows = ps.executeUpdate();
            if (addedRows != 1) {
                throw new ServiceFailureException("Unable to delete assignment " + assignment);
            }

        } catch (SQLException sqle) {
            logger.log(Level.SEVERE, null, sqle);
            throw new ServiceFailureException("Error when updating assignment " + assignment, sqle);
        }

    }

    /**
     * Returns a Set of all assignments. If there are none, then an empty collection is returned.
     *
     * @return Set of all assignments
     * @throws ServiceFailureException  if an unpredictable error occurred
     */
    @Override
    public Set<Assignment> findAllAssignments() {

        Set<Assignment> retSet = new HashSet<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     DbHelper.SQL_SELECT_ALL_ASSIGNMENTS)) {

            ResultSet rs = ps.executeQuery();
            rs.beforeFirst();

            while (rs.next()) {
                Assignment assignment = resultSetToAssignment(rs);
                retSet.add(assignment);
            }

        } catch (SQLException sqle) {
            logger.log(Level.SEVERE, null, sqle);
            throw new ServiceFailureException("Error when looking up all agents", sqle);
        }

        return retSet;

    }

    /**
     * Returns a Set of all assignments assigned to a specific agent. Agent should have
     * valid id. Otherwise, the assignments in returned in collection are non-deterministic.
     *
     * @param agent instance of an Agent with valid id
     * @return Set of assignments for an agent
     * @throws IllegalArgumentException  if the given Agent has invalid data
     * @throws ServiceFailureException   if an unpredictable error occurred
     */
    @Override
    public Set<Assignment> findAssignmentsForAgent(Agent agent) throws IllegalArgumentException {

        if (agent == null) {
            throw new IllegalArgumentException("Agent pointer is null");
        }
        if (agent.getId() == null) {
            throw new IllegalArgumentException("Agent with null id cannot be deleted");
        }
        if (agent.getId() <= 0) {
            throw new IllegalArgumentException("Agent's id is less than zero");
        }
        if (agentManager.findAgentById(agent.getId()) == null) {
            throw new IllegalArgumentException("No such agent exists");
        }

        Set<Assignment> retSet = new HashSet<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     DbHelper.SQL_SELECT_ALL_ASSIGNMENTS_FOR_AGENT)) {

            ps.setLong(1, agent.getId());

            ResultSet rs = ps.executeQuery();
            rs.beforeFirst();

            while (rs.next()) {
                Assignment assignment = resultSetToAssignment(rs);
                retSet.add(assignment);
            }

        } catch (SQLException sqle) {
            logger.log(Level.SEVERE, null, sqle);
            throw new ServiceFailureException("Error when looking up all assignment for agent " + agent, sqle);
        }

        return retSet;
    }

    /**
     * Returns a Set of all assignments assigned to a specific mission. Mission should have
     * valid id. Otherwise, the assignments in returned in collection are non-deterministic.
     *
     * @param mission instance of a Mission with valid id
     * @return Set of assignments for a Mission
     * @throws IllegalArgumentException  if the given Mission has invalid data
     * @throws ServiceFailureException   if an unpredictable error occurred
     */
    @Override
    public Set<Assignment> findAssignmentsForMission(Mission mission) throws IllegalArgumentException {

        if (mission == null) {
            throw new IllegalArgumentException("Mission pointer is null");
        }
        if (mission.getId() == null) {
            throw new IllegalArgumentException("mission with null id cannot be deleted");
        }
        if (mission.getId() <= 0) {
            throw new IllegalArgumentException("Mission's id is less than zero");
        }
        if (missionManager.findMissionById(mission.getId()) == null) {
            throw new IllegalArgumentException("No such mission exists");
        }

        Set<Assignment> retSet = new HashSet<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     DbHelper.SQL_SELECT_ALL_ASSIGNMENTS_FOR_MISSION)) {

            ps.setLong(1, mission.getId());

            ResultSet rs = ps.executeQuery();
            rs.beforeFirst();

            while (rs.next()) {
                Assignment assignment = resultSetToAssignment(rs);
                retSet.add(assignment);
            }

        } catch (SQLException sqle) {
            logger.log(Level.SEVERE, null, sqle);
            throw new ServiceFailureException("Error when looking up all assignment for mission " + mission, sqle);
        }

        return retSet;

    }

    /**
     * Group of constraints to check if Assignment has correct values set. Each of the
     * constraints can throw IllegalArgumentException
     */
    private void validateAssignment(Assignment assignment) {

        if (assignment == null) {
            throw new IllegalArgumentException("assignment is null");
        }
        if (assignment.getAgent() == null) {
            throw new IllegalArgumentException("agent is null");
        }
        if (assignment.getAgent().getId() == null) {
            throw new IllegalArgumentException("agent id is null");
        }
        if (assignment.getAgent().getId() <= 0) {
            throw new IllegalArgumentException("agent id is negative");
        }
        if (assignment.getMission() == null) {
            throw new IllegalArgumentException("mission is null");
        }
        if (assignment.getMission().getId() == null) {
            throw new IllegalArgumentException("mission id is null");
        }
        if (assignment.getMission().getId() <= 0) {
            throw new IllegalArgumentException("mission id is negative");
        }
        if (assignment.getStartTime() == null) {
            throw new IllegalArgumentException("start time is null");
        }
        if (assignment.getStartTime().compareTo(Instant.now()) > 0) {
            throw new IllegalArgumentException("assignment starts in the future");
        }
        if (assignment.getEndTime() != null) {
            if (assignment.getStartTime().compareTo(assignment.getEndTime()) > 0) {
                throw new IllegalArgumentException("assignment end sooner than starts");
            }
        }
    }


}




















package cz.muni.fi.agentproject.tests;

import cz.muni.fi.agentproject.*;
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * @author Dužinka
 * @version 24.03.2015
 */
public class AssignmentManagerImplTest {

    private AssignmentManager manager;
    private Mission goodMission;
    private Agent goodAgent;
    private DataSource dataSource;


    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Before
    public void setUp() throws SQLException {
        BasicDataSource bds = new BasicDataSource();
        bds.setUrl(DbHelper.DB_URL);
        bds.setUsername(DbHelper.USERNAME);
        bds.setPassword(DbHelper.PASSWORD);
        this.dataSource = bds;
        try (Connection connection = dataSource.getConnection()) {
            connection.prepareStatement(DbHelper.SQL_DROP_TABLE_ASSIGNMENT)
                    .executeUpdate();
            connection.prepareStatement(DbHelper.SQL_CREATE_TABLE_ASSIGNMENT)
                    .executeUpdate();
        }

        manager = new AssignmentManagerImpl();
        goodMission = new Mission(1L, "Make IS rule the world", 2, false);
        goodAgent = new Agent(1L, "Michal Brandejs", 10L);
    }

    @After
    public void tearDown() throws SQLException {
        try (Connection connection = dataSource.getConnection()){
            connection.prepareStatement(DbHelper.SQL_DROP_TABLE_ASSIGNMENT)
                    .executeUpdate();
        }
    }

    /* Create assignment */

    @Test
    public void createAssignment() {
        Assignment assignment = new Assignment(1L, goodAgent, goodMission, 1L, 2L);
        manager.createAssignment(assignment);

        Long id = assignment.getId();
        assertNotNull(id);

        Assignment newAssignment = manager.findAssignmentById(id);
        assertEquals(assignment, newAssignment);
        assertNotSame(assignment, newAssignment);
    }

    @Test
    public void createAssignmentNull() {
        expectedEx.expect(IllegalArgumentException.class);
        manager.createAssignment(null);
    }

    @Test
    public void createAssignmentWithNullAgent() {
        Assignment assignment = new Assignment(1L, null, goodMission, 1L, 2L);

        expectedEx.expect(IllegalArgumentException.class);
        manager.createAssignment(assignment);
    }

    @Test
    public void createAssignmentWithNullMission() {
        Assignment assignment = new Assignment(1L, goodAgent, null, 1L, 2L);

        expectedEx.expect(IllegalArgumentException.class);
        manager.createAssignment(assignment);
    }

    @Test
    public void createAssignmentEndsSoonerThanStarts() {
        Assignment assignment = new Assignment(1L, goodAgent, goodMission, 2L, 1L);

        expectedEx.expect(IllegalArgumentException.class);
        manager.createAssignment(assignment);
    }

    /* Update assignment */

    @Test
    public void updateAssignment() {
        Agent agent = new Agent(5L, "Jiri Barnat", 12L);
        Mission mission = new Mission(30L, "Make Haskell rule the world", 1, false);
        Assignment assignment = new Assignment(null, agent, mission, 1L, 2L);
        manager.createAssignment(assignment);
        Long id = assignment.getId();
        Assignment newAssignment = manager.findAssignmentById(id);

        newAssignment.setAgent(goodAgent);
        newAssignment.setMission(goodMission);
        newAssignment.setStartTime(3L);
        newAssignment.setEndtime(4L);

        manager.updateAssignment(newAssignment);
        assertNotEquals(assignment, newAssignment);
        assertEquals(assignment.getId(), newAssignment.getId());
        assertNotEquals(assignment.getAgent(), newAssignment.getAgent());
        assertNotEquals(assignment.getMission(), newAssignment.getMission());
        assertNotEquals(assignment.getStartTime(), newAssignment.getStartTime());
        assertNotEquals(assignment.getEndtime(), newAssignment.getEndtime());
    }

    @Test
    public void updateAssignmentNull() {
        Assignment newAssignment = makeAssignment();
        newAssignment.setId(null);

        expectedEx.expect(IllegalArgumentException.class);
        manager.updateAssignment(newAssignment);
    }

    @Test
    public void updateAssignmentWithWrongId() {
        Assignment newAssignment = makeAssignment();
        newAssignment.setId(-1L);

        expectedEx.expect(IllegalArgumentException.class);
        manager.updateAssignment(newAssignment);
    }

    @Test
    public void updateAssignmentWithNullAgent() {
        Assignment newAssignment = makeAssignment();
        newAssignment.setAgent(null);

        expectedEx.expect(IllegalArgumentException.class);
        manager.updateAssignment(newAssignment);
    }

    @Test
    public void updateAssignmentWithNullMission() {
        Assignment newAssignment = makeAssignment();
        newAssignment.setMission(null);

        expectedEx.expect(IllegalArgumentException.class);
        manager.updateAssignment(newAssignment);
    }

    @Test
    public void updateAssignmentEndsSoonerThanStarts() {
        Assignment newAssignment = makeAssignment();
        newAssignment.setStartTime(10L);
        newAssignment.setEndtime(5L);

        expectedEx.expect(IllegalArgumentException.class);
        manager.updateAssignment(newAssignment);
    }

    /* Find assignments */

    @Test
    public void findAllAssignments() {
        Assignment assignment1 = new Assignment(null, goodAgent, goodMission, 1L, 2L);
        Assignment assignment2 = new Assignment(null, new Agent(10L, "Agent 2", 10L),
                                        new Mission(10L, "Mission 2", 1, false), 6L, 8L);
        Assignment assignment3 = new Assignment(null, new Agent(11L, "Agent 3", 20L),
                                        new Mission(11L, "Mission 3", 1, false), 14L, 28L);
        Assignment assignment4 = new Assignment(null, new Agent(12L, "Agent 4", 5318008L),
                                        new Mission(12L, "Mission 4", 1, false), 1350L, 1402L);

        // must be created prior to storing due to id recovery
        manager.createAssignment(assignment1);
        manager.createAssignment(assignment2);
        manager.createAssignment(assignment3);
        manager.createAssignment(assignment4);

        Set<Assignment> storedAssignments = new HashSet<>();
        storedAssignments.add(assignment1);
        storedAssignments.add(assignment2);
        storedAssignments.add(assignment3);
        storedAssignments.add(assignment4);

        Set<Assignment> foundAssignments = manager.findAllAssignments();
        assertTrue(storedAssignments.equals(foundAssignments));
    }

    @Test
    public void findAssignmentsForAgent() {
        // TODO
        // check also for completed missions and zero agent requirement
    }

    @Test
    public void findAssignmentsForMission() {
        // TODO
        // check for border states of requiredAgents
    }

    @Test
    public void requiredAgentsDecrease() {
        Mission mission = new Mission(1L, "Make IS rule the world", 2, false);
        int requiredAgentsBefore = mission.getRequiredAgents();

        Assignment assignment = new Assignment(1L, goodAgent, mission, 6L, 10L);
        int requiredAgentsAfter = mission.getRequiredAgents();

        assertTrue((requiredAgentsBefore - 1) == requiredAgentsAfter);
    }

    /* Delete assignment */

    @Test
    public void deleteAssignment() {
        Assignment assignment = new Assignment(1L, goodAgent, goodMission, 2L, 1L);
        manager.createAssignment(assignment);

        Long id = assignment.getId();
        manager.deleteAssignment(assignment);

        Assignment storedAssignment = manager.findAssignmentById(id);
        assertNull(storedAssignment);
    }

    @Test
    public void deleteAssignmentRequiredAgentsIncrease() {
        Mission mission = new Mission(1L, "Make IS rule the world", 2, false);
        Assignment assignment = new Assignment(1L, goodAgent, mission, 2L, 1L);
        manager.createAssignment(assignment);
        int requiredAgentsBefore = mission.getRequiredAgents();

        manager.deleteAssignment(assignment);
        int requiredAgentsAfter = mission.getRequiredAgents();
        assertTrue((requiredAgentsBefore + 1) == requiredAgentsAfter);
    }

    /* Private helper methods */

    private Assignment makeAssignment() {
        Assignment assignment = new Assignment(null, goodAgent, goodMission, 1L, 2L);
        manager.createAssignment(assignment);
        Long id = assignment.getId();
        Assignment newAssignment = manager.findAssignmentById(id);
        return newAssignment;
    }
}

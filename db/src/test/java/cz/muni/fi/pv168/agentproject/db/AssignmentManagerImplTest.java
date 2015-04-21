package cz.muni.fi.pv168.agentproject.db;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * @author Dužinka
 * @version 30.03.2015
 *
 * TODO: assignMoreAgentsThanPermittedViaUpdate() -- isn't update mission in database enough?
 */
public class AssignmentManagerImplTest {

    private DataSource dataSource;

    private AssignmentManager manager;
    private AgentManager agentManager;
    private MissionManager missionManager;

    private Mission goodMission;
    private Agent goodAgent;


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
            connection.prepareStatement(DbHelper.SQL_DROP_TABLE_AGENT)
                    .executeUpdate();
            connection.prepareStatement(DbHelper.SQL_DROP_TABLE_MISSION)
                    .executeUpdate();
            connection.prepareStatement(DbHelper.SQL_CREATE_TABLE_AGENT)
                    .executeUpdate();
            connection.prepareStatement(DbHelper.SQL_CREATE_TABLE_MISSION)
                    .executeUpdate();
            connection.prepareStatement(DbHelper.SQL_CREATE_TABLE_ASSIGNMENT)
                    .executeUpdate();
        }

        goodMission = new Mission(null, "Make IS rule the world", 2, false);
        goodAgent = new Agent(null, "Michal Brandejs", Instant.ofEpochMilli(10L));

        agentManager = new AgentManagerImpl(dataSource);
        missionManager = new MissionManagerImpl(dataSource);

        agentManager.createAgent(goodAgent);
        missionManager.createMission(goodMission);

        manager = new AssignmentManagerImpl(dataSource, agentManager, missionManager);
    }

    @After
    public void tearDown() throws SQLException {

        try (Connection connection = dataSource.getConnection()) {
            connection.prepareStatement(DbHelper.SQL_DROP_TABLE_ASSIGNMENT)
                    .executeUpdate();
            connection.prepareStatement(DbHelper.SQL_DROP_TABLE_AGENT)
                    .executeUpdate();
            connection.prepareStatement(DbHelper.SQL_DROP_TABLE_MISSION)
                    .executeUpdate();
        }
    }

    /* Create assignment */

    @Test
    public void createAssignment() {
        Assignment assignment = new Assignment(null, goodAgent, goodMission, Instant.ofEpochMilli(1L), Instant.ofEpochMilli(2L));
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
        Assignment assignment = new Assignment(null, null, goodMission, Instant.ofEpochMilli(1L), Instant.ofEpochMilli(2L));

        expectedEx.expect(IllegalArgumentException.class);
        manager.createAssignment(assignment);
    }

    @Test
    public void createAssignmentWithNullMission() {
        Assignment assignment = new Assignment(null, goodAgent, null, Instant.ofEpochMilli(1L), Instant.ofEpochMilli(2L));

        expectedEx.expect(IllegalArgumentException.class);
        manager.createAssignment(assignment);
    }

    @Test
    public void createAssignmentEndsSoonerThanStarts() {
        Assignment assignment = new Assignment(null, goodAgent, goodMission, Instant.ofEpochMilli(2L), Instant.ofEpochMilli(1L));

        expectedEx.expect(IllegalArgumentException.class);
        manager.createAssignment(assignment);
    }

    /* Update assignment */

    @Test
    public void updateAssignment() {
        Assignment assignment = new Assignment(null, goodAgent, goodMission, Instant.ofEpochMilli(1L), Instant.ofEpochMilli(2L));
        manager.createAssignment(assignment);
        Long id = assignment.getId();
        Assignment newAssignment = manager.findAssignmentById(id);

        Agent newAgent = new Agent(null, "Jmeno", Instant.ofEpochMilli(100L));
        agentManager.createAgent(newAgent);

        Mission newMission = new Mission(null, "Kill someone", 3, false);
        missionManager.createMission(newMission);

        newAssignment.setAgent(newAgent);
        newAssignment.setMission(newMission);
        newAssignment.setStartTime(Instant.ofEpochMilli(3L));
        newAssignment.setEndTime(Instant.ofEpochMilli(4L));

        manager.updateAssignment(newAssignment);
        assertNotEquals(assignment, newAssignment);
        assertEquals(assignment.getId(), newAssignment.getId());
        assertNotEquals(assignment.getAgent(), newAssignment.getAgent());
        assertNotEquals(assignment.getMission(), newAssignment.getMission());
        assertNotEquals(assignment.getStartTime(), newAssignment.getStartTime());
        assertNotEquals(assignment.getEndTime(), newAssignment.getEndTime());
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
        newAssignment.setStartTime(Instant.ofEpochMilli(10L));
        newAssignment.setEndTime(Instant.ofEpochMilli(5L));

        expectedEx.expect(IllegalArgumentException.class);
        manager.updateAssignment(newAssignment);
    }

    /* Find assignments */

    @Test
    public void findAllAssignments() {
        makeMultipleAgents();
        List<Agent> agents = new ArrayList<>(agentManager.findAllAgents());
        makeMultipleMissions();
        List<Mission> missions = new ArrayList<>(missionManager.findAllMissions());

        Assignment assignment;
        Set<Assignment> storedAssignments = new HashSet<>();

        int size = agents.size() > missions.size() ? missions.size() : agents.size();
        for(int i = 0; i < size; i++) {
            assignment = makeAssignment(agents.get(i), missions.get(i));
            storedAssignments.add(assignment);
        }

        Set<Assignment> foundAssignments = manager.findAllAssignments();
        assertTrue(storedAssignments.equals(foundAssignments));
    }

    @Test
    public void findAssignmentsForAgent() {
        makeMultipleMissions();
        List<Mission> missions = new ArrayList<>(missionManager.findAllMissions());
        Assignment assignment;
        Set<Assignment> storedAssignments = new HashSet<>();

        for(Mission mission : missions) {
            assignment = makeAssignment(goodAgent, mission);
            storedAssignments.add(assignment);
        }

        Set<Assignment> foundAssignments = manager.findAssignmentsForAgent(goodAgent);
        assertTrue(storedAssignments.equals(foundAssignments));
    }

    @Test
    public void findAssignmentsForMission() {
        makeMultipleAgents();
        List<Agent> agents = new ArrayList<>(agentManager.findAllAgents());
        Assignment assignment;
        Set<Assignment> storedAssignments = new HashSet<>();
        Mission missionWithLotsOfAgents = new Mission(null, "Need more agents", 50, false);
        missionManager.createMission(missionWithLotsOfAgents);

        for(Agent agent : agents) {
            assignment = makeAssignment(agent, missionWithLotsOfAgents);
            storedAssignments.add(assignment);
        }

        Set<Assignment> foundAssignments = manager.findAssignmentsForMission(missionWithLotsOfAgents);
        assertTrue(storedAssignments.equals(foundAssignments));
    }

    /* Delete assignment */

    @Test
    public void deleteAssignment() {
        Assignment assignment = new Assignment(null, goodAgent, goodMission, Instant.ofEpochMilli(2L), Instant.ofEpochMilli(3L));
        manager.createAssignment(assignment);

        Long id = assignment.getId();
        manager.deleteAssignment(assignment);

        Assignment storedAssignment = manager.findAssignmentById(id);
        assertNull(storedAssignment);
    }

    /* Check for valid number of agents in a mission & for valid assignments */

    @Test
    public void requiredAgentsNumberCheck() {
        // goodMission requires 2 agents; make an assignment with goodAgent and goodMission
        Assignment assignment = makeAssignment();
        Agent agent1 = new Agent(null, "Krecek", Instant.ofEpochMilli(5318008L));
        Agent agent2 = new Agent(null, "Cincila", Instant.ofEpochMilli(8086L));
        agentManager.createAgent(agent1);
        agentManager.createAgent(agent2);

        assignment = makeAssignment(agent1, goodMission);
        expectedEx.expect(IllegalArgumentException.class);
        assignment = makeAssignment(agent2, goodMission);
    }

    @Test
    public void assignOneAgentTwice() {
        Assignment assignment1 = makeAssignment();
        expectedEx.expect(Exception.class);
        Assignment assignment2 = makeAssignment();
    }

    @Test
    public void assignOneAgentTwiceViaUpdate() {
        Assignment assignment1 = makeAssignment();

        Agent agent2 = new Agent(null, "Jarda", Instant.ofEpochMilli(2L));
        agentManager.createAgent(agent2);
        Assignment assignment2 = makeAssignment(agent2, goodMission);

        expectedEx.expect(IllegalArgumentException.class);
        assignment2.setAgent(goodAgent);
        manager.updateAssignment(assignment2);
    }

    @Test
    public void assignMoreAgentsThanPermittedViaUpdate() {
        Mission mission = new Mission(null, "Let's fight some bad guys in blood fluid!", 2, false);
        Agent agent1 = new Agent(null, "Red blood cell", Instant.ofEpochMilli(40L));
        Agent agent2 = new Agent(null, "White blood cell", Instant.ofEpochMilli(60L));
        missionManager.createMission(mission);
        agentManager.createAgent(agent1);
        agentManager.createAgent(agent2);
        Assignment assignment1 = makeAssignment(agent1, mission);
        Assignment assignment2 = makeAssignment(agent2, mission);

        expectedEx.expect(IllegalArgumentException.class);
        mission.setRequiredAgents(1);
        missionManager.updateMission(mission);
    }

    @Test
    public void assignZeroRequiredAgentsViaUpdate() {
        Mission mission = new Mission(null, "I don't need anybody! Soon...", 1, false);
        missionManager.createMission(mission);
        Assignment assignment = makeAssignment(goodAgent, mission);

        expectedEx.expect(IllegalArgumentException.class);
        mission.setRequiredAgents(0);
        missionManager.updateMission(mission);
    }

    /* Private helper methods */

    private Assignment makeAssignment() {
        return makeAssignment(goodAgent, goodMission);
    }

    private Assignment makeAssignment(Agent agent, Mission mission) {
        Assignment assignment = new Assignment(null, agent, mission, Instant.ofEpochMilli(1L), Instant.ofEpochMilli(2L));
        manager.createAssignment(assignment);
        Long id = assignment.getId();
        Assignment newAssignment = manager.findAssignmentById(id);
        return newAssignment;
    }

    private void makeMultipleAgents() {
        Agent agent1 = new Agent(null, "Agent First", Instant.ofEpochMilli(10L));
        Agent agent2 = new Agent(null, "Agent Second", Instant.ofEpochMilli(5318008L));
        Agent agent3 = new Agent(null, "Agent Third", Instant.ofEpochMilli(386L));
        Agent agent4 = new Agent(null, "Agent Fourth", Instant.ofEpochMilli(486L));
        Agent agent5 = new Agent(null, "Agent Fifth", Instant.ofEpochMilli(8080L));

        agentManager.createAgent(agent1);
        agentManager.createAgent(agent2);
        agentManager.createAgent(agent3);
        agentManager.createAgent(agent4);
        agentManager.createAgent(agent5);
    }

    private void makeMultipleMissions() {
        Mission mission1 = new Mission(null, "Mission 1", 1, false);
        Mission mission2 = new Mission(null, "Mission 2", 1, false);
        Mission mission3 = new Mission(null, "Mission 3", 1, false);
        Mission mission4 = new Mission(null, "Mission 4", 2, false);
        Mission mission5 = new Mission(null, "Mission 5", 3, false);

        missionManager.createMission(mission1);
        missionManager.createMission(mission2);
        missionManager.createMission(mission3);
        missionManager.createMission(mission4);
        missionManager.createMission(mission5);
    }
}

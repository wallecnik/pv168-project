package cz.muni.fi.agentproject.tests;

import cz.muni.fi.agentproject.*;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

/**
 * Created by Dužinka on 17. 3. 2015.
 */
public class AssignmentManagerImplTest {

    private AssignmentManager manager;


    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Before
    public void setUp() {
        manager = new AssignmentManagerImpl();
    }

    /* Create assignment */

    private Agent createAgent() {
        Agent agent = new Agent(1L, "Michal Brandejs", 10L);
        return agent;
    }

    private Mission createMission() {
        Mission mission = new Mission(1L, "Make IS rule the world", 2, false);
        return mission;
    }

    @Test
    public void createAssignment() {
        Agent agent = createAgent();
        Mission mission = createMission();
        Assignment assignment = new Assignment(1L, agent, mission, 1L, 2L);
        manager.createAssignment(assignment);

        Long id = assignment.getId();
        assertNotNull(id);

        Assignment newAssignment = manager.findAssignmentById(id);
        assertEquals(assignment, newAssignment);
        assertNotSame(assignment, newAssignment);
    }
}

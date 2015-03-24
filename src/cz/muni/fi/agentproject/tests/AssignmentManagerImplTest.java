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
        //TODO: create sample agent and mission
    }

    /* Create assignment */

    private Agent createTestAgent() {
        Agent agent = new Agent(1L, "Michal Brandejs", 10L);
        return agent;
    }

    private Mission createTestMission() {
        Mission mission = new Mission(1L, "Make IS rule the world", 2, false);
        return mission;
    }

    @Test
    public void createAssignment() {
        Agent agent = createTestAgent();
        Mission mission = createTestMission();
        Assignment assignment = new Assignment(1L, agent, mission, 1L, 2L);
        manager.createAssignment(assignment);

        Long id = assignment.getId();
        assertNotNull(id);

        Assignment newAssignment = manager.findAssignmentById(id);
        assertEquals(assignment, newAssignment);
        assertNotSame(assignment, newAssignment);
    }
    
    @Test
    public void createAssignmentWithWrongID() {
        
    }
    
    @Test
    public void createAssignmentWithMissingAgent() {
        
    }
    
    @Test
    public void createAssignmentWithMissingMission() {
        
    }
    
    @Test
    public void createAssignmentWithMissingStartTime() {
        
    }
    
    @Test
    public void createAssignmentWithMissingEndTime() {
        
    }
    
    
}

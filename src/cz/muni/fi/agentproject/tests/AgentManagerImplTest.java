package cz.muni.fi.agentproject.tests;

import cz.muni.fi.agentproject.Agent;
import cz.muni.fi.agentproject.AgentManager;
import cz.muni.fi.agentproject.AgentManagerImpl;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * Created by Dužinka on 7. 3. 2015.
 */
public class AgentManagerImplTest {

    AgentManager manager;

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Before
    public void setUp() {
        manager = new AgentManagerImpl();
    }

    /* Create agent */

    @Test
    public void createAgent() {
        Agent agent = new Agent(1L, "Michal Brandejs", 10L, "Make IS rule the world");
        manager.createAgent(agent);

        Long id = agent.getId();
        assertNotNull(id);

        Agent storedAgent = manager.findAgentById(id);
        assertEquals(agent, storedAgent);
        assertNotSame(agent, storedAgent);
    }

    @Test
    public void createAgentNull() {
        expectedEx.expect(NullPointerException.class);
        expectedEx.expectMessage("Null agent (object)");
        manager.createAgent(null);
    }

    @Test
     public void createAgentWithWrongId() {
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("ID cannot be <= 0");
        Agent agent = new Agent(-1L, "name", 10L, "description");
        manager.createAgent(agent);
    }

    @Test
    public void createAgentWithNullName() {
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("Name is null");
        Agent agent = new Agent(1L, null, 10L, "description");
        manager.createAgent(agent);
    }

    @Test
    public void createAgentWithEmptyName() {
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("Name is empty");
        Agent agent = new Agent(1L, "", 10L, "description");
        manager.createAgent(agent);
    }

    @Test
    public void createAgentNameWithIllegalSymbols() {
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("Name contains illegal symbols");
        Agent agent = new Agent(1L, "lol_#yolo^2", 10L, "description");
        manager.createAgent(agent);
        // regex which might help: name.matches("[a-zA-Z]+");
    }

    @Test
    public void createAgentNullBorn() {
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("Born is null");
        Agent agent = new Agent(1L, "name", null, "description");
        manager.createAgent(agent);
    }

    /*
    Didn't work with Date & Calendar before, not sure if set correctly
     */
    @Test
    public void createAgentBornMoreThan100yo() {
        try {
            Calendar cal = Calendar.getInstance();
            //cal.setTime(new Date());
            cal.add(Calendar.YEAR, -100);
            Date dateBefore100Years = cal.getTime();

            Agent agent = new Agent(1L, "name", dateBefore100Years.getTime(), "bornMoreThan100yo");
            manager.createAgent(agent);
            fail("Agent is more than 100 years old");
        }
        catch (IllegalArgumentException ok) {
        }
    }

    @Test
    public void createAgentNotBornYet() {
        try {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_MONTH, +1);
            Date datePlus1Day = cal.getTime();

            Agent agent = new Agent(1L, "name", datePlus1Day.getTime(), "notBornYet");
            manager.createAgent(agent);
            fail("Agent is not born yet");
        }
        catch (IllegalArgumentException ok) {
        }
    }

    @Test
    public void createAgentNullDescription() {
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("Born is null");
        Agent agent = new Agent(1L, "name", 10L, null);
        manager.createAgent(agent);
    }


    /* Update agent */

    private Agent makeAgent() {
        Agent agent = new Agent(1L, "Michal Brandejs", 10L, "Make IS rule the world");
        manager.createAgent(agent);
        Long id = agent.getId();
        Agent newAgent = manager.findAgentById(id);
        return newAgent;
    }

    @Test
    public void updateAgent() {
        Agent agent = new Agent(1L, "Michal Brandejs", 10L, "Make IS rule the world");
        manager.createAgent(agent);
        Long id = agent.getId();
        Agent newAgent = manager.findAgentById(id);

        newAgent.setName("Jiri Barnat");
        newAgent.setBorn(20L);
        newAgent.setDescription("Make Haskell rule the world");

        manager.updateAgent(newAgent);
        assertNotEquals(agent, newAgent);
        assertEquals(agent.getId(), newAgent.getId());
        assertNotEquals(agent.getName(), newAgent.getName());
        assertNotEquals(agent.getBorn(), newAgent.getBorn());
        assertNotEquals(agent.getDescription(), newAgent.getDescription());
    }

    @Test
    public void updateAgentNull() {
        expectedEx.expect(NullPointerException.class);
        expectedEx.expectMessage("Null agent (object)");
        Agent newAgent = makeAgent();
        newAgent.setId(Long.MAX_VALUE);
        manager.updateAgent(newAgent);
    }

    @Test
    public void updateAgentWithWrongId() {
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("ID cannot be <= 0");
        Agent newAgent = makeAgent();
        newAgent.setId(-1L);
        manager.updateAgent(newAgent);
    }

    @Test
    public void updateAgentWithNullName() {
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("Name is null");
        Agent newAgent = makeAgent();
        newAgent.setName(null);
        manager.updateAgent(newAgent);
    }

    @Test
    public void updateAgentWithEmptyName() {
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("Name is empty");
        Agent newAgent = makeAgent();
        newAgent.setName("");
        manager.updateAgent(newAgent);
    }

    @Test
    public void updateAgentNameWithIllegalSymbols() {
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("Name contains illegal symbols");
        Agent newAgent = makeAgent();
        newAgent.setName("lol_#yolo^2");
        manager.updateAgent(newAgent);
    }

    @Test
    public void updateAgentNullBorn() {
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("Born is null");
        Agent newAgent = makeAgent();
        newAgent.setBorn(null);
        manager.updateAgent(newAgent);
    }

    @Test
    public void updateAgentBornMoreThan100yo() {
        Agent agent = makeAgent();

        try {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.YEAR, -100);
            Date dateBefore100Years = cal.getTime();

            agent.setBorn(dateBefore100Years.getTime());
            manager.updateAgent(agent);
            fail("updateAgent: Agent is more than 100 years old");
        }
        catch (IllegalArgumentException ok) {
        }
    }

    @Test
    public void updateAgentNotBornYet() {
        Agent agent = makeAgent();

        try {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_MONTH, +1);
            Date datePlus1Day = cal.getTime();

            agent.setBorn(datePlus1Day.getTime());
            manager.updateAgent(agent);
            fail("updateAgent: Agent is not born yet");
        }
        catch (IllegalArgumentException ok) {
        }
    }

    @Test
    public void updateAgentNullDescription() {
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("Born is null");
        Agent newAgent = makeAgent();
        newAgent.setDescription(null);
        manager.updateAgent(newAgent);
    }

    @Test
    public void deleteAgent() {
        Agent agent = new Agent(1L, "Michal Brandejs", 10L, "Make IS rule the world");
        manager.createAgent(agent);

        Long id = agent.getId();
        manager.deleteAgent(agent);

        Agent storedAgent = manager.findAgentById(id);
        assertNull(storedAgent);
    }
}


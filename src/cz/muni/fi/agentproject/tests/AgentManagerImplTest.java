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
        Agent agent = new Agent(1L, "Michal Brandejs", 10L);
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
        Agent agent = new Agent(-1L, "name", 10L);

        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("ID cannot be <= 0");
        manager.createAgent(agent);
    }

    @Test
    public void createAgentWithNullName() {
        Agent agent = new Agent(1L, null, 10L);

        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("Name is null");
        manager.createAgent(agent);
    }

    @Test
    public void createAgentWithEmptyName() {
        Agent agent = new Agent(1L, "", 10L);

        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("Name is empty");
        manager.createAgent(agent);
    }

    @Test
    public void createAgentNameWithIllegalSymbols() {
        Agent agent = new Agent(1L, "lol_#yolo^2", 10L);

        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("Name contains illegal symbols");
        manager.createAgent(agent);
        // regex which might help: name.matches("[a-zA-Z]+");
    }

    /*
    Didn't work with Date & Calendar before, not sure if set correctly
     */
    @Test
    public void createAgentBornMoreThan100yo() {
        Calendar cal = Calendar.getInstance();
        //cal.setTime(new Date());
        cal.add(Calendar.YEAR, -100);
        Date dateBefore100Years = cal.getTime();

        Agent agent = new Agent(1L, "name", dateBefore100Years.getTime());

        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("Agent would be too old");
        manager.createAgent(agent);
    }

    @Test
    public void createAgentNotBornYet() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, +1);
        Date datePlus1Day = cal.getTime();

        Agent agent = new Agent(1L, "name", datePlus1Day.getTime());

        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("Born is in future");
        manager.createAgent(agent);
    }


    /* Update agent */

    private Agent makeAgent() {
        Agent agent = new Agent(1L, "Michal Brandejs", 10L);
        manager.createAgent(agent);
        Long id = agent.getId();
        Agent newAgent = manager.findAgentById(id);
        return newAgent;
    }

    @Test
    public void updateAgent() {
        Agent agent = new Agent(1L, "Michal Brandejs", 10L);
        manager.createAgent(agent);
        Long id = agent.getId();
        Agent newAgent = manager.findAgentById(id);

        newAgent.setName("Jiri Barnat");
        newAgent.setBorn(20L);

        manager.updateAgent(newAgent);
        assertNotEquals(agent, newAgent);
        assertEquals(agent.getId(), newAgent.getId());
        assertNotEquals(agent.getName(), newAgent.getName());
        assertNotEquals(agent.getBorn(), newAgent.getBorn());
    }

    @Test
    public void updateAgentNull() {
        Agent newAgent = makeAgent();
        newAgent.setId(Long.MAX_VALUE);

        expectedEx.expect(NullPointerException.class);
        expectedEx.expectMessage("Null agent (object)");
        manager.updateAgent(newAgent);
    }

    @Test
    public void updateAgentWithWrongId() {
        Agent newAgent = makeAgent();
        newAgent.setId(-1L);

        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("ID cannot be <= 0");
        manager.updateAgent(newAgent);
    }

    @Test
    public void updateAgentWithNullName() {
        Agent newAgent = makeAgent();
        newAgent.setName(null);

        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("Name is null");
        manager.updateAgent(newAgent);
    }

    @Test
    public void updateAgentWithEmptyName() {
        Agent newAgent = makeAgent();
        newAgent.setName("");

        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("Name is empty");
        manager.updateAgent(newAgent);
    }

    @Test
    public void updateAgentNameWithIllegalSymbols() {
        Agent newAgent = makeAgent();
        newAgent.setName("lol_#yolo^2");

        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("Name contains illegal symbols");
        manager.updateAgent(newAgent);
    }

    @Test
    public void updateAgentBornMoreThan100yo() {
        Agent agent = makeAgent();

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -100);
        Date dateBefore100Years = cal.getTime();

        agent.setBorn(dateBefore100Years.getTime());

        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("Agent would be too old");
        manager.updateAgent(agent);
    }

    @Test
    public void updateAgentNotBornYet() {
        Agent agent = makeAgent();

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, +1);
        Date datePlus1Day = cal.getTime();

        agent.setBorn(datePlus1Day.getTime());

        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("Born is in future");
        manager.updateAgent(agent);
    }

    @Test
    public void deleteAgent() {
        Agent agent = new Agent(1L, "Michal Brandejs", 10L);
        manager.createAgent(agent);

        Long id = agent.getId();
        manager.deleteAgent(agent);

        Agent storedAgent = manager.findAgentById(id);
        assertNull(storedAgent);
    }
}


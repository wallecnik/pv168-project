package cz.muni.fi.agentproject.tests;

import cz.muni.fi.agentproject.Agent;
import cz.muni.fi.agentproject.AgentManager;
import cz.muni.fi.agentproject.AgentManagerImpl;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * Created by Dužinka on 7. 3. 2015.
 */
public class AgentManagerImplTest {

    AgentManager manager;

    @Before
    public void setUp() {
        manager = new AgentManagerImpl();
    }

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
    public void createAgentWithWrongAttributes() {

        //null
        try {
            manager.createAgent(null);
            fail("Null agent (object)");
        }
        catch (IllegalArgumentException iae) {
            //OK
        }

        //id <= 0
        try {
            Agent agent = new Agent(0L, "name", 10L, "id");
            manager.createAgent(agent);

            Agent agent2 = new Agent(-1L, "name", 10L, "id");
            manager.createAgent(agent2);
            fail("ID cannot be <= 0");
        }
        catch (IllegalArgumentException iae) {
            //OK
        }

        //null name
        try {
            Agent agent = new Agent(1L, null, 10L, "name");
            manager.createAgent(agent);
            fail("Name is null");
        }
        catch (IllegalArgumentException iae) {
            //OK
        }

        //empty name
        try {
            Agent agent = new Agent(1L, "", 10L, "name");
            manager.createAgent(agent);
            fail("Name is empty");
        }
        catch (IllegalArgumentException iae) {
            //OK
        }

        //name with illegal symbols (including diacritics)
        try {
            Agent agent = new Agent(1L, "lol_#yolo^2", 10L, "name");
            manager.createAgent(agent);
            fail("Name contains illegal symbols");
            // regex which might help: name.matches("[a-zA-Z]+");
        }
        catch (IllegalArgumentException iae) {
            //OK
        }

        //null born
        try {
            Agent agent = new Agent(1L, "name", null, "born");
            manager.createAgent(agent);
            fail("Born is null");
        }
        catch (IllegalArgumentException iae) {
            //OK
        }

        //null description
        try {
            Agent agent = new Agent(1L, "name", 10L, null);
            manager.createAgent(agent);
            fail("Description is null");
        }
        catch (IllegalArgumentException iae) {
            //OK
        }
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
        catch (IllegalArgumentException iae) {
            //OK
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
        catch (IllegalArgumentException iae) {
            //OK
        }
    }

    @Test
    public void updateAgent() {

        Agent agent = new Agent(1L, "Michal Brandejs", 10L, "Make IS rule the world");
        manager.createAgent(agent);

        Long id = agent.getId();
        Agent storedAgent = manager.findAgentById(id);
        storedAgent.setName("Jiri Barnat");
        storedAgent.setBorn(20L);
        storedAgent.setDescription("Make Haskell rule the world");

        manager.updateAgent(storedAgent);
        assertNotEquals(agent, storedAgent);
        assertEquals(agent.getId(), storedAgent.getId());
        assertNotEquals(agent.getName(), storedAgent.getName());
        assertNotEquals(agent.getBorn(), storedAgent.getBorn());
        assertNotEquals(agent.getDescription(), storedAgent.getDescription());

    }

    @Test
    public void updateAgentWithWrongAttributes() {

        Agent agent = new Agent(1L, "Michal Brandejs", 10L, "Make IS rule the world");
        manager.createAgent(agent);

        Long id = agent.getId();
        Agent storedAgent = manager.findAgentById(id);

        //no such id
        try {
            storedAgent.setId(Long.MAX_VALUE);
            manager.updateAgent(storedAgent);
            fail("No such id");
        }
        catch (IllegalArgumentException iae) {
            //OK
        }

        //null name
        try {
            storedAgent.setName(null);
            manager.updateAgent(storedAgent);
            fail("Name is null");
        }
        catch (IllegalArgumentException iae) {
            //OK
        }

        //empty name
        try {
            storedAgent.setName("");
            manager.updateAgent(storedAgent);
            fail("Name is empty");
        }
        catch (IllegalArgumentException iae) {
            //OK
        }

        //name with illegal symbols (including diacritics)
        try {
            storedAgent.setName("lol_#yolo^2");
            manager.updateAgent(storedAgent);
            fail("Name contains illegal symbols");
        }
        catch (IllegalArgumentException iae) {
            //OK
        }

        //null born
        try {
            storedAgent.setBorn(null);
            manager.updateAgent(storedAgent);
            fail();
        }
        catch (IllegalArgumentException iae) {
            //OK
        }

        //null description
        try {
            storedAgent.setDescription(null);
            manager.updateAgent(storedAgent);
            fail("Description is null");
        }
        catch (IllegalArgumentException iae) {
            //OK
        }
    }

    @Test
    public void updateAgentBornMoreThan100yo() {
        Agent agent = new Agent(1L, "Michal Brandejs", 10L, "Make IS rule the world");
        manager.createAgent(agent);

        Long id = agent.getId();
        Agent storedAgent = manager.findAgentById(id);

        try {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.YEAR, -100);
            Date dateBefore100Years = cal.getTime();

            storedAgent.setBorn(dateBefore100Years.getTime());
            manager.updateAgent(storedAgent);
            fail("updateAgent: Agent is more than 100 years old");
        }
        catch (IllegalArgumentException iae) {
            //OK
        }
    }

    @Test
    public void updateAgentNotBornYet() {
        Agent agent = new Agent(1L, "Michal Brandejs", 10L, "Make IS rule the world");
        manager.createAgent(agent);

        Long id = agent.getId();
        Agent storedAgent = manager.findAgentById(id);

        try {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_MONTH, +1);
            Date datePlus1Day = cal.getTime();

            storedAgent.setBorn(datePlus1Day.getTime());
            manager.updateAgent(storedAgent);
            fail("updateAgent: Agent is not born yet");
        }
        catch (IllegalArgumentException iae) {
            //OK
        }
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


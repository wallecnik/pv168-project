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
import java.util.*;

import static org.junit.Assert.*;

/**
 * Test for implementation AgentManagerImpl of an AgentManager interface
 *
 * @author  Wallecnik
 * @author  Dužinka
 * @version 23.3.2015
 */
public class AgentManagerImplTest {

    private AgentManager manager;
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
            connection.prepareStatement(DbHelper.SQL_DROP_TABLE_AGENT)
                    .executeUpdate();
            connection.prepareStatement(DbHelper.SQL_CREATE_TABLE_AGENT)
                    .executeUpdate();
        }

        manager = new AgentManagerImpl(dataSource);
    }

    @After
    public void tearDown() throws SQLException {
        try (Connection connection = dataSource.getConnection()){
            connection.prepareStatement(DbHelper.SQL_DROP_TABLE_AGENT)
                    .executeUpdate();
        }
    }

    /* Create agent */

    @Test
    public void createAgent() {
        Agent agent = new Agent(null, "Michal Brandejs", Instant.ofEpochMilli(10L));
        manager.createAgent(agent);

        Long id = agent.getId();
        assertNotNull(id);

        Agent storedAgent = manager.findAgentById(id);
        assertEquals(agent, storedAgent);
        assertNotSame(agent, storedAgent);
    }

    @Test
    public void createAgentNull() {
        expectedEx.expect(IllegalArgumentException.class);
        manager.createAgent(null);
    }

    @Test
    public void createAgentWithNullName() {
        Agent agent = new Agent(null, null, Instant.ofEpochMilli(10L));

        expectedEx.expect(IllegalArgumentException.class);
        manager.createAgent(agent);
    }

    @Test
    public void createAgentWithEmptyName() {
        Agent agent = new Agent(null, "", Instant.ofEpochMilli(10L));

        expectedEx.expect(IllegalArgumentException.class);
        manager.createAgent(agent);
    }

    @Test
    public void createAgentNameWithIllegalSymbols() {
        Agent agent = new Agent(null, "lol_#yolo^2", Instant.ofEpochMilli(10L));

        expectedEx.expect(IllegalArgumentException.class);
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
        cal.add(Calendar.YEAR, -101);
        Date dateBefore100Years = cal.getTime();

        Agent agent = new Agent(null, "name", Instant.ofEpochMilli(dateBefore100Years.getTime()));

        expectedEx.expect(IllegalArgumentException.class);
        manager.createAgent(agent);
    }

    @Test
    public void createAgentNotBornYet() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, +1);
        Date datePlus1Day = cal.getTime();

        Agent agent = new Agent(null, "name", Instant.ofEpochMilli(datePlus1Day.getTime()));

        expectedEx.expect(IllegalArgumentException.class);
        manager.createAgent(agent);
    }

    /* Update agent */

    @Test
    public void updateAgent() {
        Agent agent = new Agent(null, "Michal Brandejs", Instant.ofEpochMilli(10L));
        manager.createAgent(agent);
        Long id = agent.getId();
        Agent newAgent = manager.findAgentById(id);

        newAgent.setName("Jiri Barnat");
        newAgent.setBorn(Instant.ofEpochMilli(20L));

        manager.updateAgent(newAgent);
        assertNotEquals(agent, newAgent);
        assertEquals(agent.getId(), newAgent.getId());
        assertNotEquals(agent.getName(), newAgent.getName());
        assertNotEquals(agent.getBorn(), newAgent.getBorn());
    }

    @Test
    public void updateAgentNull() {
        Agent newAgent = makeAgent();
        newAgent.setId(null);

        expectedEx.expect(IllegalArgumentException.class);
        manager.updateAgent(newAgent);
    }

    @Test
    public void updateAgentWithWrongId() {
        Agent newAgent = makeAgent();
        newAgent.setId(-1L);

        expectedEx.expect(IllegalArgumentException.class);
        manager.updateAgent(newAgent);
    }

    @Test
    public void updateAgentWithNullName() {
        Agent newAgent = makeAgent();
        newAgent.setName(null);

        expectedEx.expect(IllegalArgumentException.class);
        manager.updateAgent(newAgent);
    }

    @Test
    public void updateAgentWithEmptyName() {
        Agent newAgent = makeAgent();
        newAgent.setName("");

        expectedEx.expect(IllegalArgumentException.class);
        manager.updateAgent(newAgent);
    }

    @Test
    public void updateAgentNameWithIllegalSymbols() {
        Agent newAgent = makeAgent();
        newAgent.setName("lol_#yolo^2");

        expectedEx.expect(IllegalArgumentException.class);
        manager.updateAgent(newAgent);
    }

    @Test
    public void updateAgentBornMoreThan100yo() {
        Agent agent = makeAgent();

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -101);
        Date dateBefore100Years = cal.getTime();

        agent.setBorn(Instant.ofEpochMilli(dateBefore100Years.getTime()));

        expectedEx.expect(IllegalArgumentException.class);
        manager.updateAgent(agent);
    }

    @Test
    public void updateAgentNotBornYet() {
        Agent agent = makeAgent();

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, +1);
        Date datePlus1Day = cal.getTime();

        agent.setBorn(Instant.ofEpochMilli(datePlus1Day.getTime()));

        expectedEx.expect(IllegalArgumentException.class);
        manager.updateAgent(agent);
    }

    /* Find all agents */

    @Test
    public void findAllAgents() {
        Agent agent1 = new Agent(null, "Michal Brandejs", Instant.ofEpochMilli(10L));
        Agent agent2 = new Agent(null, "Jiri Barnat", Instant.ofEpochMilli(8L));
        Agent agent3 = new Agent(null, "Petr Beran", Instant.ofEpochMilli(5318008L));
        Agent agent4 = new Agent(null, "Petr Hasil", Instant.ofEpochMilli(15L));

        // must be created prior to storing due to id recovery
        manager.createAgent(agent1);
        manager.createAgent(agent2);
        manager.createAgent(agent3);
        manager.createAgent(agent4);

        Set<Agent> storedAgents = new HashSet<>();
        storedAgents.add(agent1);
        storedAgents.add(agent2);
        storedAgents.add(agent3);
        storedAgents.add(agent4);

        List<Agent> foundAgents = manager.findAllAgents();
        assertTrue(storedAgents.equals(foundAgents));
        }

    /* Delete agent */

    @Test
    public void deleteAgent() {
        Agent agent = new Agent(null, "Michal Brandejs", Instant.ofEpochMilli(10L));
        manager.createAgent(agent);

        Long id = agent.getId();
        manager.deleteAgent(agent);

        Agent storedAgent = manager.findAgentById(id);
        assertNull(storedAgent);
    }

    /* Private helper methods */

    private Agent makeAgent() {
        Agent agent = new Agent(null, "Michal Brandejs", Instant.ofEpochMilli(10L));
        manager.createAgent(agent);
        Long id = agent.getId();
        Agent newAgent = manager.findAgentById(id);
        return newAgent;
    }
}


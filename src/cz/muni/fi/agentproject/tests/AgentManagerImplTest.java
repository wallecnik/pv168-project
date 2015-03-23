package cz.muni.fi.agentproject.tests;

import cz.muni.fi.agentproject.Agent;
import cz.muni.fi.agentproject.AgentManager;
import cz.muni.fi.agentproject.AgentManagerImpl;
import cz.muni.fi.agentproject.DbHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import org.apache.commons.dbcp2.BasicDataSource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * Created by Dužinka on 7. 3. 2015.
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
        Agent agent = new Agent(null, "Michal Brandejs", 10L);
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
        Agent agent = new Agent(null, null, 10L);

        expectedEx.expect(IllegalArgumentException.class);
        manager.createAgent(agent);
    }

    @Test
    public void createAgentWithEmptyName() {
        Agent agent = new Agent(null, "", 10L);

        expectedEx.expect(IllegalArgumentException.class);
        manager.createAgent(agent);
    }

    @Test
    public void createAgentNameWithIllegalSymbols() {
        Agent agent = new Agent(null, "lol_#yolo^2", 10L);

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

        Agent agent = new Agent(null, "name", dateBefore100Years.getTime());

        expectedEx.expect(IllegalArgumentException.class);
        manager.createAgent(agent);
    }

    @Test
    public void createAgentNotBornYet() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, +1);
        Date datePlus1Day = cal.getTime();

        Agent agent = new Agent(null, "name", datePlus1Day.getTime());

        expectedEx.expect(IllegalArgumentException.class);
        manager.createAgent(agent);
    }


    /* Update agent */

    private Agent makeAgent() {
        Agent agent = new Agent(null, "Michal Brandejs", 10L);
        manager.createAgent(agent);
        Long id = agent.getId();
        Agent newAgent = manager.findAgentById(id);
        return newAgent;
    }

    @Test
    public void updateAgent() {
        Agent agent = new Agent(null, "Michal Brandejs", 10L);
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

        agent.setBorn(dateBefore100Years.getTime());

        expectedEx.expect(IllegalArgumentException.class);
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
        manager.updateAgent(agent);
    }

    @Test
    public void deleteAgent() {
        Agent agent = new Agent(null, "Michal Brandejs", 10L);
        manager.createAgent(agent);

        Long id = agent.getId();
        manager.deleteAgent(agent);

        Agent storedAgent = manager.findAgentById(id);
        assertNull(storedAgent);
    }
}


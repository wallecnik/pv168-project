package cz.muni.fi.agentproject;

import javax.sql.DataSource;
import java.sql.*;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * @author  Wallecnik
 * @version 31.2.2015
 */
public class AgentManagerImpl implements AgentManager {

    public static final Logger logger = Logger.getLogger(AgentManagerImpl.class.getName());

    private DataSource dataSource;

    public AgentManagerImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void createAgent(Agent agent) throws ServiceFailureException {

        validateAgent(agent);

        if (agent.getId() != null) throw new IllegalArgumentException("agent id is not null");

        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(
                    DbHelper.SQL_INSERT_INTO_AGENT, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, agent.getName());
                ps.setLong(2, agent.getBorn());

                int addedRows = ps.executeUpdate();
                if (addedRows != 1) {
                    throw new ServiceFailureException("Internal Error: More rows "
                            + "inserted when trying to insert agent " + agent);
                }

                ResultSet keyRS = ps.getGeneratedKeys();
                Long newId = this.getKeyFromRS(keyRS, agent);
                agent.setId(newId);
            }
        }
        catch (SQLException sqle) {
            logger.log(Level.SEVERE, null, sqle);
            throw new ServiceFailureException("Error when inserting agent " + agent, sqle);
        }

    }

    @Override
    public Agent findAgentById(Long id) {

        Agent retVal= null;

        if (id == null) throw new IllegalArgumentException("id is null");

        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(
                    DbHelper.SQL_SELECT_SINGLE_AGENT)) {
                ps.setLong(1, id);
                ResultSet rs = ps.executeQuery();

                if (rs.first()) {
                    Agent agent = resultSetToAgent(rs);

                    if (rs.next()) {
                        throw new ServiceFailureException( "Internal error: More entities with the same id found " +
                                "(source id: " + id + ", found " + agent + " and " + resultSetToAgent(rs));
                    }

                    retVal = agent;
                }
                else {
                    retVal = null;
                }
            }

        } catch (SQLException sqle) {
            logger.log(Level.SEVERE, null, sqle);
            throw new ServiceFailureException("Error when looking up an agent with id" + id, sqle);
        }

        return retVal;
    }

    @Override
    public void updateAgent(Agent agent) {

        validateAgent(agent);

        if (agent.getId() == null) throw new IllegalArgumentException("Agent with null id cannot be updated");
        if (agent.getId() <= 0) throw new IllegalArgumentException("Agent's id is less than zero");

        try (Connection conn = dataSource.getConnection()) {
            try(PreparedStatement ps = conn.prepareStatement(DbHelper.SQL_UPDATE_SINGLE_AGENT)) {

                ps.setString(1, agent.getName());
                ps.setLong(2, agent.getBorn());
                ps.setLong(3, agent.getId());

                int addedRows = ps.executeUpdate();
                if(addedRows != 1) {
                    throw new IllegalArgumentException("Unable to update agent " + agent);
                }
            }
        } catch (SQLException sqle) {
            logger.log(Level.SEVERE, null, sqle);
            throw new ServiceFailureException("Error when updating an agent" + agent, sqle);
        }
    }

    @Override
    public void deleteAgent(Agent agent) {
        if (agent == null) throw new IllegalArgumentException("Agent pointer is null");
        if (agent.getId() == null) throw new IllegalArgumentException("Agent with null id cannot be deleted");
        if (agent.getId() <= 0) throw new IllegalArgumentException("Agent's id is less than zero");

        try (Connection conn = dataSource.getConnection()) {
            try(PreparedStatement ps = conn.prepareStatement(DbHelper.SQL_DELETE_SINGLE_AGENT)) {
                Long agentId = agent.getId();
                ps.setLong(1, agentId);

                int addedRows = ps.executeUpdate();
                if(addedRows != 1) {
                    throw new ServiceFailureException("Did not delete agent with id =" + agentId);
                }
            }
        } catch (SQLException sqle) {
            logger.log(Level.SEVERE, null, sqle);
            throw new ServiceFailureException("Unable to delete an agent" + agent, sqle);
        }
    }

    @Override
    public List<Agent> findAllAgents() {

        List<Agent> retList = new ArrayList<Agent>();

        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(
                    DbHelper.SQL_SELECT_ALL_AGENTS)) {
                ResultSet rs = ps.executeQuery();

                rs.beforeFirst();

                while (rs.next()) {
                    Agent agent = resultSetToAgent(rs);

                    retList.add(agent);
                }
            }

        } catch (SQLException sqle) {
            logger.log(Level.SEVERE, null, sqle);
            throw new ServiceFailureException("Error when looking up all agents", sqle);
        }

        return retList;

    }

    private Long getKeyFromRS(ResultSet resultSet, Agent agent) throws SQLException {

        Long newId;

        if (resultSet.first()) {
            if (resultSet.getMetaData().getColumnCount() != 1) {
                throw new ServiceFailureException("Internal Error: Generated key"
                        + "retrieving failed when trying to insert agent " + agent
                        + " - wrong key fields count: " + resultSet.getMetaData().getColumnCount());
            }

            newId = resultSet.getLong(1);

            if (! resultSet.isLast()) {
                throw new ServiceFailureException("Internal Error: Generated key"
                        + "retrieving failed when trying to insert agent " + agent
                        + " - more keys found");
            }
        }
        else {
            throw new ServiceFailureException("Internal Error: Generated key"
                    + "retrieving failed when trying to insert agent " + agent
                    + " - no key found");
        }

        return newId;
    }

    private Agent resultSetToAgent(ResultSet resultSet) throws SQLException {

        Long id     = resultSet.getLong(DbContract.COLUMN_AGENT_ID);
        String name = resultSet.getString(DbContract.COLUMN_AGENT_NAME);
        long born   = resultSet.getLong(DbContract.COLUMN_AGENT_BORN);

        Agent agent = new Agent(id, name, born);

        return agent;
    }

    private void validateAgent(Agent agent) throws IllegalArgumentException {
        if (agent == null) throw new IllegalArgumentException("agent is null");
        if (agent.getName() == null) throw new IllegalArgumentException("agent name is null");
        if (agent.getName().equals("")) throw new IllegalArgumentException("agent name is empty");
        if (agent.getName().length() > Constants.NAME_MAX_LENGTH) throw new IllegalArgumentException("agent name is too long");
        if (! Pattern.matches(Constants.NAME_REGEX, agent.getName())) throw new IllegalArgumentException("agent name contains illegal characters");
        ZonedDateTime agentBorn = ZonedDateTime.ofInstant(Instant.ofEpochMilli(agent.getBorn()), ZoneId.systemDefault());
        if (agentBorn.compareTo(ZonedDateTime.now()) > 0) throw new IllegalArgumentException("Agent not born yet");
        if (agentBorn.plusYears(100).compareTo(ZonedDateTime.now()) < 0) throw new IllegalArgumentException("Agent is too old");
    }

}

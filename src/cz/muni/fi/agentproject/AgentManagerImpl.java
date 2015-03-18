package cz.muni.fi.agentproject;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author  Wallecnik
 * @version 31.2.2015
 */
public class AgentManagerImpl implements AgentManager {

    private static final int NAME_MAX_LENGTH = 255;

    public static final Logger logger = Logger.getLogger(AgentManagerImpl.class.getName());

    private DataSource dataSource;

    public AgentManagerImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void createAgent(Agent agent) throws ServiceFailureException {

        if (agent == null) throw new IllegalArgumentException("agent is null");
        if (agent.getId() != null) throw new IllegalArgumentException("agent id is not null");
        if (agent.getName() == null) throw new IllegalArgumentException("agent name is null");
        if (agent.getName().equals("")) throw new IllegalArgumentException("agent name is empty");
        if (agent.getName().length() > NAME_MAX_LENGTH) throw new IllegalArgumentException("agent name is empty");
        //TODO: implement born protection - toto dopisu ja :-)

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

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return retVal;
    }

    @Override
    public void updateAgent(Agent agent) {
        throw new ServiceFailureException("Not yet implemented");
    }

    @Override
    public void deleteAgent(Agent agent) {
        throw new ServiceFailureException("Not yet implemented");
    }

    @Override
    public List<Agent> findAllAgents() {
        return null;
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

}

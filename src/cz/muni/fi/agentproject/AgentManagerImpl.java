package cz.muni.fi.agentproject;

import javax.sql.DataSource;
import java.sql.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * This class manages the database for creating, retrieving, updating and deleting Agents.
 * This implementation follows recommendations given by AgentManager interface's JavaDoc.
 *
 * This class uses thread safe DataSource interface for connecting to database.
 *
 * @author  Wallecnik
 * @author  Dužinka
 * @version 24.3.2015
 */
public class AgentManagerImpl extends AbstractManager implements AgentManager {

    public static final Logger logger = Logger.getLogger(AgentManagerImpl.class.getName());

    private DataSource dataSource;

    /**
     * Constructor for objects of this class. Takes one argument implementing DataSource interface
     * which it uses for connecting to database.
     *
     * @param dataSource reference to object of type DataSource
     */
    public AgentManagerImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Adds a new Agent into the database. Provided Agent must have valid values associated. If not, the method
     * throws an IllegalArgumentException. Possible causes are, that an agent's name contains illegal
     * characters or agent is too old or not born yet. Complete list would be too long - see
     * validateAgent() method for all of them.
     *
     * @param agent  A well formed instance of an Agent
     * @throws ServiceFailureException   if an error occurred when using the database
     * @throws IllegalArgumentException  if an illegal state of Agent was provided
     */
    @Override
    public void createAgent(Agent agent) throws ServiceFailureException, IllegalArgumentException {

        validateAgent(agent);

        if (agent.getId() != null) {
            throw new IllegalArgumentException("agent id is not null");
        }

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     DbHelper.SQL_INSERT_INTO_AGENT, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, agent.getName());
            ps.setTimestamp(2, Timestamp.from(agent.getBorn()));

            int addedRows = ps.executeUpdate();
            if (addedRows != 1) {
                throw new ServiceFailureException("Internal Error: More rows "
                        + "inserted when trying to insert agent " + agent);
            }

            ResultSet keyRS = ps.getGeneratedKeys();
            Long newId = this.getKeyFromRS(keyRS, agent);
            agent.setId(newId);

        }
        catch (SQLException sqle) {
            logger.log(Level.SEVERE, null, sqle);
            throw new ServiceFailureException("Error when inserting agent " + agent, sqle);
        }

    }

    /**
     * Retrieves an Agent from the database or returns null if no agent with provided id
     * was found.
     *
     * @param id  id of an Agent to look for
     * @return  a new instance of an Agent or null
     * @throws ServiceFailureException   if more record's were found or another error occurred
     * @throws IllegalArgumentException  if the provided id is null or negative
     */
    @Override
    public Agent findAgentById(Long id) throws ServiceFailureException, IllegalArgumentException{

        Agent retVal;

        if (id == null) {
            throw new IllegalArgumentException("id is null");
        }
        if (id <= 0) {
            throw new IllegalArgumentException("id is negative");
        }

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(DbHelper.SQL_SELECT_SINGLE_AGENT)) {

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

        } catch (SQLException sqle) {
            logger.log(Level.SEVERE, null, sqle);
            throw new ServiceFailureException("Error when looking up an agent with id" + id, sqle);
        }

        return retVal;
    }

    /**
     * Updates an existing Agent in the database. Provided Agent must have valid values associated.
     * If not, the method throws an IllegalArgumentException. Possible causes are, that an agent's
     * name contains illegal characters or agent is too old or not born yet. Complete list would be
     * too long - see validateAgent() method for all of them.
     *
     * @param agent  a well formed instance of an Agent
     * @throws ServiceFailureException   if an error with the database occurred
     * @throws IllegalArgumentException  if the agent could not be updated e.g. is not present
     */
    @Override
    public void updateAgent(Agent agent) throws ServiceFailureException, IllegalArgumentException {

        validateAgent(agent);

        if (agent.getId() == null) {
            throw new IllegalArgumentException("Agent with null id cannot be updated");
        }
        if (agent.getId() <= 0) {
            throw new IllegalArgumentException("Agent's id is less than zero");
        }

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(DbHelper.SQL_UPDATE_SINGLE_AGENT)) {

            ps.setString(1, agent.getName());
            ps.setTimestamp(2, Timestamp.from(agent.getBorn()));
            ps.setLong(3, agent.getId());

            int addedRows = ps.executeUpdate();
            if(addedRows != 1) {
                throw new IllegalArgumentException("Unable to update agent " + agent);
            }

        } catch (SQLException sqle) {
            logger.log(Level.SEVERE, null, sqle);
            throw new ServiceFailureException("Error when updating an agent" + agent, sqle);
        }
    }

    /**
     * Deletes Agent from the database. Provided instance of the Agent does not have to be well formed,
     * but the Agent must have valid id.
     *
     * @param agent  an instance of an Agent with valid id
     * @throws ServiceFailureException   if an error with the database occurred
     * @throws IllegalArgumentException  if no such id was present in the database
     */
    @Override
    public void deleteAgent(Agent agent) throws ServiceFailureException, IllegalArgumentException {
        if (agent == null) {
            throw new IllegalArgumentException("Agent pointer is null");
        }
        if (agent.getId() == null) {
            throw new IllegalArgumentException("Agent with null id cannot be deleted");
        }
        if (agent.getId() <= 0) {
            throw new IllegalArgumentException("Agent's id is less than zero");
        }

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(DbHelper.SQL_DELETE_SINGLE_AGENT)) {

            Long agentId = agent.getId();
            ps.setLong(1, agentId);

            int addedRows = ps.executeUpdate();
            if(addedRows != 1) {
                throw new IllegalArgumentException("Did not delete agent with id =" + agentId);
            }

        } catch (SQLException sqle) {
            logger.log(Level.SEVERE, null, sqle);
            throw new ServiceFailureException("Error when deleting an agent" + agent, sqle);
        }
    }

    /**
     * Returns Set of all Agents in the database. If there are none, than an empty
     * collection is returned
     *
     * @return Set of Agents
     * @throws ServiceFailureException  if an error with database occurred
     */
    @Override
    public Set<Agent> findAllAgents() throws ServiceFailureException {

        Set<Agent> retSet = new HashSet<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(
                    DbHelper.SQL_SELECT_ALL_AGENTS)) {

            ResultSet rs = ps.executeQuery();
            rs.beforeFirst();

            while (rs.next()) {
                Agent agent = resultSetToAgent(rs);
                retSet.add(agent);
            }

        } catch (SQLException sqle) {
            logger.log(Level.SEVERE, null, sqle);
            throw new ServiceFailureException("Error when looking up all agents", sqle);
        }

        return retSet;

    }

    /**
     * Group of constraints to check if Agent has correct values set. Each of the
     * constraints can throw IllegalArgumentException
     */
    private void validateAgent(Agent agent) throws IllegalArgumentException {
        if (agent == null) {
            throw new IllegalArgumentException("agent is null");
        }
        if (agent.getName() == null) {
            throw new IllegalArgumentException("agent name is null");
        }
        if (agent.getName().equals("")) {
            throw new IllegalArgumentException("agent name is empty");
        }
        if (agent.getName().length() > Constants.AGENT_NAME_MAX_LENGTH) {
            throw new IllegalArgumentException("agent name is too long");
        }
        if (! Pattern.matches(Constants.AGENT_NAME_REGEX, agent.getName())) {
            throw new IllegalArgumentException("agent name contains illegal characters");
        }
        ZonedDateTime agentBorn = ZonedDateTime.ofInstant(agent.getBorn(), ZoneId.systemDefault());
        if (agentBorn.compareTo(ZonedDateTime.now()) > 0) {
            throw new IllegalArgumentException("Agent not born yet");
        }
        if (agentBorn.plusYears(100).compareTo(ZonedDateTime.now()) < 0) {
            throw new IllegalArgumentException("Agent is too old");
        }
    }

}

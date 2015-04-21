package cz.muni.fi.pv168.agentproject.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.ZoneId;

/**
 * This implements some methods used in all managers or methods that are dependent
 * on each other.
 *
 * @author Wallecnik
 * @version 29.3.2015
 */
public abstract class AbstractManager {

    /**
     * Retrieves a newly generated key from given ResultSet or throws an exception
     * if more keys or no key were found
     */
    protected Long getKeyFromRS(ResultSet resultSet, Object causeInstance) throws SQLException {

        Long newId;

        if (resultSet.first()) {
            if (resultSet.getMetaData().getColumnCount() != 1) {
                throw new ServiceFailureException("Internal Error: Generated key"
                        + "retrieving failed when trying to insert agent " + causeInstance
                        + " - wrong key fields count: " + resultSet.getMetaData().getColumnCount());
            }

            newId = resultSet.getLong(1);

            if (! resultSet.isLast()) {
                throw new ServiceFailureException("Internal Error: Generated key"
                        + "retrieving failed when trying to insert agent " + causeInstance
                        + " - more keys found");
            }
        }
        else {
            throw new ServiceFailureException("Internal Error: Generated key"
                    + "retrieving failed when trying to insert agent " + causeInstance
                    + " - no key found");
        }

        return newId;
    }

    /**
     * Transforms given ResultSet row to an instance of Agent. ResultSet must be
     * pointed at the specific row from which this method generates the Agent.
     */
    protected Agent resultSetToAgent(ResultSet resultSet) throws SQLException {

        Long id      = resultSet.getLong(DbContract.COLUMN_AGENT_ID);
        String name  = resultSet.getString(DbContract.COLUMN_AGENT_NAME);
        Instant born = resultSet.getTimestamp(DbContract.COLUMN_AGENT_BORN).toLocalDateTime().atZone(ZoneId.of("UTC")).toInstant();

        return new Agent(id, name, born);

    }

    /**
     * Transforms given ResultSet row to an instance of Mission. ResultSet must be
     * pointed at the specific row from which this method generates the Agent.
     */
    protected Mission resultSetToMission(ResultSet resultSet) throws SQLException {

        Long id             = resultSet.getLong(DbContract.COLUMN_MISSION_ID);
        String goal         = resultSet.getString(DbContract.COLUMN_MISSION_GOAL);
        int requiredAgents  = resultSet.getInt(DbContract.COLUMN_MISSION_REQUIRED_AGENTS);
        boolean completed   = resultSet.getBoolean(DbContract.COLUMN_MISSION_COMPLETED);

        return new Mission(id, goal, requiredAgents, completed);

    }

    /**
     * Transforms given ResultSet row to an instance of Assignment. ResultSet must be
     * pointed at the specific row from which this method generates the Agent.
     */
    protected Assignment resultSetToAssignment(ResultSet resultSet) throws SQLException {

        Long id = resultSet.getLong(DbContract.COLUMN_ASSIGNMENT_ID);
        Agent agent = resultSetToAgent(resultSet);
        Mission mission = resultSetToMission(resultSet);
        Instant startTime = resultSet.getTimestamp(DbContract.COLUMN_ASSIGNMENT_START_TIME).toLocalDateTime().atZone(ZoneId.of("UTC")).toInstant();

        Instant endTime = null;
        if (resultSet.getTimestamp(DbContract.COLUMN_ASSIGNMENT_END_TIME) != null) {
            endTime = resultSet.getTimestamp(DbContract.COLUMN_ASSIGNMENT_END_TIME).toLocalDateTime().atZone(ZoneId.of("UTC")).toInstant();
        }

        return new Assignment(id, agent, mission, startTime, endTime);

    }


}

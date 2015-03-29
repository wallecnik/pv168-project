package cz.muni.fi.agentproject;

import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.Instant;

/**
 * No meaning, just runnable
 */
public class TestingMain {

    public static void main(String[] args) throws SQLException {

        DataSource dataSource;

        BasicDataSource bds = new BasicDataSource();
        bds.setUrl(DbHelper.DB_URL);
        bds.setUsername(DbHelper.USERNAME);
        bds.setPassword(DbHelper.PASSWORD);
        dataSource = bds;
        try (Connection connection = dataSource.getConnection()) {
            connection.prepareStatement(DbHelper.SQL_DROP_TABLE_ASSIGNMENT)
                    .executeUpdate();
            connection.prepareStatement(DbHelper.SQL_DROP_TABLE_AGENT)
                    .executeUpdate();
            connection.prepareStatement(DbHelper.SQL_DROP_TABLE_MISSION)
                    .executeUpdate();
            connection.prepareStatement(DbHelper.SQL_CREATE_TABLE_AGENT)
                    .executeUpdate();
            connection.prepareStatement(DbHelper.SQL_CREATE_TABLE_MISSION)
                    .executeUpdate();
            connection.prepareStatement(DbHelper.SQL_CREATE_TABLE_ASSIGNMENT)
                    .executeUpdate();
        }

        AgentManager agentManager = new AgentManagerImpl(dataSource);
        MissionManager missionManager = new MissionManagerImpl(dataSource);
        AssignmentManager manager = new AssignmentManagerImpl(dataSource, agentManager, missionManager);

        Mission goodMission = new Mission(null, "Make IS rule the world", 2, false);
        Agent goodAgent = new Agent(null, "Michal Brandejs", Instant.ofEpochMilli(10L));

        agentManager.createAgent(goodAgent);
        missionManager.createMission(goodMission);

        Assignment goodAssignment = new Assignment(null, goodAgent, goodMission, Instant.now(), null);
        manager.createAssignment(goodAssignment);

    }

}

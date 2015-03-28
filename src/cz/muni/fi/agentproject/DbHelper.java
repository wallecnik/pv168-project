package cz.muni.fi.agentproject;

/**
 * Holds sql commands
 *
 * @author Wallecnik
 * @version 18.3.2015
 */
public class DbHelper {

    private DbHelper() {
        throw new AssertionError("Do not instantiate");
    }

    public static final String USERNAME = "root";
    public static final String PASSWORD = "";
    public static final String DB_NAME = "AgentProject";
    public static final String DB_URL = "jdbc:mysql://localhost:3306/" + DB_NAME;

    /**
     * SQL commands to manipulate with agents.
     * Supposed usage is with preparedStatement.
     */

    public static final String SQL_CREATE_TABLE_AGENT = "" +
            "CREATE TABLE IF NOT EXISTS agent " +
            "(" +
                "agent_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
                "agent_name     VARCHAR(" + Constants.AGENT_NAME_MAX_LENGTH + ") NOT NULL, " +
                "agent_born     DATETIME(" + Constants.TIMESTAMP_DECIMAL_PRECISION + ") NOT NULL" +
            ") ENGINE = MyIsam";

    public static final String SQL_DROP_TABLE_AGENT = "" +
            "DROP TABLE IF EXISTS agent";

    public static final String SQL_INSERT_INTO_AGENT = "" +
            "INSERT INTO agent " +
            "(agent_name, agent_born) " +
            "VALUES (?, ?)";

    public static final String SQL_SELECT_SINGLE_AGENT = "" +
            "SELECT * " +
            "FROM agent " +
            "WHERE agent_id = ?";

    public static final String SQL_SELECT_ALL_AGENTS = "" +
            "SELECT * " +
            "FROM agent";

    public static final String SQL_UPDATE_SINGLE_AGENT = "" +
            "UPDATE agent " +
            "SET agent_name = ?, agent_born = ? " +
            "WHERE agent_id = ?";

    public static final String SQL_DELETE_SINGLE_AGENT = "" +
            "DELETE FROM agent " +
            "WHERE agent_id = ?";

    /**
     * SQL commands to manipulate with missions.
     * Supposed usage is with preparedStatement.
     */

    public static final String SQL_CREATE_TABLE_MISSION = "" +
            "CREATE TABLE IF NOT EXISTS mission " +
            "(" +
                "mission_id      BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
                "mission_goal            TEXT(" + Constants.MISSION_GOAL_MAX_LENGTH + ") NOT NULL, " +
                "mission_required_agents INT UNSIGNED NOT NULL DEFAULT 1, " +
                "mission_completed       BOOL NOT NULL DEFAULT 0" +
            ") ENGINE = MyIsam";

    public static final String SQL_DROP_TABLE_MISSION = "" +
            "DROP TABLE IF EXISTS mission";

    public static final String SQL_INSERT_INTO_MISSION = "" +
            "INSERT INTO mission " +
            "(mission_goal, mission_required_agents, mission_completed) " +
            "VALUES (?, ?, ?)";

    public static final String SQL_SELECT_SINGLE_MISSION = "" +
            "SELECT * " +
            "FROM mission " +
            "WHERE mission_id = ?";

    public static final String SQL_SELECT_ALL_MISSIONS = "" +
            "SELECT * " +
            "FROM mission";

    public static final String SQL_UPDATE_SINGLE_MISSION = "" +
            "UPDATE mission " +
            "SET mission_goal = ?, mission_required_agents = ?, mission_completed = ? " +
            "WHERE mission_id = ?";

    public static final String SQL_DELETE_SINGLE_MISSION = "" +
            "DELETE FROM mission " +
            "WHERE mission_id = ?";

    /**
     * SQL commands to manipulate with assignments.
     * Supposed usage is with preparedStatement.
     */

    public static final String SQL_CREATE_TABLE_ASSIGNMENT = "" +
            "CREATE TABLE IF NOT EXISTS assignment " +
            "(" +
                "assignment_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
                "agent_id      BIGINT UNSIGNED NOT NULL, " +
                "mission_id    BIGINT UNSIGNED NOT NULL, " +
                "assignment_start_time    DATETIME(" + Constants.TIMESTAMP_DECIMAL_PRECISION + ") NOT NULL, " +
                "assignment_end_time      DATETIME(" + Constants.TIMESTAMP_DECIMAL_PRECISION + ") NULL DEFAULT NULL, " +

                "FOREIGN KEY (agent_id) " +
                "REFERENCES agent(agent_id) " +
                "ON UPDATE CASCADE ON DELETE CASCADE, " +

                "FOREIGN KEY (mission_id) " +
                "REFERENCES mission(mission_id) " +
                "ON UPDATE CASCADE ON DELETE CASCADE" +
            ") ENGINE = MyIsam";

    public static final String SQL_DROP_TABLE_ASSIGNMENT = "" +
            "DROP TABLE IF EXISTS assignment";

    public static final String SQL_INSERT_INTO_ASSIGNMENT = "" +
            "INSERT INTO assignment " +
            "SELECT NewRow.* " +
            "FROM (SELECT " +
                "null AS assignment_id, " +
                "? AS agent_id, " +
                "? AS mission_id, " +
                "? AS assignment_start_time, " +
                "? AS assignment_end_time" +
            ") AS NewRow " +
            "WHERE (SELECT mission_required_agents FROM mission WHERE mission_id = ?) " +
                "> (SELECT count(*) FROM assignment WHERE mission_id = ?)" +
                "and (SELECT count(*) FROM assignment WHERE agent_id = ? and mission_id = ?) = 0";

    public static final String SQL_SELECT_SINGLE_ASSIGNMENT = "" +
            "SELECT * " +
            "FROM (assignment NATURAL JOIN agent NATURAL JOIN mission) " +
            "WHERE assignment_id = ?";

    public static final String SQL_SELECT_ALL_ASSIGNMENTS = "" +
            "SELECT * " +
            "FROM (assignment NATURAL JOIN agent NATURAL JOIN mission)";

    public static final String SQL_SELECT_ALL_ASSIGNMENTS_FOR_AGENT = "" +
            "SELECT * " +
            "FROM (assignment NATURAL JOIN agent NATURAL JOIN mission) " +
            "WHERE agent_id = ?";

    public static final String SQL_SELECT_ALL_ASSIGNMENTS_FOR_MISSION = "" +
            "SELECT * " +
            "FROM (assignment NATURAL JOIN agent NATURAL JOIN mission) " +
            "WHERE mission_id = ?";

    public static final String SQL_UPDATE_SINGLE_ASSIGNMENT = "" +
            "UPDATE assignment " +
            "SET agent_id = ?, mission_id = ?, assignment_start_time = ?, assignment_end_time = ?" +
            "WHERE assignment_id = ?";

    public static final String SQL_DELETE_SINGLE_ASSIGNMENT = "" +
            "DELETE FROM assignment " +
            "WHERE assignment_id = ?";


}



















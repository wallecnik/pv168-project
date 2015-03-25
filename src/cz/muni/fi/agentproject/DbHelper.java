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
            "CREATE TABLE IF NOT EXISTS " + DbContract.TABLE_NAME_AGENT + " " +
            "(" +
                DbContract.COLUMN_AGENT_ID   + " BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
                DbContract.COLUMN_AGENT_NAME + " VARCHAR(" + Constants.AGENT_NAME_MAX_LENGTH + ") NOT NULL, " +
                DbContract.COLUMN_AGENT_BORN + " DATETIME(" + Constants.TIMESTAMP_DECIMAL_PRECISION + ") NOT NULL" +
            ") ENGINE InnoDB";

    public static final String SQL_DROP_TABLE_AGENT = "" +
            "DROP TABLE IF EXISTS " + DbContract.TABLE_NAME_AGENT;

    public static final String SQL_INSERT_INTO_AGENT = "" +
            "INSERT INTO " + DbContract.TABLE_NAME_AGENT + " " +
            "(" +
                DbContract.COLUMN_AGENT_NAME + ", " +
                DbContract.COLUMN_AGENT_BORN +
            ") VALUES (?, ?)";

    public static final String SQL_SELECT_SINGLE_AGENT = "" +
            "SELECT " +
                DbContract.COLUMN_AGENT_ID   + ", " +
                DbContract.COLUMN_AGENT_NAME + ", " +
                DbContract.COLUMN_AGENT_BORN + " " +
            "FROM " + DbContract.TABLE_NAME_AGENT + " " +
            "WHERE " + DbContract.COLUMN_AGENT_ID + " = ?";

    public static final String SQL_SELECT_ALL_AGENTS = "" +
            "SELECT " +
                DbContract.COLUMN_AGENT_ID   + ", " +
                DbContract.COLUMN_AGENT_NAME + ", " +
                DbContract.COLUMN_AGENT_BORN + " " +
            "FROM " + DbContract.TABLE_NAME_AGENT;

    public static final String SQL_UPDATE_SINGLE_AGENT = "" +
            "UPDATE " + DbContract.TABLE_NAME_AGENT + " " +
            "SET " +
                DbContract.COLUMN_AGENT_NAME + " = ?, " +
                DbContract.COLUMN_AGENT_BORN + " = ? " +
            "WHERE " + DbContract.COLUMN_AGENT_ID + " = ?";

    public static final String SQL_DELETE_SINGLE_AGENT = "" +
            "DELETE FROM " + DbContract.TABLE_NAME_AGENT + " " +
            "WHERE " + DbContract.COLUMN_AGENT_ID + " = ?";

    //TODO: ASSIGNMENT and MISSION SQL commands are NOT TESTED!!!

    /**
     * SQL commands to manipulate with missions.
     * Supposed usage is with preparedStatement.
     */

    public static final String SQL_CREATE_TABLE_MISSION = "" +
            "CREATE TABLE IF NOT EXISTS " + DbContract.TABLE_NAME_MISSION + " " +
            "(" +
                DbContract.COLUMN_MISSION_ID              + " BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
                DbContract.COLUMN_MISSION_GOAL            + " TEXT(" + Constants.MISSION_GOAL_MAX_LENGTH + ") NOT NULL, " +
                DbContract.COLUMN_MISSION_REQUIRED_AGENTS + " INT UNSIGNED NOT NULL DEFAULT 1, " +
                DbContract.COLUMN_MISSION_COMPLETED       + " BOOL NOT NULL DEFAULT 0" +
            ") ENGINE InnoDB";

    public static final String SQL_DROP_TABLE_MISSION = "" +
            "DROP TABLE IF EXISTS " + DbContract.TABLE_NAME_MISSION;

    public static final String SQL_INSERT_INTO_MISSION = "" +
            "INSERT INTO " + DbContract.TABLE_NAME_MISSION + " " +
            "(" +
                DbContract.COLUMN_MISSION_GOAL + ", " +
                DbContract.COLUMN_MISSION_REQUIRED_AGENTS + ", " +
                DbContract.COLUMN_MISSION_COMPLETED +
            ") VALUES (?, ?, ?)";

    public static final String SQL_SELECT_SINGLE_MISSION = "" +
            "SELECT " +
                DbContract.COLUMN_MISSION_ID   + ", " +
                DbContract.COLUMN_MISSION_GOAL + ", " +
                DbContract.COLUMN_MISSION_REQUIRED_AGENTS + ", " +
                DbContract.COLUMN_MISSION_COMPLETED + " " +
            "FROM " + DbContract.TABLE_NAME_MISSION + " " +
            "WHERE " + DbContract.COLUMN_MISSION_ID + " = ?";

    public static final String SQL_SELECT_ALL_MISSIONS = "" +
            "SELECT " +
                DbContract.COLUMN_MISSION_ID   + ", " +
                DbContract.COLUMN_MISSION_GOAL + ", " +
                DbContract.COLUMN_MISSION_REQUIRED_AGENTS + ", " +
                DbContract.COLUMN_MISSION_COMPLETED + " " +
            "FROM " + DbContract.TABLE_NAME_MISSION;

    public static final String SQL_UPDATE_SINGLE_MISSION = "" +
            "UPDATE " + DbContract.TABLE_NAME_MISSION + " " +
            "SET " +
                DbContract.COLUMN_MISSION_GOAL + " = ?, " +
                DbContract.COLUMN_MISSION_REQUIRED_AGENTS + " = ?, " +
                DbContract.COLUMN_MISSION_COMPLETED + " = ? " +
            "WHERE " + DbContract.COLUMN_MISSION_ID + " = ?";

    public static final String SQL_DELETE_SINGLE_MISSION = "" +
            "DELETE FROM " + DbContract.TABLE_NAME_MISSION + " " +
            "WHERE " + DbContract.COLUMN_MISSION_ID + " = ?";

    /**
     * SQL commands to manipulate with assignments.
     * Supposed usage is with preparedStatement.
     */

    public static final String SQL_CREATE_TABLE_ASSIGNMENT = "" +
            "CREATE TABLE IF NOT EXISTS " + DbContract.TABLE_NAME_ASSIGNMENT + " " +
            "(" +
                DbContract.COLUMN_ASSIGNMENT_ID         + " BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
                DbContract.COLUMN_ASSIGNMENT_AGENT_ID   + " BIGINT UNSIGNED NOT NULL, " +
                DbContract.COLUMN_ASSIGNMENT_MISSION_ID + " BIGINT UNSIGNED NOT NULL, " +
                DbContract.COLUMN_ASSIGNMENT_STARTTIME  + " DATETIME(\" + Constants.TIMESTAMP_DECIMAL_PRECISION + \") NOT NULL, " +
                DbContract.COLUMN_ASSIGNMENT_ENDTIME    + " DATETIME(\" + Constants.TIMESTAMP_DECIMAL_PRECISION + \") NULL DEFAULT NULL, " +
                "FOREIGN KEY (" + DbContract.COLUMN_ASSIGNMENT_AGENT_ID + ") " +
                "REFERENCES " + DbContract.TABLE_NAME_AGENT + "(" + DbContract.COLUMN_AGENT_ID + ") " +
                "ON UPDATE CASCADE ON DELETE CASCADE, " +
                "FOREIGN KEY (" + DbContract.COLUMN_ASSIGNMENT_MISSION_ID + ") " +
                "REFERENCES " + DbContract.TABLE_NAME_MISSION + "(" + DbContract.COLUMN_MISSION_ID + ") " +
                "ON UPDATE CASCADE ON DELETE CASCADE" +
            ") ENGINE InnoDB";

    public static final String SQL_DROP_TABLE_ASSIGNMENT = "" +
            "DROP TABLE IF EXISTS " + DbContract.TABLE_NAME_ASSIGNMENT;

    public static final String SQL_INSERT_INTO_ASSIGNMENT = "" +
            "INSERT INTO " + DbContract.TABLE_NAME_ASSIGNMENT + " " +
            "(" +
                DbContract.COLUMN_ASSIGNMENT_AGENT_ID + ", " +
                DbContract.COLUMN_ASSIGNMENT_MISSION_ID + ", " +
                DbContract.COLUMN_ASSIGNMENT_STARTTIME + ", " +
                DbContract.COLUMN_ASSIGNMENT_ENDTIME +
            ") VALUES (?, ?, ?, ?)";

    public static final String SQL_SELECT_SINGLE_ASSIGNMENT = "" +
            "SELECT " +
                DbContract.COLUMN_ASSIGNMENT_ID   + ", " +
                DbContract.COLUMN_ASSIGNMENT_AGENT_ID + ", " +
                DbContract.COLUMN_ASSIGNMENT_MISSION_ID + ", " +
                DbContract.COLUMN_ASSIGNMENT_STARTTIME + ", " +
                DbContract.COLUMN_ASSIGNMENT_ENDTIME + " " +
            "FROM " + DbContract.TABLE_NAME_ASSIGNMENT + " " +
            "WHERE " + DbContract.COLUMN_ASSIGNMENT_ID + " = ?";

    public static final String SQL_SELECT_ALL_ASSIGNMENTS = "" +
            "SELECT " +
                DbContract.COLUMN_ASSIGNMENT_ID   + ", " +
                DbContract.COLUMN_ASSIGNMENT_AGENT_ID + ", " +
                DbContract.COLUMN_ASSIGNMENT_MISSION_ID + ", " +
                DbContract.COLUMN_ASSIGNMENT_STARTTIME + ", " +
                DbContract.COLUMN_ASSIGNMENT_ENDTIME + " " +
            "FROM " + DbContract.TABLE_NAME_ASSIGNMENT;

    public static final String SQL_SELECT_ALL_ASSIGNMENTS_FOR_AGENT = "" +
            "SELECT " +
            DbContract.COLUMN_ASSIGNMENT_ID   + ", " +
            DbContract.COLUMN_ASSIGNMENT_AGENT_ID + ", " +
            DbContract.COLUMN_ASSIGNMENT_MISSION_ID + ", " +
            DbContract.COLUMN_ASSIGNMENT_STARTTIME + ", " +
            DbContract.COLUMN_ASSIGNMENT_ENDTIME + " " +
            "FROM " + DbContract.TABLE_NAME_ASSIGNMENT + " " +
            "WHERE " + DbContract.COLUMN_ASSIGNMENT_AGENT_ID + " = ?";

    public static final String SQL_SELECT_ALL_ASSIGNMENTS_FOR_MISSION = "" +
            "SELECT " +
            DbContract.COLUMN_ASSIGNMENT_ID   + ", " +
            DbContract.COLUMN_ASSIGNMENT_AGENT_ID + ", " +
            DbContract.COLUMN_ASSIGNMENT_MISSION_ID + ", " +
            DbContract.COLUMN_ASSIGNMENT_STARTTIME + ", " +
            DbContract.COLUMN_ASSIGNMENT_ENDTIME + " " +
            "FROM " + DbContract.TABLE_NAME_ASSIGNMENT + " " +
            "WHERE " + DbContract.COLUMN_ASSIGNMENT_MISSION_ID + " = ?";

    public static final String SQL_UPDATE_SINGLE_ASSIGNMENT = "" +
            "UPDATE " + DbContract.TABLE_NAME_MISSION + " " +
            "SET " +
                DbContract.COLUMN_ASSIGNMENT_AGENT_ID + " = ?, " +
                DbContract.COLUMN_ASSIGNMENT_MISSION_ID + " = ?, " +
                DbContract.COLUMN_ASSIGNMENT_STARTTIME + " = ?, " +
                DbContract.COLUMN_ASSIGNMENT_ENDTIME + " = ? " +
            "WHERE " + DbContract.COLUMN_ASSIGNMENT_ID + " = ?";

    public static final String SQL_DELETE_SINGLE_ASSIGNMENT = "" +
            "DELETE FROM " + DbContract.TABLE_NAME_ASSIGNMENT + " " +
            "WHERE " + DbContract.COLUMN_ASSIGNMENT_ID + " = ?";


}



















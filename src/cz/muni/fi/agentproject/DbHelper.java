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

    public static final String SQL_CREATE_TABLE_AGENT = "" +
            "CREATE TABLE " + DbContract.TABLE_NAME_AGENT +
            " (" +
            DbContract.COLUMN_AGENT_ID   + " BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
            DbContract.COLUMN_AGENT_NAME + " VARCHAR(255) NOT NULL, " +
            DbContract.COLUMN_AGENT_BORN + " BIGINT NOT NULL" +
            ")";

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

    public static final String SQL_UPDATE_SINGLE_AGENT = "" +
            "UPDATE " + DbContract.TABLE_NAME_AGENT +
            "SET " +
            DbContract.COLUMN_AGENT_NAME + " = ?, " +
            DbContract.COLUMN_AGENT_BORN + " = ? " +
            "WHERE " + DbContract.COLUMN_AGENT_ID + " = ?";

    public static final String SQL_DELETE_SINGLE_AGENT = "" +
            "DELETE FROM " + DbContract.TABLE_NAME_AGENT + " " +
            "WHERE " + DbContract.COLUMN_AGENT_ID + " = ?";

}

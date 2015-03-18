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

    public static final String DB_URL = "";

    public static final String SQL_CREATE_TEMPORARY_TABLE_AGENT = "" +
            "";

    public static final String SQL_DROP_TABLE_AGENT = "DROP TABLE " + DbContract.TABLE_NAME_AGENT;

    public static String SQL_INSERT_INTO_AGENT = "" +
            "INSERT INTO " + DbContract.TABLE_NAME_AGENT +
            " (" +
            DbContract.COLUMN_AGENT_NAME + ", " +
            DbContract.COLUMN_AGENT_BORN + ", " +
            ") VALUES (?, ?)";

    public static String SQL_SELECT_SINGLE_AGENT = "" +
            "SELECT " +
            DbContract.COLUMN_AGENT_ID   + ", " +
            DbContract.COLUMN_AGENT_NAME + ", " +
            DbContract.COLUMN_AGENT_BORN + ", " +
            " FROM " + DbContract.TABLE_NAME_AGENT +
            " WHERE " + DbContract.COLUMN_AGENT_ID + " = ?";


}

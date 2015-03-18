package cz.muni.fi.agentproject;

/**
 * Holds appearance of the database
 *
 * @author Wallecnik
 * @version 18.3.2015
 */
public class DbContract {

    private DbContract() {
        throw new AssertionError("Do not instantiate");
    }

    public static String TABLE_NAME_AGENT = "agent";
    public static String COLUMN_AGENT_ID = "id";
    public static String COLUMN_AGENT_NAME = "name";
    public static String COLUMN_AGENT_BORN = "born";

}

package cz.muni.fi.pv168.agentproject.db;

/**
 * Some constants for usage in the whole application
 *
 * @author Wallecnik
 * @version 24.3.2015
 */
public class Constants {

    private Constants() {
        throw new AssertionError("Do not instantiate");
    }

    public static final String AGENT_NAME_REGEX = "[\\p{L}\\s\']*\\p{L}[\\p{L}\\s\']*";

    public static final short AGENT_NAME_MAX_LENGTH = 255;
    public static final int MISSION_GOAL_MAX_LENGTH = 256*256-1;
    public static final short TIMESTAMP_DECIMAL_PRECISION = 6;


}

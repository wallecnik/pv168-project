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

    /**
     * Table Agent
     */
    public static final String TABLE_NAME_AGENT = "agent";
    public static final String COLUMN_AGENT_ID = "id";
    public static final String COLUMN_AGENT_NAME = "name";
    public static final String COLUMN_AGENT_BORN = "born";

    /**
     * Table Mission
     */
    public static final String TABLE_NAME_MISSION = "mission";
    public static final String COLUMN_MISSION_ID = "id";
    public static final String COLUMN_MISSION_REQUIRED_AGENTS = "required_agents";
    public static final String COLUMN_MISSION_GOAL = "goal";
    public static final String COLUMN_MISSION_COMPLETED = "completed";

    /**
     * Table Assignment
     */
    public static final String TABLE_NAME_ASSIGNMENT = "assignment";
    public static final String COLUMN_ASSIGNMENT_ID = "id";
    public static final String COLUMN_ASSIGNMENT_AGENT_ID = "agent_id";
    public static final String COLUMN_ASSIGNMENT_MISSION_ID = "mission_id";
    public static final String COLUMN_ASSIGNMENT_STARTTIME = "startTime";
    public static final String COLUMN_ASSIGNMENT_ENDTIME = "endTime";

}

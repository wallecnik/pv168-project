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
    public static final String COLUMN_AGENT_ID = "agent_id";
    public static final String COLUMN_AGENT_NAME = "agent_name";
    public static final String COLUMN_AGENT_BORN = "agent_born";

    /**
     * Table Mission
     */
    public static final String TABLE_NAME_MISSION = "mission";
    public static final String COLUMN_MISSION_ID = "mission_id";
    public static final String COLUMN_MISSION_REQUIRED_AGENTS = "mission_required_agents";
    public static final String COLUMN_MISSION_GOAL = "mission_goal";
    public static final String COLUMN_MISSION_COMPLETED = "mission_completed";

    /**
     * Table Assignment
     */
    public static final String TABLE_NAME_ASSIGNMENT = "assignment";
    public static final String COLUMN_ASSIGNMENT_ID = "assignment_id";
    public static final String COLUMN_ASSIGNMENT_AGENT_ID = COLUMN_AGENT_ID;
    public static final String COLUMN_ASSIGNMENT_MISSION_ID = COLUMN_MISSION_ID;
    public static final String COLUMN_ASSIGNMENT_START_TIME = "assignment_start_time";
    public static final String COLUMN_ASSIGNMENT_END_TIME = "assignment_end_time";

}

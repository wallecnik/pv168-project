package cz.muni.fi.pv168.agentproject.db;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implemented MissionManager interface
 *
 * @author Dužinka
 * @version 31.03.2015
 */
public class MissionManagerImpl extends AbstractManager implements MissionManager {

    public static final Logger logger = Logger.getLogger(MissionManagerImpl.class.getName());
    private DataSource dataSource;

    public MissionManagerImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Creates mission in the database.
     * Mission constraints solved at private validateMission().
     *
     * @param mission                       mission to be pushed into database
     * @throws ServiceFailureException      when SQL Exception occurs or wrong number
     *                                          of rows added to the dabatase
     * @throws IllegalArgumentException     when mission has false parameters
     *                                          (inherited from validateMission())
     */
    @Override
    public void createMission(Mission mission) throws ServiceFailureException, IllegalArgumentException {

        validateMission(mission);

        if (mission.getId() != null) {
            throw new IllegalArgumentException("Mission id is not null.");
        }

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     DbHelper.SQL_INSERT_INTO_MISSION, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, mission.getGoal());
            ps.setInt(2, mission.getRequiredAgents());
            ps.setBoolean(3, mission.isCompleted());

            int addedRows = ps.executeUpdate();
            if (addedRows != 1) {
                throw new ServiceFailureException("Internal Error: More rows "
                        + "inserted when trying to insert mission " + mission);
            }

            ResultSet keyRS = ps.getGeneratedKeys();
            Long newId = this.getKeyFromRS(keyRS, mission);
            mission.setId(newId);

        }
        catch (SQLException sqle) {
            logger.log(Level.SEVERE, null, sqle);
            throw new ServiceFailureException("Error when inserting mission " + mission, sqle);
        }
    }

    /**
     * Updates a mission in a database with the provided one.
     * Mission constraints solved at private validateMission().
     *
     * @param mission                       mission with new parameters
     * @throws ServiceFailureException      when SQL Exception occurs or wrong number
     *                                          of rows added to the dabatase
     * @throws IllegalArgumentException     when mission has false parameters
     *                                          (inherited from validateMission())
     */
    @Override
        public void updateMission(Mission mission) throws ServiceFailureException, IllegalArgumentException {

        validateMission(mission);

        if (mission.getId() == null) {
            throw new IllegalArgumentException("Mission with null id cannot be updated");
        }
        if (mission.getId() <= 0) {
            throw new IllegalArgumentException("Mission id is less than zero");
        }

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(DbHelper.SQL_UPDATE_SINGLE_MISSION)) {

            ps.setString(1, mission.getGoal());
            ps.setInt(2, mission.getRequiredAgents());
            ps.setBoolean(3, mission.isCompleted());
            ps.setLong(4, mission.getId());
            ps.setLong(5, mission.getId());
            ps.setInt(6, mission.getRequiredAgents());

            int addedRows = ps.executeUpdate();
            if(addedRows != 1) {
                throw new IllegalArgumentException("Unable to update mission " + mission);
            }

        } catch (SQLException sqle) {
            logger.log(Level.SEVERE, null, sqle);
            throw new ServiceFailureException("Error when updating mission" + mission, sqle);
        }
    }

    /**
     * Deletes given mission from database.
     *
     * @param mission                       mission to be deleted from database
     * @throws ServiceFailureException      when SQL Exception occurs
     * @throws IllegalArgumentException     when provided mission wasn't deleted
     *                                          or has bad parameters
     */
    @Override
    public void deleteMission(Mission mission) throws ServiceFailureException, IllegalArgumentException {
        if (mission == null) {
            throw new IllegalArgumentException("Mission pointer is null");
        }
        if (mission.getId() == null) {
            throw new IllegalArgumentException("Mission with null id cannot be deleted");
        }
        if (mission.getId() <= 0) {
            throw new IllegalArgumentException("Mission id is less than zero");
        }

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(DbHelper.SQL_DELETE_SINGLE_MISSION)) {

            Long missionId = mission.getId();
            ps.setLong(1, missionId);

            int addedRows = ps.executeUpdate();
            if(addedRows != 1) {
                throw new IllegalArgumentException("Did not delete mission with id =" + missionId);
            }

        } catch (SQLException sqle) {
            logger.log(Level.SEVERE, null, sqle);
            throw new ServiceFailureException("Error when deleting mission" + mission, sqle);
        }
    }

    /**
     * Find a specific mission by its ID.
     *
     * @param id
     * @return                              Mission instance
     * @throws ServiceFailureException      when SQL Exception occurs
     *                                          or more missions with the same ID are found
     * @throws IllegalArgumentException     when ID isn't valid
     */
    @Override
    public Mission findMissionById(Long id) throws ServiceFailureException, IllegalArgumentException{

        Mission returnValue;

        if (id == null) {
            throw new IllegalArgumentException("ID is null");
        }
        if (id <= 0) {
            throw new IllegalArgumentException("ID is less than zero");
        }

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(DbHelper.SQL_SELECT_SINGLE_MISSION)) {

            ps.setLong(1, id);
            ResultSet resultSet = ps.executeQuery();

            if (resultSet.first()) {
                Mission mission = resultSetToMission(resultSet);

                if (resultSet.next()) {
                    throw new ServiceFailureException( "Internal error: More entities with the same id found " +
                            "(source id: " + id + ", found " + mission + " and " + resultSetToMission(resultSet));
                }

                returnValue = mission;
            }
            else {
                returnValue = null;
            }

        } catch (SQLException sqle) {
            logger.log(Level.SEVERE, null, sqle);
            throw new ServiceFailureException("Error when searching for mission with id " + id, sqle);
        }

        return returnValue;
    }

    /**
     * Finds all missions.
     *
     * @return                          Set of Mission instances
     * @throws ServiceFailureException  when SQL Exception occurs
     */
    @Override
    public Set<Mission> findAllMissions() throws ServiceFailureException {

        Set<Mission> retSet = new HashSet<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     DbHelper.SQL_SELECT_ALL_MISSIONS)) {

            ResultSet resultSet = ps.executeQuery();
            resultSet.beforeFirst();

            while (resultSet.next()) {
                Mission mission = resultSetToMission(resultSet);
                retSet.add(mission);
            }

        } catch (SQLException sqle) {
            logger.log(Level.SEVERE, null, sqle);
            throw new ServiceFailureException("Error when searching for all missions", sqle);
        }

        return retSet;
    }

    /**
     * Checks if the provided mission has valid parameters.
     *
     * @param mission                       Mission instance
     * @throws IllegalArgumentException     when given parameter isn't valid
     */
    private void validateMission(Mission mission) throws IllegalArgumentException {
        if (mission == null) {
            throw new IllegalArgumentException("Mission is null");
        }

        if (mission.getGoal() == null) {
            throw new IllegalArgumentException("Mission goal is null");
        }

        if (mission.getGoal().equals("")) {
            throw new IllegalArgumentException("Mission goal is empty");
        }

        if (mission.getGoal().length() > Constants.MISSION_GOAL_MAX_LENGTH) {
            throw new IllegalArgumentException("Mission goal name is too long");
        }

        if (mission.getRequiredAgents() <= 0) {
            throw new IllegalArgumentException("Mission requires positive number of agents");
        }
    }

    public List<Mission> sortById() {
        List<Mission> sortedList = new ArrayList<>(findAllMissions());
        Collections.sort(sortedList, new IdComparator());
        return sortedList;
    }

    public List<Mission> sortByGoal() {
        List<Mission> sortedList = new ArrayList<>(findAllMissions());
        Collections.sort(sortedList, new GoalComparator());
        return sortedList;
    }

    public List<Mission> sortByRequiredAgents() {
        List<Mission> sortedList = new ArrayList<>(findAllMissions());
        Collections.sort(sortedList, new RequiredAgentComparator());
        return sortedList;
    }

    public List<Mission> sortByCompleted() {
        List<Mission> sortedList = new ArrayList<>(findAllMissions());
        Collections.sort(sortedList, new CompletedComparator());
        return sortedList;
    }
}

class IdComparator implements Comparator<Mission> {
    @Override
    public int compare(Mission a, Mission b) {
        return a.getId() < b.getId() ? -1 : a.getId() == b.getId() ? 0 : 1;
    }
}

class GoalComparator implements Comparator<Mission> {
    @Override
    public int compare(Mission a, Mission b) {
        return a.getGoal().compareToIgnoreCase(b.getGoal());
    }
}

class RequiredAgentComparator implements Comparator<Mission> {
    @Override
    public int compare(Mission a, Mission b) {
        return a.getRequiredAgents() < b.getRequiredAgents() ?
                -1 : a.getRequiredAgents() == b.getRequiredAgents() ? 0 : 1;
    }
}

class CompletedComparator implements Comparator<Mission> {
    @Override
    public int compare(Mission a, Mission b) {
        return (a.isCompleted() == b.isCompleted() ? 0 : (b.isCompleted() ? 1 : -1));
    }
}
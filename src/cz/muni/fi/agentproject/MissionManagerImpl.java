package cz.muni.fi.agentproject;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implemented MissionManager interface
 * TODO: Javadocs, settle the zero requiredAgents updateMission issue
 *
 * @author Dužinka
 * @version 25.03.2015
 */
public class MissionManagerImpl extends AbstractManager implements MissionManager {

    public static final Logger logger = Logger.getLogger(MissionManagerImpl.class.getName());
    private DataSource dataSource;

    public MissionManagerImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

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

            int addedRows = ps.executeUpdate();
            if(addedRows != 1) {
                throw new IllegalArgumentException("Unable to update mission " + mission);
            }

        } catch (SQLException sqle) {
            logger.log(Level.SEVERE, null, sqle);
            throw new ServiceFailureException("Error when updating mission" + mission, sqle);
        }
    }

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

        // zero should be fine when updating and reaching mission's agent capacity
        if (mission.getRequiredAgents() < 0) {
            throw new IllegalArgumentException("Mission requires negative number of agents");
        }
    }
}

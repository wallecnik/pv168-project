package cz.muni.fi.agentproject.tests;

import cz.muni.fi.agentproject.DbHelper;
import cz.muni.fi.agentproject.Mission;
import cz.muni.fi.agentproject.MissionManager;
import cz.muni.fi.agentproject.MissionManagerImpl;
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * @author  Wallecnik
 * @version 31.22.2015
 */
public class MissionManagerImplTest {

    private MissionManager manager;
    private DataSource dataSource;

    @Rule
    public ExpectedException expected = ExpectedException.none();

    @Before
    public void setUp() throws SQLException {
        BasicDataSource bds = new BasicDataSource();
        bds.setUrl(DbHelper.DB_URL);
        bds.setUsername(DbHelper.USERNAME);
        bds.setPassword(DbHelper.PASSWORD);
        this.dataSource = bds;
        try (Connection connection = dataSource.getConnection()) {
            connection.prepareStatement(DbHelper.SQL_DROP_TABLE_MISSION)
                    .executeUpdate();
            connection.prepareStatement(DbHelper.SQL_CREATE_TABLE_MISSION)
                    .executeUpdate();
        }
        this.manager = new MissionManagerImpl(dataSource);
    }

    @After
    public void tearDown() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            connection.prepareStatement(DbHelper.SQL_DROP_TABLE_MISSION)
                    .executeUpdate();
        }
    }

    @Test
    public void createMission() {
        Mission mission = new Mission(null, "Kill Usama", 100, false);
        manager.createMission(mission);

        Long id = mission.getId();
        assertNotNull(id);

        Mission storedMission = manager.findMissionById(id);
        assertEquals(mission, storedMission);
        assertNotSame(mission, storedMission);
    }

    @Test
    public void createNullMission() {
        expected.expect(IllegalArgumentException.class);
        manager.createMission(null);
    }

    @Test
    public void createMissionWithAssignedId() {
        Mission mission = new Mission(0L, "", 1, false);
        expected.expect(IllegalArgumentException.class);
        manager.createMission(mission);
    }

    @Test
    public void createMissionWithNullGoal() {
        Mission mission = new Mission(null, null, 1, false);
        expected.expect(IllegalArgumentException.class);
        manager.createMission(mission);
    }

    @Test
    public void createMissionWithZeroRequiredAgents() {
        Mission mission = new Mission(null, "", 0, false);
        expected.expect(IllegalArgumentException.class);
        manager.createMission(mission);
    }

    @Test
    public void createMissionWithNegativeRequiredAgents() {
        Mission mission = new Mission(null, "", -1, false);
        expected.expect(IllegalArgumentException.class);
        manager.createMission(mission);
    }

    @Test
    public void createMissionMarkedCompleted() {
        Mission mission = new Mission(null, "", 1, true);
        expected.expect(IllegalArgumentException.class);
        manager.createMission(mission);
    }

    @Test
    public void updateMission() {

        Mission mission = new Mission(null, "Kill Usama", 100, false);
        manager.createMission(mission);

        Long id = mission.getId();
        Mission storedMission = manager.findMissionById(id);
        storedMission.setGoal("Kill Obama");
        storedMission.setRequiredAgents(1);
        storedMission.setCompleted(true);

        manager.updateMission(storedMission);
        assertNotEquals(mission, storedMission);
        assertEquals(mission.getId(), storedMission.getId());
        assertNotEquals(mission.getGoal(), storedMission.getGoal());
        assertNotEquals(mission.getRequiredAgents(), storedMission.getRequiredAgents());
        assertNotEquals(mission.isCompleted(), storedMission.isCompleted());

    }

    @Test
    public void updateMissionWithWrongId() {
        Mission storedMission = createAndRetrieveMission();
        storedMission.setId(Long.MAX_VALUE);
        expected.expect(IllegalArgumentException.class);
        manager.updateMission(storedMission);
    }

    @Test
    public void updateMissionWithNullGoal() {
        Mission storedMission = createAndRetrieveMission();
        storedMission.setGoal(null);
        expected.expect(IllegalArgumentException.class);
        manager.updateMission(storedMission);
    }

    @Test
    public void updateMissionWithZeroRequiredAgents() {
        Mission storedMission = createAndRetrieveMission();
        storedMission.setRequiredAgents(0);
        expected.expect(IllegalArgumentException.class);
        manager.updateMission(storedMission);
    }

    @Test
    public void updateMissionWithNegativeRequiredAgents() {
        Mission storedMission = createAndRetrieveMission();
        storedMission.setRequiredAgents(-1);
        expected.expect(IllegalArgumentException.class);
        manager.updateMission(storedMission);
    }

    @Test
    public void deleteMission() {
        Mission mission = new Mission(null, "Kill Usama", 100, false);
        manager.createMission(mission);

        Long id = mission.getId();
        manager.deleteMission(mission);

        Mission storedMission = manager.findMissionById(id);
        assertNull(storedMission);
    }

    @Test
    public void deleteNullMission() {
        expected.expect(IllegalArgumentException.class);
        manager.deleteMission(null);
    }

    @Test
    public void deleteMissionWithWrongId() {
        Long fakeId = Long.MAX_VALUE / 2;
        if (manager.findMissionById(fakeId) != null) {
            fakeId = Math.round(Math.random() * Long.MAX_VALUE / 2);
            System.err.println("Using random value for id: " + fakeId);
        }

        Mission mission = new Mission(fakeId, null, 0, false);
        expected.expect(IllegalArgumentException.class);
        manager.deleteMission(mission);
    }

    @Test
    public void createMoreMissions() {
        Set<Mission> currentMissions = manager.findAllMissions();

        Mission mission1 = new Mission(null, "goal1", 1, false);
        manager.createMission(mission1);
        Mission mission2 = new Mission(null, "goal2", 2, false);
        manager.createMission(mission2);
        Mission mission3 = new Mission(null, "goal3", 2, false);
        manager.createMission(mission3);

        Set<Mission> newMissions = manager.findAllMissions();

        assertNotEquals(currentMissions, newMissions);
        assertEquals(currentMissions.size() + 3, newMissions.size());

        newMissions.remove(mission1);
        newMissions.remove(mission2);
        newMissions.remove(mission3);

        assertEquals(currentMissions, newMissions);
    }

    private Mission createAndRetrieveMission() {
        Mission mission = new Mission(null, "Kill Usama", 100, false);
        manager.createMission(mission);

        Long id = mission.getId();
        return manager.findMissionById(id);
    }

}

























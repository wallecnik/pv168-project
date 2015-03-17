package cz.muni.fi.agentproject.tests;

import cz.muni.fi.agentproject.Mission;
import cz.muni.fi.agentproject.MissionManager;
import cz.muni.fi.agentproject.MissionManagerImpl;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

/**
 * @author  Wallecnik
 * @version 31.22.2015
 */
public class MissionManagerImplTest {

    private MissionManager manager;

    @Rule
    private ExpectedException expected = ExpectedException.none();

    @Before
    public void setUp() {
        manager = new MissionManagerImpl();
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
    public void updateMissionWithNullDescription() {
        Mission storedMission = createAndRetrieveMission();
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
    public void updateMissionWithNegatoverequiredAgents() {
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

    }

    private Mission createAndRetrieveMission() {
        Mission mission = new Mission(null, "Kill Usama", 100, false);
        manager.createMission(mission);

        Long id = mission.getId();
        return manager.findMissionById(id);
    }

}

























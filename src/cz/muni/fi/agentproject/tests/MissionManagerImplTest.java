package cz.muni.fi.agentproject.tests;

import cz.muni.fi.agentproject.Mission;
import cz.muni.fi.agentproject.MissionManager;
import cz.muni.fi.agentproject.MissionManagerImpl;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author  Wallecnik
 * @version 31.22.2015
 */
public class MissionManagerImplTest {

    MissionManager manager;

    @Before
    public void setUp() {
        manager = new MissionManagerImpl();
    }

    @Test
    public void createMission() {
        Mission mission = new Mission(null, "Kill Usama", "Quick in and out", 100, false);
        manager.createMission(mission);

        Long id = mission.getId();
        assertNotNull(id);

        Mission storedMission = manager.findMissionById(id);
        assertEquals(mission, storedMission);
        assertNotSame(mission, storedMission);
    }

    @Test
    public void createMissionWithWrongAttributes() {

        //null
        try {
            manager.createMission(null);
            fail();
        }
        catch (IllegalArgumentException iae) {
            //OK
        }

        //setting id
        try {
            Mission mission = new Mission(0L, "", "", 1, false);
            manager.createMission(mission);
            fail();
        }
        catch (IllegalArgumentException iae) {
            //OK
        }

        //null goal
        try {
            Mission mission = new Mission(null, null, "", 1, false);
            manager.createMission(mission);
            fail();
        }
        catch (IllegalArgumentException iae) {
            //OK
        }

        //null description
        try {
            Mission mission = new Mission(null, "", null, 1, false);
            manager.createMission(mission);
            fail();
        }
        catch (IllegalArgumentException iae) {
            //OK
        }

        //zero Agents
        try {
            Mission mission = new Mission(null, "", "", 0, false);
            manager.createMission(mission);
            fail();
        }
        catch (IllegalArgumentException iae) {
            //OK
        }

        //negative Agents
        try {
            Mission mission = new Mission(null, "", "", -1, false);
            manager.createMission(mission);
            fail();
        }
        catch (IllegalArgumentException iae) {
            //OK
        }

        //completed mission
        try {
            Mission mission = new Mission(null, "", "", 1, true);
            manager.createMission(mission);
            fail();
        }
        catch (IllegalArgumentException iae) {
            //OK
        }


    }

    @Test
    public void updateMission() {

        Mission mission = new Mission(null, "Kill Usama", "Quick in and out", 100, false);
        manager.createMission(mission);

        Long id = mission.getId();
        Mission storedMission = manager.findMissionById(id);
        storedMission.setGoal("Kill Obama");
        storedMission.setDescription("Slow infiltration");
        storedMission.setRequiredAgents(1);
        storedMission.setCompleted(true);

        manager.updateMission(storedMission);
        assertNotEquals(mission, storedMission);
        assertEquals(mission.getId(), storedMission.getId());
        assertNotEquals(mission.getGoal(), storedMission.getGoal());
        assertNotEquals(mission.getDescription(), storedMission.getDescription());
        assertNotEquals(mission.getRequiredAgents(), storedMission.getRequiredAgents());
        assertNotEquals(mission.isCompleted(), storedMission.isCompleted());

    }

    @Test
    public void updateMissionWithWrongAttributes() {

        Mission mission = new Mission(null, "Kill Usama", "Quick in and out", 100, false);
        manager.createMission(mission);

        Long id = mission.getId();
        Mission storedMission = manager.findMissionById(id);

        //no such id
        try {
            storedMission.setId(Long.MAX_VALUE);
            manager.updateMission(storedMission);
            fail();
        }
        catch (IllegalArgumentException iae) {
            //OK
        }

        //null goal
        try {
            storedMission.setGoal(null);
            manager.updateMission(storedMission);
            fail();
        }
        catch (IllegalArgumentException iae) {
            //OK
        }

        //null description
        try {
            storedMission.setDescription(null);
            manager.updateMission(storedMission);
            fail();
        }
        catch (IllegalArgumentException iae) {
            //OK
        }

        //zero agents
        try {
            storedMission.setRequiredAgents(0);
            manager.updateMission(storedMission);
            fail();
        }
        catch (IllegalArgumentException iae) {
            //OK
        }

        //negative agents
        try {
            storedMission.setRequiredAgents(-1);
            manager.updateMission(storedMission);
            fail();
        }
        catch (IllegalArgumentException iae) {
            //OK
        }

    }

    @Test
    public void deleteMission() {

        Mission mission = new Mission(null, "Kill Usama", "Quick in and out", 100, false);
        manager.createMission(mission);

        Long id = mission.getId();
        manager.deleteMission(mission);

        Mission storedMission = manager.findMissionById(id);
        assertNull(storedMission);

    }

}

























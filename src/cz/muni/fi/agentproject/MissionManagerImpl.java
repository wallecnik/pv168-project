package cz.muni.fi.agentproject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Implemented MissionManager interface (Mission CRUD operations).
 *
 * TODO: Possibly modify createMission() so that it actually creates a mission
 *
 * Created by Dužinka on 3. 3. 2015.
 */
public class MissionManagerImpl implements MissionManager {

    private List<Mission> missions = new ArrayList<>();

    /**
     * Creates a new mission.
     * In it's current state, it accepts a mission from outside
     * and adds it to the list of missions, if another mission with
     * the same id isn't already present.
     *
     * @param newMission    mission to be created
     */
    @Override
    public void createMission(Mission newMission) {
        if (newMission == null) {
            throw new IllegalArgumentException("createMission: null parameter");
        }

        for(Mission mission : missions) {
            if (mission.getId() == newMission.getId()) {
                System.err.print("Already contains a mission with the same id");
            }
        }

        missions.add(newMission);
    }

    /**
     * Deletes a mission from the list of missions.
     * @param mission   mission to be deleted
     */
    @Override
    public void deleteMission(Mission mission) {
        if (mission == null) {
            throw new IllegalArgumentException("deleteMission: null parameter");
        }

        for(Mission missionInList : missions) {
            if (mission.equals(missionInList)) {
                missions.remove(mission);
                return;
            }
        }

        System.err.print("No such mission to delete.");
    }

    /**
     * Updates a mission.
     * In fact, checks for identical id in the list of missions and if it finds it,
     * removes the original mission from the list and adds the new one.
     * @param mission   mission to replace the other one with the same id
     */
    @Override
    public void updateMission(Mission mission) {
        if (mission == null) {
            throw new IllegalArgumentException("updateMission: null parameter");
        }

        for(Mission missionInList : missions) {
            if (mission.getId() == missionInList.getId()) {
                missions.remove(missionInList);
                missions.add(mission);
                return;
            }
        }

        System.err.print("No such mission available for update.");
    }

    /**
     * Returns single mission from a list by given id.
     * @param id    id of the wanted mission
     * @return      wanted mission with given id; null if not such mission is present
     */
    @Override
    public Mission findMissionById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("findMissionById: null parameter");
        }

        for(Mission mission : missions) {
            if (id == mission.getId()) {
                return mission;
            }
        }
        return null;
    }

    /**
     * Returns unmodifiable list of all the missions.
     * @return      list of missions; null if no such list exists
     */
    @Override
    public List<Mission> findAllMissions() {
        return Collections.unmodifiableList(missions);
    }
}

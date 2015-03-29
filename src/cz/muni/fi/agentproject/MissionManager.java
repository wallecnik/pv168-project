package cz.muni.fi.agentproject;

import java.util.Set;

/**
 * Interface for MissionManager.
 * Includes basic CRUD operations (create, retrieve, update, delete).
 *
 * Created by Dužinka on 3. 3. 2015.
 */
public interface MissionManager {

    void createMission(Mission mission);
    void deleteMission(Mission mission);
    void updateMission(Mission mission);

    Mission findMissionById(Long id);
    Set<Mission> findAllMissions();
}

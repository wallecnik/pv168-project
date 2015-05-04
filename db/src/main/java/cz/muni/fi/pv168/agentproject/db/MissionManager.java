package cz.muni.fi.pv168.agentproject.db;

import java.util.List;

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
    List<Mission> findAllMissions();
    List<Mission> sortById();
    List<Mission> sortByGoal();
    List<Mission> sortByRequiredAgents();
    List<Mission> sortByCompleted();
}

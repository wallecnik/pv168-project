package cz.muni.fi.agentproject;

import java.util.List;

/**
 * Created by Dužinka on 3. 3. 2015.
 */
public class MissionManagerImpl implements MissionManager {
    private Mission mission;

    @Override
    public void createMission(Mission mission) {
        this.mission = mission;
    }

    @Override
    public boolean deleteMission(Mission mission) {
        return false;
    }

    @Override
    public boolean updateMission(Mission mission) {
        return false;
    }

    @Override
    public Mission findMissionById(Long id) {
        return null;
    }

    @Override
    public List<Mission> findAllMissions() {
        return null;
    }
}

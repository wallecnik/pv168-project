import java.util.List;

/**
 * Created by Dužinka on 3. 3. 2015.
 */
public interface MissionManager {
    void createMission(Mission mission);
    boolean deleteMission(Mission mission);
    boolean updateMission(Long id, Mission mission);

    Mission findMissionById(Long id);
    List<Mission> findAllMissions();
}

/**
 * Created by Dužinka on 3. 3. 2015.
 */
public class Mission {
    private Long id;
    private String description;
    private int requiredAgents;
    private String goal;

    public Mission(String goal, Long id, String description, int requiredAgents) {
        this.goal = goal;
        this.id = id;
        this.description = description;
        this.requiredAgents = requiredAgents;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Mission)) return false;

        Mission mission = (Mission) o;

        if (requiredAgents != mission.requiredAgents) return false;
        if (description != null ? !description.equals(mission.description) : mission.description != null) return false;
        if (goal != null ? !goal.equals(mission.goal) : mission.goal != null) return false;
        return !(id != null ? !id.equals(mission.id) : mission.id != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + requiredAgents;
        result = 31 * result + (goal != null ? goal.hashCode() : 0);
        return result;
    }
}

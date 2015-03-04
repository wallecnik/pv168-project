package cz.muni.fi.agentproject;

/**
 * Class to hold data about mission
 *
 * Created by Dužinka on 3. 3. 2015.
 */
public class Mission {

    private Long id;
    private int requiredAgents;
    private String description;
    private String goal;
    private boolean completed;

    /**
     * Constructor for Mission class
     * @param id                clearly identifiable long number, can be null
     * @param goal              goal of the mission
     * @param description       description of the mission
     * @param requiredAgents    number of agents required to succesfully complete the mission
     * @param completed         indicates the completeness of the mission
     */
    public Mission(Long id, String goal, String description, int requiredAgents, boolean completed) {
        this.id = id;
        this.goal = goal;
        this.description = description;
        this.requiredAgents = requiredAgents;
        this.completed = completed;
    }

    /**
     * Getters
     */

    public Long getId() {
        return id;
    }

    public int getRequiredAgents() {
        return requiredAgents;
    }

    public String getDescription() {
        return description;
    }

    public String getGoal() {
        return goal;
    }

    public boolean isCompleted() {
        return completed;
    }

    /**
     * Setters
     */

    public void setId(Long id) {
        this.id = id;
    }

    public void setRequiredAgents(int requiredAgents) {
        this.requiredAgents = requiredAgents;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    /**
     * Equals + hashCode methods
     */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Mission)) return false;

        Mission mission = (Mission) o;

        if (completed != mission.completed) return false;
        if (requiredAgents != mission.requiredAgents) return false;
        if (description != null ? !description.equals(mission.description) : mission.description != null) return false;
        if (goal != null ? !goal.equals(mission.goal) : mission.goal != null) return false;
        if (id != null ? !id.equals(mission.id) : mission.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + requiredAgents;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (goal != null ? goal.hashCode() : 0);
        result = 31 * result + (completed ? 1 : 0);
        return result;
    }
}

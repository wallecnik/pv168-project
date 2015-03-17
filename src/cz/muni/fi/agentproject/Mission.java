package cz.muni.fi.agentproject;

/**
 * Class to hold data about mission
 *
 * Created by Dužinka on 3. 3. 2015.
 */
public class Mission {

    private Long id;
    private int requiredAgents;
    private String goal;
    private boolean completed;

    /**
     * Constructor for Mission class
     * @param id                clearly identifiable long number, can be null
     * @param goal              goal of the mission
     * @param requiredAgents    number of agents required to succesfully complete the mission
     * @param completed         indicates the completeness of the mission
     */
    public Mission(Long id, String goal, int requiredAgents, boolean completed) {
        this.id = id;
        this.goal = goal;
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

        if (requiredAgents != mission.requiredAgents) return false;
        if (goal != null ? !goal.equals(mission.goal) : mission.goal != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = requiredAgents;
        result = 31 * result + (goal != null ? goal.hashCode() : 0);
        return result;
    }
}

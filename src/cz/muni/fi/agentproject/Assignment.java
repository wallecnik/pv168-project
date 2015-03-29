package cz.muni.fi.agentproject;

import java.time.Instant;

/**
 * This class represents an assignment of an agent to a mission.
 * It's just a holder for a few attributes like structure in C.
 *
 * @author  Wallecnik
 * @version 31.2.2015
 */
public class Assignment {

    private Long id;
    private Agent agent;
    private Mission mission;
    private Instant startTime;
    private Instant endTime;

    public Assignment(Long id, Agent agent, Mission mission, Instant startTime, Instant endTime) {
        this.id = id;
        this.agent = agent;
        this.mission = mission;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    /**
     * Getters
     */

    public Long getId() {
        return id;
    }

    public Agent getAgent() {
        return agent;
    }

    public Mission getMission() {
        return mission;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public Instant getEndTime() {
        return endTime;
    }

    /**
     * Setters
     */

    public void setId(Long id) {
        this.id = id;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public void setMission(Mission mission) {
        this.mission = mission;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    /**
     * Equals & HashCode
     */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Assignment)) return false;

        Assignment that = (Assignment) o;

        if (agent != null ? !agent.equals(that.agent) : that.agent != null) return false;
        if (endTime != null ? !endTime.equals(that.endTime) : that.endTime != null) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (mission != null ? !mission.equals(that.mission) : that.mission != null) return false;
        if (startTime != null ? !startTime.equals(that.startTime) : that.startTime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (agent != null ? agent.hashCode() : 0);
        result = 31 * result + (mission != null ? mission.hashCode() : 0);
        result = 31 * result + (startTime != null ? startTime.hashCode() : 0);
        result = 31 * result + (endTime != null ? endTime.hashCode() : 0);
        return result;
    }

    /**
     * Returns a brief description of this assignment. The exact format is unspecified
     * and may change.
     *
     * @return String representing this Assignment
     */
    @Override
    public String toString() {
        return "Assignment{" +
                "agent=" + agent +
                ", mission=" + mission +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}

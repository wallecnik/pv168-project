package cz.muni.fi.agentproject;

/**
 * @author  Wallecnik
 * @version 31.22.2015
 */
public class Assignment {

    private Long id;
    private Agent agent;
    private Mission mission;
    private long startTime;
    private long endtime;

    public Assignment(Long id, Agent agent, Mission mission, long startTime, long endtime) {
        this.id = id;
        this.agent = agent;
        this.mission = mission;
        this.startTime = startTime;
        this.endtime = endtime;
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

    public long getStartTime() {
        return startTime;
    }

    public long getEndtime() {
        return endtime;
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

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public void setEndtime(long endtime) {
        this.endtime = endtime;
    }
}

package cz.muni.fi.agentproject;

/**
 * @author  Wallecnik
 * @version 31.22.2015
 */
public class Assignment {

    private Long id;
    private Agent agent;
    private Mission mission;
    private Long startTime;
    private Long endtime;

    public Assignment(Long id, Agent agent, Mission mission, Long startTime, Long endtime) {
        this.id = id;
        this.agent = agent;
        this.mission = mission;
        this.startTime = startTime;
        this.endtime = endtime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public Mission getMission() {
        return mission;
    }

    public void setMission(Mission mission) {
        this.mission = mission;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndtime() {
        return endtime;
    }

    public void setEndtime(Long endtime) {
        this.endtime = endtime;
    }
}

package cz.muni.fi.agentproject;

import java.time.Instant;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class represents a secret agent. It's just a holder of few attributes like structure in C.
 *
 * @author  Wallecnik
 * @version 31.2.2015
 */
public class Agent {

    private Long id;
    private String name;
    private Instant born;

    /**
     * Constructor for objects of class Agent.
     *
     * @param id          synthetic id of this Agent
     * @param name        name of this Agent
     * @param born        long value of time in miliseconds
     */
    public Agent(Long id, String name, Instant born) {
        this.id = id;
        this.name = name;
        this.born = born;
    }

    /**
     * Getters
     */

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Instant getBorn() {
        return born;
    }

    /**
     * Setters
     */

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBorn(Instant born) {
        this.born = born;
    }

    /**
     * Equals + hashCode methods
     */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Agent)) return false;

        Agent agent = (Agent) o;

        if (born != null ? !born.equals(agent.born) : agent.born != null) return false;
        if (id != null ? !id.equals(agent.id) : agent.id != null) return false;
        if (name != null ? !name.equals(agent.name) : agent.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (born != null ? born.hashCode() : 0);
        return result;
    }

    /**
     * Returns a brief description of this agent. Exact format is unspecified,
     * but the following may look typically like that:
     *
     * "Agent{name='John Doe', born=2015-03-25T08:44:05.143Z}"
     *
     * @return String representing this Agent
     */
    @Override
    public String toString() {
        return "Agent{" +
                "name='" + name + '\'' +
                ", born=" + born +
                '}';
    }

}




















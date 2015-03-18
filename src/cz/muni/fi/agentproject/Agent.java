package cz.muni.fi.agentproject;

/**
 * This class represents a secret agent. It's just a holder of few attributes like structure in C.
 *
 * @author  Wallecnik
 * @version 31.22.2015
 */
public class Agent {

    private Long id;
    private String name;
    private long born;

    /**
     * Constructor for objects of class Agent.
     *
     * @param id          synthetic id of this Agent
     * @param name        name of this Agent
     * @param born        long value of time the agent was born
     */
    public Agent(Long id, String name, long born) {
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

    public long getBorn() {
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

    public void setBorn(long born) {
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

        if (born != agent.born) return false;
        if (name != null ? !name.equals(agent.name) : agent.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (int) (born ^ (born >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "Agent{" +
                "name='" + name + '\'' +
                ", born=" + born +
                '}';
    }
}

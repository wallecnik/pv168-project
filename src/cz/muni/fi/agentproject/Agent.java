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
    private Long born;
    private String description;

    /**
     * Constructor for objects of class Agent.
     *
     * @param id          synthetic id of this Agent
     * @param name        name of this Agent
     * @param born        long value of time the agent was born
     * @param description short description of this Agent
     */
    public Agent(Long id, String name, Long born, String description) {
        this.id = id;
        this.name = name;
        this.born = born;
        this.description = description;
    }

    /**
     * Getters and setters
     */

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getBorn() {
        return born;
    }

    public void setBorn(Long born) {
        this.born = born;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

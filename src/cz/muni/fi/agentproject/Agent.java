package cz.muni.fi.agentproject;

/**
 * Created by Wallecnik on 03.03.15.
 */
public class Agent {

    private Long id;
    private String name;
    private Long born;
    private String description;

    public Agent(Long id, String name, Long born, String description) {
        this.id = id;
        this.name = name;
        this.born = born;
        this.description = description;
    }
}

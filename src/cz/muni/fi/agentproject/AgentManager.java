package cz.muni.fi.agentproject;

import java.util.Set;

/**
 * Interface for basic CRUD operations for Agent instances.
 *
 * Implementation should check the state of the Agent instances before saving them
 * in order to store valid data.
 *
 * @author  Wallecnik
 * @version 24.3.2015
 */
public interface AgentManager {

    /**
     * Adds a new Agent. Provided Agent should have valid values associated. If not, the method should
     * throw an IllegalArgumentException.
     *
     * @param agent  An instance of an Agent
     */
    void createAgent(Agent agent);

    /**
     * Retrieves an Agent or returns null if no agent with provided id was found.
     *
     * @param id  id of an Agent to look for
     * @return  a new instance of an Agent or null
     */
    Agent findAgentById(Long id);

    /**
     * Updates an existing Agent. Provided Agent should have valid values associated.
     * If not, the method should throw an IllegalArgumentException.
     *
     * @param agent  an instance of an Agent
     */
    void updateAgent(Agent agent);

    /**
     * Deletes Agent. Provided instance of the Agent should have valid id.
     *
     * @param agent  an instance of an Agent
     */
    void deleteAgent(Agent agent);

    /**
     * Returns List of all Agents.
     *
     * @return  List of Agents
     */
    Set<Agent> findAllAgents() throws ServiceFailureException;

}

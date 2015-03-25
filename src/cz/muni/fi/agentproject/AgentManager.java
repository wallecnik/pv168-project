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
     * Adds a new Agent. Provided Agent should have valid values associated.
     *
     * @param agent  An instance of an Agent
     * @throws IllegalArgumentException  if an illegal state of Agent was provided
     */
    void createAgent(Agent agent) throws IllegalArgumentException;

    /**
     * Retrieves an Agent or returns null if no agent with provided id was found.
     *
     * @param id  id of an Agent to look for
     * @return  a new instance of an Agent or null
     * @throws IllegalArgumentException if the provided id is null or negative
     */
    Agent findAgentById(Long id) throws IllegalArgumentException;

    /**
     * Updates an existing Agent. Provided Agent should have valid values associated.
     *
     * @param agent  an instance of an Agent
     * @throws IllegalArgumentException if the Agent has invalid data
     */
    void updateAgent(Agent agent) throws IllegalArgumentException;

    /**
     * Deletes Agent. Provided instance of the Agent should have valid id.
     *
     * @param agent  an instance of an Agent
     * @throws IllegalArgumentException if the Agent is invalid
     */
    void deleteAgent(Agent agent) throws IllegalArgumentException;

    /**
     * Returns List of all Agents.
     *
     * @return  List of Agents
     */
    Set<Agent> findAllAgents();

}

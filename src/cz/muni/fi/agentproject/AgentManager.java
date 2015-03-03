package cz.muni.fi.agentproject;

import java.util.List;

/**
 * @author  Wallecnik
 * @version 31.22.2015
 */
public interface AgentManager {

    void createAgent(Agent agent);

    Agent findAgentById(Long id);

    void updateAgent(Agent agent);

    void deleteAgent(Agent agent);

    List<Agent> findAllAgents();

}

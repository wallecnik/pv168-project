package cz.muni.fi.pv168.agentproject.web;

import cz.muni.fi.pv168.agentproject.db.*;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;
import java.util.logging.Logger;

/**
 * This listener sets up connection to the database and creates entity managers.
 *
 * @author Wallecnik
 * @version 1.0-SNAPSHOT
 */
@WebListener
public class StartListener implements ServletContextListener {

    public final static Logger logger = Logger.getLogger(StartListener.class.getName());

    @Resource(name = "jdbc/AgentProject")
    private DataSource dataSource;

    @Override
    public void contextInitialized(ServletContextEvent ev) {
        logger.info("Web application initiated");
        ServletContext servletContext = ev.getServletContext();

        AgentManager agentManager = new AgentManagerImpl(dataSource);
        servletContext.setAttribute("agentManager", agentManager);
        MissionManager missionManager = new MissionManagerImpl(dataSource);
        servletContext.setAttribute("missionManager", missionManager);
        AssignmentManager assignmentManager = new AssignmentManagerImpl(dataSource, agentManager, missionManager);
        servletContext.setAttribute("assignmentManager", assignmentManager);

        logger.info("Managers created");
    }

    @Override
    public void contextDestroyed(ServletContextEvent ev) {
        logger.info("Application ends");
    }
}
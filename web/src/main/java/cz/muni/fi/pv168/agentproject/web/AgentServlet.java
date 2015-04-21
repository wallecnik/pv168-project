package cz.muni.fi.pv168.agentproject.web;

import cz.muni.fi.pv168.agentproject.db.Agent;
import cz.muni.fi.pv168.agentproject.db.AgentManager;
import cz.muni.fi.pv168.agentproject.db.Constants;
import cz.muni.fi.pv168.agentproject.db.ServiceFailureException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * Created by Wallecnik on 17.04.15.
 */
@WebServlet(AgentServlet.AGENT_URL + "/*")
public class AgentServlet extends HttpServlet{

    public  static final String AGENT_URL = "/agents";
    private static final String AGENT_JSP = "/agents.jsp";

    public final static Logger logger = Logger.getLogger(AgentServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getPathInfo();
        if (action != null) {
            if (action.equals("/edit")) {
                Long id = Long.valueOf(request.getParameter("id"));
                request.setAttribute("editAgent", getAgentManager().findAgentById(id));
            }
        }
        showAgents(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getPathInfo();
        Long id = null;
        String name = null;
        Instant born = null;
        switch (action) {
            case "/edit":
                id = Long.valueOf(request.getParameter("id"));
                //no return because editing and adding is similar
            case "/add":
                name = request.getParameter("name");
                String day   = request.getParameter("day");
                String month = request.getParameter("month");
                String year  = request.getParameter("year");

                if (name == null || name.equals("") || day == null || day.equals("") || month == null || month.equals("") || year == null || year.equals("")) {
                    request.setAttribute("error", "agent.form.error.missing_field");
                    showAgents(request, response);
                    return;
                }

                try {
                    if (Integer.valueOf(day) < 0 || Integer.valueOf(day) > 31 || Integer.valueOf(month) < 0 || Integer.valueOf(month) > 12) {
                        request.setAttribute("error", "agent.form.error.wrong_number_format");
                        showAgents(request, response);
                        return;
                    }
                } catch (NumberFormatException e) {
                    request.setAttribute("error", "agent.form.error.wrong_number_format");
                    showAgents(request, response);
                    return;
                }

                ZoneId zoneId = ZoneId.of(request.getParameter("zoneId"));
                born = ZonedDateTime.of(Integer.valueOf(year), Integer.valueOf(month), Integer.valueOf(day), 0, 0, 0, 0, zoneId).toInstant();

                if (! Pattern.matches(Constants.AGENT_NAME_REGEX, name)) {
                    request.setAttribute("error", "agent.form.error.wrong_name_format");
                    showAgents(request, response);
                    return;
                }
                ZonedDateTime agentBorn = ZonedDateTime.ofInstant(born, ZoneId.systemDefault());
                if (agentBorn.compareTo(ZonedDateTime.now()) > 0) {
                    request.setAttribute("error", "agent.form.error.not_born_yet");
                    showAgents(request, response);
                    return;
                }
                if (agentBorn.plusYears(100).compareTo(ZonedDateTime.now()) < 0) {
                    request.setAttribute("error", "agent.form.error.too_old");
                    showAgents(request, response);
                    return;
                }

                Agent agent = new Agent(id, name, born);

                try {
                    switch (action) {
                        case "/edit":
                            getAgentManager().updateAgent(agent);
                            logger.info("Updated agent: " + agent);
                            break;
                        case "/add":
                            getAgentManager().createAgent(agent);
                            logger.info("Added agent: " + agent);
                            break;
                    }
                    response.sendRedirect(request.getContextPath() + AGENT_URL);
                } catch (ServiceFailureException e) {
                    logger.severe("Failed to create agent." + e);
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
                }

                return;
            case "/delete":
                id = Long.valueOf(request.getParameter("id"));
                try {
                    getAgentManager().deleteAgent(new Agent(id, name, born));
                    logger.info("Deleted agent. Id: " + id);
                    response.sendRedirect(request.getContextPath() + AGENT_URL);
                } catch (ServiceFailureException e) {
                    logger.severe("Failed to delete agent." + e);
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
                }
                return;
            default:
                logger.severe("Unknown action " + action);
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Unknown action " + action);
                return;
        }
    }

    private AgentManager getAgentManager() {
        return (AgentManager) this.getServletContext().getAttribute("agentManager");
    }

    private void showAgents(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            request.setAttribute("agents", getAgentManager().findAllAgents());
            request.getRequestDispatcher(AGENT_JSP).forward(request, response);
        } catch (ServiceFailureException e) {
            logger.severe("Error when showing the list of Agents");
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

}

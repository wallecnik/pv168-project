package cz.muni.fi.pv168.agentproject.web;

import cz.muni.fi.pv168.agentproject.db.Constants;
import cz.muni.fi.pv168.agentproject.db.Mission;
import cz.muni.fi.pv168.agentproject.db.MissionManager;
import cz.muni.fi.pv168.agentproject.db.ServiceFailureException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * Created by Dužinka on 20. 4. 2015.
 */
@WebServlet(MissionServlet.MISSION_URL + "/*")
public class MissionServlet extends HttpServlet  {

    public static final Logger logger = Logger.getLogger(StartListener.class.getName());
    public static final String MISSION_JSP = "/missions.jsp";
    public static final String MISSION_URL = "/missions";

    public static boolean sortById = false;
    public static boolean sortByGoal = false;
    public static boolean sortByRequiredAgents = false;
    public static boolean sortByCompleted = false;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getPathInfo();

        if (action != null) {
            switch(action) {
                case "/update":
                    Long id = Long.valueOf(request.getParameter("id"));
                    request.setAttribute("oneMission", getMissionManager().findMissionById(id));
                    break;
                case "/sortById":
                    setSortFlag(Flag.ID);
                    break;
                case "/sortByGoal":
                    setSortFlag(Flag.GOAL);
                    break;
                case "/sortByRequiredAgents":
                    setSortFlag(Flag.REQ_AGENTS);
                    break;
                case "/sortByCompleted":
                    setSortFlag(Flag.COMPLETED);
                    break;
                default:
                    break;
            }
        }

        showMissions(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        //akce podle přípony v URL
        String action = request.getPathInfo();
        switch (action) {
            case "/add":
                //načtení POST parametrů z formuláře
                String goal = request.getParameter("goal");
                String requiredAgentsString = request.getParameter("requiredAgents");
                String completedString = request.getParameter("completed");

                //kontrola vyplnění hodnot
                if (goal == null || goal.length() == 0 || requiredAgentsString == null ||
                        requiredAgentsString.length() == 0 || completedString == null || completedString.length() == 0) {
                    request.setAttribute("errorHtml", "It's necessary to fill in all values!");
                    showMissions(request, response);
                    return;
                }

                if (!completedString.equals("true") && !completedString.equals("false")) {
                    request.setAttribute("errorHtml", "Wrong boolean format!");
                    showMissions(request, response);
                    return;
                }

                if (! Pattern.matches(Constants.AGENT_NAME_REGEX, goal)) {
                    request.setAttribute("errorHtml", "Goal contains illegal characters!");
                    showMissions(request, response);
                    return;
                }

                if (goal.toLowerCase().contains("ě") || goal.toLowerCase().contains("č") ||
                        goal.toLowerCase().contains("ř") || goal.toLowerCase().contains("ď") ||
                        goal.toLowerCase().contains("ť") || goal.toLowerCase().contains("ň") ||
                        goal.toLowerCase().contains("ľ")) {
                    request.setAttribute("errorHtml", "Goal contains unsupported characters!");
                    showMissions(request, response);
                    return;
                }

                int requiredAgents = Integer.parseInt(requiredAgentsString);
                boolean completed = Boolean.parseBoolean(completedString);


                try {
                    Mission mission = new Mission(null, goal, requiredAgents, completed);
                    getMissionManager().createMission(mission);
                    logger.info("created " + mission);
                    //redirect-after-POST je ochrana před vícenásobným odesláním formuláře
                    response.sendRedirect(request.getContextPath()+MISSION_URL);
                    return;
                } catch (ServiceFailureException ex) {
                    logger.severe("Cannot add mission: " + ex);
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
                    return;
                }

            case "/delete":
                try {
                    Long id = Long.valueOf(request.getParameter("id"));
                    Mission missionToDelete = getMissionManager().findMissionById(id);
                    getMissionManager().deleteMission(missionToDelete);
                    logger.info("deleted mission " + id);
                    response.sendRedirect(request.getContextPath()+MISSION_URL);
                    return;
                } catch (ServiceFailureException ex) {
                    logger.severe("Cannot delete mission: " + ex);
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
                    return;
                }

            case "/update":
                Long id = Long.valueOf(request.getParameter("id"));
                goal = request.getParameter("goal");
                requiredAgentsString = request.getParameter("requiredAgents");
                completedString = request.getParameter("completed");

                if (goal == null || goal.length() == 0 || requiredAgentsString == null ||
                        requiredAgentsString.length() == 0 || completedString == null || completedString.length() == 0) {
                    request.setAttribute("errorHtml", "It's necessary to fill in all values!");
                    showMissions(request, response);
                    return;
                }

                if (!completedString.equals("true") && !completedString.equals("false")) {
                    request.setAttribute("errorHtml", "Wrong boolean format!");
                    showMissions(request, response);
                    return;
                }

                if (! Pattern.matches(Constants.AGENT_NAME_REGEX, goal)) {
                    request.setAttribute("errorHtml", "Goal contains illegal characters!");
                    showMissions(request, response);
                    return;
                }

                if (goal.toLowerCase().contains("ě") || goal.toLowerCase().contains("č") ||
                        goal.toLowerCase().contains("ř") || goal.toLowerCase().contains("ď") ||
                        goal.toLowerCase().contains("ť") || goal.toLowerCase().contains("ň") ||
                        goal.toLowerCase().contains("ľ")) {
                    request.setAttribute("errorHtml", "Goal contains unsupported characters!");
                    showMissions(request, response);
                    return;
                }

                requiredAgents = Integer.parseInt(requiredAgentsString);
                completed = Boolean.parseBoolean(completedString);

                try {
                    Mission mission = getMissionManager().findMissionById(id);
                    mission.setGoal(goal);
                    mission.setRequiredAgents(requiredAgents);
                    mission.setCompleted(completed);
                    getMissionManager().updateMission(mission);
                    logger.info("updated " + mission);
                    //redirect-after-POST je ochrana před vícenásobným odesláním formuláře
                    response.sendRedirect(request.getContextPath()+MISSION_URL);
                    return;
                } catch (ServiceFailureException ex) {
                    logger.severe("Cannot update mission: " + ex);
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
                    return;
                }
            default:
                logger.severe("Unknown action " + action);
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Unknown action " + action);
        }
    }

    private MissionManager getMissionManager() {
        return (MissionManager) this.getServletContext().getAttribute("missionManager");
    }

    private void showMissions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            if (sortById) {
                request.setAttribute("missions", getMissionManager().sortById());
            }
            else if (sortByGoal) {
                request.setAttribute("missions", getMissionManager().sortByGoal());
            }
            else if (sortByRequiredAgents) {
                request.setAttribute("missions", getMissionManager().sortByRequiredAgents());
            }
            else if (sortByCompleted) {
                request.setAttribute("missions", getMissionManager().sortByCompleted());
            }
            else {
                request.setAttribute("missions", getMissionManager().findAllMissions());
            }
            request.getRequestDispatcher(MISSION_JSP).forward(request, response);
        }
        catch (ServiceFailureException ex) {
            logger.severe("Error when forwarding to missions page");
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
        }
    }

    private void setSortFlag(Flag flag) {
        switch(flag) {
            case ID:
                sortById = true;
                sortByGoal = false;
                sortByRequiredAgents = false;
                sortByCompleted = false;
                break;
            case GOAL:
                sortById = false;
                sortByGoal = true;
                sortByRequiredAgents = false;
                sortByCompleted = false;
                break;
            case REQ_AGENTS:
                sortById = false;
                sortByGoal = false;
                sortByRequiredAgents = true;
                sortByCompleted = false;
                break;
            case COMPLETED:
                sortById = false;
                sortByGoal = false;
                sortByRequiredAgents = false;
                sortByCompleted = true;
                break;
            default:
                break;
        }
    }
}

enum Flag {
    ID, GOAL, REQ_AGENTS, COMPLETED
}

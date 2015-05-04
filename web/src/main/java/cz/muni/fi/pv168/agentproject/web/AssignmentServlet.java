package cz.muni.fi.pv168.agentproject.web;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Wallecnik
 * @version 1.0-SNAPSHOT
 */
@WebServlet(AssignmentServlet.ASSIGNMENT_URL + "/*")
public class AssignmentServlet extends HttpServlet{

    public  static final String ASSIGNMENT_URL = "/assignments";
    private static final String ASSIGNMENT_JSP = "/assignments.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher(ASSIGNMENT_JSP).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher(ASSIGNMENT_JSP).forward(request, response);
    }

}

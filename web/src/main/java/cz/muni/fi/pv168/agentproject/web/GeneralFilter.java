package cz.muni.fi.pv168.agentproject.web;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * This filter filters every request and response that comes in and out of the server.
 * Filter sets encoding to UTF-8
 *
 * @author Wallecnik
 * @version 1.0-SNAPSHOT
 */
@WebFilter("/*")
public class GeneralFilter implements Filter {

    public static final Logger logger = Logger.getLogger(GeneralFilter.class.getName());

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("Filter intialized");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        servletRequest.setCharacterEncoding("utf-8");
        servletResponse.setContentType("text/html; charset=utf-8");
        servletResponse.setCharacterEncoding("utf-8");
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        logger.info("Filter destroyed");
    }
}

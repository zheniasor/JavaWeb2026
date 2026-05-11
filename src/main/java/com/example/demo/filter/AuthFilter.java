package com.example.demo.filter;

import com.example.demo.command.AttributeConstants;
import com.example.demo.controller.ParameterConstants;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@WebFilter(urlPatterns = {"/*"})
public class AuthFilter implements Filter {

    private static final Logger LOGGER = LogManager.getLogger(AuthFilter.class);

    private static final Set<String> STATIC_EXTENSIONS = new HashSet<>(Arrays.asList(
            ".css", ".js", ".jpg", ".jpeg", ".png", ".gif", ".svg",
            ".ico", ".woff", ".woff2", ".ttf", ".eot", ".map", ".webp"
    ));

    private static final Set<String> PUBLIC_PATHS = new HashSet<>(Arrays.asList(
            "/index.jsp",
            "/pages/register.jsp",
            "/pages/confirmation.jsp",
            "/pages/error/error_404.jsp",
            "/pages/error/error_500.jsp"
    ));

    private static final Set<String> PUBLIC_COMMANDS = new HashSet<>(Arrays.asList(
            "LOGIN", "ADD_USER", "CONFIRM", "CHANGE_LANGUAGE", "DEFAULT"
    ));

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        LOGGER.info("AuthFilter initialized");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String uri = req.getRequestURI();
        String contextPath = req.getContextPath();
        String path = uri.substring(contextPath.length());

        String command = req.getParameter(ParameterConstants.COMMAND_PARAM);

       if (isStaticResource(path)) {
            LOGGER.debug("Static resource - allowing without auth: {}", path);
            chain.doFilter(request, response);
            return;
        }

       if (PUBLIC_PATHS.contains(path)) {
            LOGGER.debug("Public path - allowing without auth: {}", path);
            chain.doFilter(request, response);
            return;
        }

        if (command != null && PUBLIC_COMMANDS.contains(command.toUpperCase())) {
            LOGGER.debug("Public command - allowing without auth: {}", command);
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute(AttributeConstants.USER_ATTR) == null) {
            LOGGER.warn("Unauthorized access to: {}, redirecting to login", path);
            res.sendRedirect(req.getContextPath() + "/index.jsp");
            return;
        }

        String currentLang = (String) session.getAttribute("currentLang");
        if (currentLang == null) {
            currentLang = "ru";
            session.setAttribute("currentLang", currentLang);
        }
        req.setAttribute("currentLang", currentLang);

        LOGGER.debug("Authorized access: {} by user {}", path, session.getAttribute(AttributeConstants.USER_ATTR));
        chain.doFilter(request, response);
    }

    private boolean isStaticResource(String path) {
        for (String ext : STATIC_EXTENSIONS) {
            if (path.toLowerCase().endsWith(ext)) {
                return true;
            }
        }

        if (path.startsWith("/css/") || path.startsWith("/js/") ||
                path.startsWith("/images/") || path.startsWith("/img/") ||
                path.startsWith("/fonts/") || path.startsWith("/static/") ||
                path.startsWith("/assets/") || path.startsWith("/uploads/")) {
            return true;
        }

        return false;
    }

    @Override
    public void destroy() {
        LOGGER.info("AuthFilter destroyed");
    }
}
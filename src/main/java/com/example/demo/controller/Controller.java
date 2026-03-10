package com.example.demo.controller;

import com.example.demo.command.Command;
import com.example.demo.command.CommandType;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@WebServlet(urlPatterns = {"/controller", "*.do"})
public class Controller extends HttpServlet {

    private static final Logger LOGGER = LogManager.getLogger(Controller.class);

    private static final String CONTENT_TYPE = "text/html";
    private static final String COMMAND_PARAM = "command";
    private static final String DEFAULT_COMMAND = "DEFAULT";
    private static final String INDEX_PAGE = "index.jsp";

    @Override
    public void init() {
        LOGGER.info("Initializing Controller");
        LOGGER.info("Controller initialized successfully");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType(CONTENT_TYPE);
        String commandStr = request.getParameter(COMMAND_PARAM);

        if (commandStr == null || commandStr.isEmpty()) {
            commandStr = DEFAULT_COMMAND;
            LOGGER.debug("Command parameter is empty, using DEFAULT");
        }

        LOGGER.info("Received command: {}", commandStr);

        try {
            Command command = CommandType.define(commandStr);
            String page = command.execute(request);
            LOGGER.info("Forwarding to page: {}", page);

            request.getRequestDispatcher(page).forward(request, response);
        } catch (Exception e) {
            LOGGER.error("Error executing command: " + commandStr, e);
            response.sendRedirect(request.getContextPath() + INDEX_PAGE);
        }
    }
}
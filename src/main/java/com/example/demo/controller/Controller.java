package com.example.demo.controller;

import com.example.demo.command.Command;
import com.example.demo.command.CommandType;
import com.example.demo.constants.PageConstants;
import com.example.demo.constants.ParameterConstants;
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

        String commandStr = request.getParameter(ParameterConstants.COMMAND_PARAM);

        if (commandStr == null || commandStr.isBlank()) {
            commandStr = ParameterConstants.DEFAULT_COMMAND;
            LOGGER.debug("Command parameter is empty, using DEFAULT");
        }

        LOGGER.info("Received command: {}", commandStr);

        try {
            Command command = CommandType.define(commandStr);
            String page = command.execute(request);
            LOGGER.info("Forwarding to page: {}", page);

            request.getRequestDispatcher(page).forward(request, response);
        } catch (IllegalArgumentException e) {
            LOGGER.error("Error executing command: " + commandStr, e);
            response.sendRedirect(request.getContextPath() + PageConstants.INDEX_PAGE);
        }
    }
}
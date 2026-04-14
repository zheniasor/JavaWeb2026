package com.example.demo.controller;

import com.example.demo.command.Command;
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

        LOGGER.info("Received command: {}", commandStr);

        Command command = CommandType.define(commandStr);
        String page = command.execute(request);
        LOGGER.info("Forwarding to page: {}", page);

        request.getRequestDispatcher(page).forward(request, response);
    }
}
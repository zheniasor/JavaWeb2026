package com.example.demo.controller;

import com.example.demo.command.Command;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@WebServlet(urlPatterns = {"/controller", "*.do"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2,
        maxFileSize = 1024 * 1024 * 5,
        maxRequestSize = 1024 * 1024 * 10
)
public class Controller extends HttpServlet {

    private static final Logger LOGGER = LogManager.getLogger(Controller.class);

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

        request.setAttribute("response", response);

        Command command = CommandType.define(commandStr);
        String page = command.execute(request);

        request.removeAttribute("response");

        if (page != null && page.startsWith("redirect:")) {
            String redirectUrl = page.substring("redirect:".length());
            response.sendRedirect(redirectUrl);
            return;
        }

        if (page != null) {
            LOGGER.info("Forwarding to page: {}", page);
            request.getRequestDispatcher(page).forward(request, response);
        } else {
            LOGGER.error("Command returned null page!");
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
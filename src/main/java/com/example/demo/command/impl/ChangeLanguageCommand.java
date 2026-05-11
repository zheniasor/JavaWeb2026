package com.example.demo.command.impl;

import com.example.demo.command.Command;
import com.example.demo.command.PageConstants;
import com.example.demo.util.LocalizationUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ChangeLanguageCommand implements Command {

    private static final Logger LOGGER = LogManager.getLogger(ChangeLanguageCommand.class);

    @Override
    public String execute(HttpServletRequest request) {
        String lang = request.getParameter("lang");
        String returnPage = request.getParameter("page");

        HttpServletResponse response = (HttpServletResponse) request.getAttribute("response");

        if (response == null) {
            LOGGER.error("HttpServletResponse not found in request attributes!");
            return PageConstants.INDEX_PAGE;
        }

        if (lang != null && (lang.equals("ru") || lang.equals("en"))) {
            LocalizationUtil.setLanguage(request, response, lang);
            LOGGER.info("Language changed to: {}", lang);
        }

        request.getSession().setAttribute("currentLang", lang);

        if ("true".equals(request.getParameter("ajax"))) {
            request.setAttribute("success", true);
            return PageConstants.INDEX_PAGE; // или специальная страница для AJAX
        }

        String referer = request.getHeader("Referer");
        if (referer != null && !referer.isEmpty()) {
             return "redirect:" + referer;
        }

        return PageConstants.INDEX_PAGE;
    }
}
package com.example.demo.command.impl;

import com.example.demo.command.Command;
import jakarta.servlet.http.HttpServletRequest;

public class DefaultCommand implements Command {
    private static final String INDEX_PAGE = "index.jsp";

    @Override
    public String execute(HttpServletRequest request) {
        return INDEX_PAGE;
    }
}

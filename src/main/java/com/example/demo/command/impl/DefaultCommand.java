package com.example.demo.command.impl;

import com.example.demo.command.Command;
import com.example.demo.constants.PageConstants;
import jakarta.servlet.http.HttpServletRequest;

public class DefaultCommand implements Command {

    @Override
    public String execute(HttpServletRequest request) {
        return PageConstants.INDEX_PAGE;
    }
}
package com.example.demo.command.impl;

import com.example.demo.command.PageConstants;
import com.example.demo.command.AttributeConstants;
import com.example.demo.controller.ParameterConstants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginCommandTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpSession session;

    private LoginCommand loginCommand;

    @BeforeEach
    void setUp() {
        loginCommand = new LoginCommand();
    }

    @Test
    @DisplayName("Должен вернуть главную страницу при успешном входе")
    void execute_ShouldReturnMainPage_WhenLoginSuccessful() {
        when(request.getParameter(ParameterConstants.LOGIN_PARAM)).thenReturn("john123");
        when(request.getParameter(ParameterConstants.PASSWORD_PARAM)).thenReturn("pass123");
        when(request.getSession()).thenReturn(session);

        String result = loginCommand.execute(request);

        assertThat(result).isEqualTo(PageConstants.MAIN_PAGE);
        verify(session).setAttribute(eq(AttributeConstants.USER_ATTR), anyString());
    }

    @Test
    @DisplayName("Должен вернуть страницу входа при пустых полях")
    void execute_ShouldReturnIndexPage_WhenFieldsAreEmpty() {
        when(request.getParameter(ParameterConstants.LOGIN_PARAM)).thenReturn("");
        when(request.getParameter(ParameterConstants.PASSWORD_PARAM)).thenReturn("");

        String result = loginCommand.execute(request);

        assertThat(result).isEqualTo(PageConstants.INDEX_PAGE);
        verify(request).setAttribute(eq(AttributeConstants.ERROR_MESSAGE_ATTR), anyString());
    }
}
package com.example.demo.command.impl;

import com.example.demo.command.PageConstants;
import com.example.demo.controller.ParameterConstants;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddUserCommandTest {

    @Mock
    private HttpServletRequest request;

    private AddUserCommand addUserCommand;

    @BeforeEach
    void setUp() {
        addUserCommand = new AddUserCommand();
    }

    @Test
    @DisplayName("Должен вернуть register.jsp при ошибке валидации")
    void execute_ShouldReturnRegisterPage_WhenValidationFails() {
        when(request.getParameter(ParameterConstants.LOGIN_PARAM)).thenReturn("ab");
        when(request.getParameter(ParameterConstants.PASSWORD_PARAM)).thenReturn("pass123");
        when(request.getParameter(ParameterConstants.EMAIL_PARAM)).thenReturn("john@example.com");

        String result = addUserCommand.execute(request);

        assertThat(result).isEqualTo(PageConstants.REGISTER_PAGE);
        verify(request).setAttribute(eq("errors"), any());
    }

    @Test
    @DisplayName("Должен вернуть страницу подтверждения или регистрации при валидных данных")
    void execute_ShouldReturnConfirmationOrRegisterPage_WhenValidData() {
        when(request.getParameter(ParameterConstants.LOGIN_PARAM)).thenReturn("john123");
        when(request.getParameter(ParameterConstants.PASSWORD_PARAM)).thenReturn("pass123");
        when(request.getParameter(ParameterConstants.EMAIL_PARAM)).thenReturn("john@example.com");

        String result = addUserCommand.execute(request);

        assertThat(result).isIn(PageConstants.CONFIRMATION_PAGE, PageConstants.REGISTER_PAGE);
    }
}
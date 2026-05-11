package com.example.demo.command.impl;

import com.example.demo.command.PageConstants;
import com.example.demo.command.AttributeConstants;
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
class LogoutCommandTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpSession session;

    private LogoutCommand logoutCommand;

    @BeforeEach
    void setUp() {
        logoutCommand = new LogoutCommand();
    }

    @Test
    @DisplayName("Должен завершить сессию и вернуть index.jsp")
    void execute_ShouldInvalidateSessionAndReturnIndexPage() {
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute(AttributeConstants.USER_ATTR)).thenReturn("john123");

        String result = logoutCommand.execute(request);

        assertThat(result).isEqualTo(PageConstants.INDEX_PAGE);
        verify(session).invalidate();
    }

    @Test
    @DisplayName("Должен вернуть index.jsp когда нет активной сессии")
    void execute_ShouldReturnIndexPage_WhenNoActiveSession() {
        when(request.getSession(false)).thenReturn(null);

        String result = logoutCommand.execute(request);

        assertThat(result).isEqualTo(PageConstants.INDEX_PAGE);
        verify(session, never()).invalidate();
    }
}
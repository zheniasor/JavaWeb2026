<%@ page isErrorPage="true" contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>500 - Внутренняя ошибка сервера</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="welcome" style="max-width: 600px;">
    <h1>500 - Внутренняя ошибка сервера</h1>

    <div class="message error" style="text-align: left;">
        <p><strong>Извините, на сервере произошла ошибка.</strong></p>

        <% if (exception != null) { %>
        <p><strong>Тип ошибки:</strong> <%= exception.getClass().getName() %></p>
        <p><strong>Сообщение:</strong> <%= exception.getMessage() %></p>

        <details>
            <summary>Детали ошибки (для разработчика)</summary>
            <pre style="background-color: #f8f9fa; padding: 10px; overflow-x: auto;">
                        <% exception.printStackTrace(new java.io.PrintWriter(out)); %>
                    </pre>
        </details>
        <% } else { %>
        <p>Неизвестная ошибка</p>
        <% } %>
    </div>

    <div style="margin-top: 30px;">
        <a href="${pageContext.request.contextPath}/index.jsp" class="logout-btn">Вернуться на главную</a>
    </div>
</div>
</body>
</html>
<%@ page isErrorPage="true" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
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

        <c:if test="${not empty exception}">
            <p><strong>Тип ошибки:</strong> <c:out value="${exception.class.name}"/></p>
            <p><strong>Сообщение:</strong> <c:out value="${exception.message}"/></p>

            <details>
                <summary>Информация об ошибке</summary>
                <p>Пожалуйста, сообщите администратору код ошибки: <strong><c:out value="${errorCode}"/></strong></p>
                <p>Время: <c:out value="${timestamp}"/></p>
            </details>
        </c:if>

        <c:if test="${empty exception}">
            <p>Неизвестная ошибка</p>
        </c:if>
    </div>

    <div style="margin-top: 30px;">
        <a href="${pageContext.request.contextPath}/index.jsp" class="logout-btn">Вернуться на главную</a>
    </div>
</div>
</body>
</html>
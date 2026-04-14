<%--
  Created by IntelliJ IDEA.
  User: zheni
  Date: 02.04.2026
  Time: 09:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>Подтверждение регистрации</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="welcome">
    <h1>Регистрация</h1>

    <div class="message success" style="text-align: left;">
        <p><strong>${message}</strong></p>
        <p>После подтверждения вы сможете войти в систему.</p>
    </div>

    <div style="margin-top: 30px;">
        <a href="${pageContext.request.contextPath}/index.jsp" class="logout-btn">Вернуться на главную</a>
    </div>
</div>
</body>
</html>

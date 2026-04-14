<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>Регистрация</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<form action="${pageContext.request.contextPath}/controller" method="post">
    <input type="hidden" name="command" value="ADD_USER"/>
    <h2>Регистрация нового пользователя</h2>

    <div class="requirements">
        <strong>Требования к логину:</strong>
        <ul>
            <li>От 3 до 20 символов</li>
            <li>Только латинские буквы и цифры</li>
        </ul>
        <div class="example">Пример: <c:out value="${loginExample != null ? loginExample : 'user123'}"/></div>
    </div>

    <label for="login">Логин:</label>
    <input type="text" id="login" name="login"
           value="<c:out value='${param.login}'/>"
           pattern="^[a-zA-Z0-9]{3,20}$"
           title="Логин должен содержать 3-20 латинских букв или цифр">

    <c:if test="${not empty errors.login}">
        <div class="error"><c:out value="${errors.login}"/></div>
    </c:if>

    <div class="requirements">
        <strong>Требования к паролю:</strong>
        <ul>
            <li>Минимум 8 символов</li>
            <li>Хотя бы одна буква и одна цифра</li>
        </ul>
    </div>

    <label for="pass">Пароль:</label>
    <input type="password" id="pass" name="pass" required
           minlength="8" maxlength="50"
           pattern="^(?=.*[0-9])(?=.*[a-zA-Z]).{8,}$"
           title="Минимум 8 символов: хотя бы одна буква и одна цифра">

    <c:if test="${not empty errors.password}">
        <div class="error"><c:out value="${errors.password}"/></div>
    </c:if>

    <div class="requirements">
        <strong>Требования к email:</strong>
        <ul>
            <li>Действительный email адрес</li>
        </ul>
    </div>

    <label for="email">Email:</label>
    <input type="email" id="email" name="email"
           value="<c:out value='${param.email}'/>" required
           title="Введите корректный email">

    <c:if test="${not empty errors.email}">
        <div class="error"><c:out value="${errors.email}"/></div>
    </c:if>

    <div style="margin: 10px 0;">
        <input type="checkbox" id="showPass">
        <label for="showPass">Показать пароль</label>
    </div>

    <input type="submit" value="Зарегистрироваться"/>

    <c:if test="${not empty message}">
        <div class="message success"><c:out value="${message}"/></div>
    </c:if>

    <c:if test="${not empty errorMessage}">
        <div class="message error"><c:out value="${errorMessage}"/></div>
    </c:if>
</form>

<div style="text-align: center; margin-top: 15px;">
    <a href="${pageContext.request.contextPath}/index.jsp">← На главную</a>
</div>

<script src="${pageContext.request.contextPath}/js/script.js"></script>

</body>
</html>
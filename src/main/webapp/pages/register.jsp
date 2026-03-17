<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Регистрация</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <script src="${pageContext.request.contextPath}/js/script.js"></script>
</head>
<body>
<form action="${pageContext.request.contextPath}/controller" method="post" onsubmit="return validateForm()">
    <input type="hidden" name="command" value="ADD_USER"/>
    <h2>Регистрация нового пользователя</h2>

    <div class="requirements">
        <strong>Требования к логину:</strong>
        <ul style="margin: 5px 0 0 20px; padding: 0;">
            <li>От 3 до 20 символов</li>
            <li>Только латинские буквы (a-z, A-Z), цифры (0-9) и знак подчеркивания (_)</li>
        </ul>
        <div class="example">Пример: user_123, admin, john_doe</div>
    </div>

    <label for="login">Логин:</label>
    <input type="text" id="login" name="login" value="" required
           placeholder="например: user_123"
           pattern="[a-zA-Z0-9_]{3,20}"
           title="Логин должен содержать 3-20 символов: латинские буквы, цифры или _"/>
    <div class="hint">3-20 символов: буквы, цифры, _</div>

    <div class="requirements" style="margin-top: 15px;">
        <strong>Требования к паролю:</strong>
        <ul style="margin: 5px 0 0 20px; padding: 0;">
            <li>От 3 до 50 символов</li>
            <li>Можно использовать любые символы</li>
        </ul>
        <div class="example">Пример: pass123, myStrong!Pass, 12345</div>
    </div>

    <label for="pass">Пароль:</label>
    <input type="password" id="pass" name="pass" value="" required
           placeholder="минимум 3 символа"
           minlength="3" maxlength="50"
           title="Пароль должен содержать от 3 до 50 символов"/>
    <div class="hint"> 3-50 символов, любые символы</div>

    <div style="margin: 10px 0;">
        <input type="checkbox" id="showPass">
        <label for="showPass"> Показать пароль</label>
    </div>

    <input type="submit" value="Зарегистрироваться"/>

    <% if (request.getAttribute("message") != null) { %>
    <div class="message success">
        <%= request.getAttribute("message") %>
    </div>
    <% } %>

    <% if (request.getAttribute("errorMessage") != null) { %>
    <div class="message error">
        <%= request.getAttribute("errorMessage") %>
    </div>
    <% } %>
</form>

<div style="text-align: center; margin-top: 15px;">
    <a href="${pageContext.request.contextPath}/index.jsp">← На главную</a>
</div>
</body>
</html>
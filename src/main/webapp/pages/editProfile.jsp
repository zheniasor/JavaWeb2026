<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>Edit Profile</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<form action="${pageContext.request.contextPath}/controller" method="post" enctype="multipart/form-data">
    <input type="hidden" name="command" value="UPLOAD_AVATAR"/>
    <h2>Edit Profile</h2>

    <c:if test="${not empty userAvatar}">
        <div style="text-align: center; margin-bottom: 15px;">
            <img src="${pageContext.request.contextPath}/${userAvatar}"
                 alt="Avatar" style="width: 100px; height: 100px; border-radius: 50%; object-fit: cover;">
        </div>
    </c:if>

    <label for="avatar">Avatar (image, max 5MB):</label>
    <input type="file" id="avatar" name="avatar" accept="image/*"/>

    <label for="login">New Login:</label>
    <input type="text" id="login" name="newLogin"
           value="<c:out value='${user}'/>"
           placeholder="Enter new login (3-20 characters, letters and numbers only)"/>

    <label for="email">New Email:</label>
    <input type="email" id="email" name="email"
           value="<c:out value='${userEmail}'/>"
           placeholder="Enter new email"/>

    <label for="password">New Password:</label>
    <input type="password" id="password" name="newPassword"
           placeholder="Leave blank to keep current password"/>

    <div style="margin: 10px 0;">
        <input type="checkbox" id="showPass" onclick="togglePassword()">
        <label for="showPass">Show Password</label>
    </div>

    <input type="submit" value="Update Profile"/>

    <c:if test="${not empty message}">
        <div class="message success"><c:out value="${message}"/></div>
    </c:if>

    <c:if test="${not empty errorMessage}">
        <div class="message error"><c:out value="${errorMessage}"/></div>
    </c:if>
</form>

<div style="text-align: center; margin-top: 15px;">
    <a href="${pageContext.request.contextPath}/pages/main.jsp">← Back to Main</a>
</div>

<script>
    function togglePassword() {
        let pass = document.getElementById("password");
        pass.type = pass.type === "password" ? "text" : "password";
    }
</script>
</body>
</html>
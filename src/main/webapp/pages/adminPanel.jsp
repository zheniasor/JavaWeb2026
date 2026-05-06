<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>Admin Panel</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }
        th, td {
            border: 1px solid #ddd;
            padding: 8px;
            text-align: left;
        }
        th {
            background-color: #3498db;
            color: white;
        }
        .delete-btn {
            background-color: #e74c3c;
            color: white;
            padding: 5px 10px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }
        .delete-btn:hover {
            background-color: #c0392b;
        }
    </style>
</head>
<body>
<div class="welcome" style="max-width: 800px;">
    <h1>Admin Panel</h1>
    <p>Welcome, <c:out value="${user}"/>!</p>

    <h2>User Management</h2>

    <table>
        <thead>
        <tr>
            <th>ID</th>
            <th>Login</th>
            <th>Email</th>
            <th>Confirmed</th>
            <th>Role</th>
            <th>Active</th>
            <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="u" items="${users}">
            <tr>
                <td><c:out value="${u.id}"/></td>
                <td><c:out value="${u.login}"/></td>
                <td><c:out value="${u.email}"/></td>
                <td>${u.confirmed ? 'Yes' : 'No'}</td>
                <td><c:out value="${u.role}"/></td>
                <td>${u.active ? 'Active' : 'Inactive'}</td>
                <td>
                    <form action="${pageContext.request.contextPath}/controller" method="post"
                          onsubmit="return confirm('Are you sure you want to delete this user?')">
                        <input type="hidden" name="command" value="DELETE_USER"/>
                        <input type="hidden" name="userId" value="${u.id}"/>
                        <button type="submit" class="delete-btn">Delete</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

    <div style="margin-top: 30px;">
        <a href="${pageContext.request.contextPath}/pages/main.jsp" class="logout-btn" style="background-color: #3498db;">Back to Main</a>
    </div>
</div>
</body>
</html>
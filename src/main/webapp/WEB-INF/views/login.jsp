<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login – Ocean View Resort</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="login-page">
    <div class="login-card">
        <div class="login-header">
            <div class="login-logo">🏨</div>
            <h2>Ocean View Resort</h2>
            <p>Room Reservation Management System</p>
        </div>

        <div class="login-body">
            <!-- Error Dialog -->
            <c:if test="${not empty error}">
                <div id="errorDialog" class="dialog-overlay">
                    <div class="dialog-box">
                        <span class="dialog-icon">❌</span>
                        <h3>Login Failed</h3>
                        <p>${error}</p>
                        <button class="dialog-btn error-btn" onclick="document.getElementById('errorDialog').style.display='none'">Try Again</button>
                    </div>
                </div>
            </c:if>

            <!-- <div class="default-creds">
                <strong>Default Login Credentials:</strong><br>
                Admin: <strong>admin</strong> / <strong>admin123</strong> &nbsp;|&nbsp;
                Staff: <strong>staff</strong> / <strong>staff123</strong>
            </div> -->

            <form method="post" action="${pageContext.request.contextPath}/login">
                <div class="form-group">
                    <label>Username</label>
                    <input type="text" name="username" placeholder="Enter your username"
                           value="${not empty param.username ? param.username : ''}" required autocomplete="username">
                </div>
                <div class="form-group">
                    <label>Password</label>
                    <input type="password" name="password" placeholder="Enter your password" required autocomplete="current-password">
                </div>
                <button type="submit" class="btn btn-primary btn-block" style="margin-top:8px;">
                    Sign In
                </button>
            </form>

            <!-- <p style="text-align:center; margin-top:20px; font-size:0.8rem; color:#bbb;">
                © 2025 Ocean View Resort, Galle, Sri Lanka
            </p> -->
        </div>
    </div>
</div>
</body>
</html>

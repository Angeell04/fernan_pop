<%--
  Created by IntelliJ IDEA.
  User: miran
  Date: 10/09/2026
  Time: 19:10
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" errorPage="error.jsp" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>FernanPop - Login</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/style.css">
</head>
<body>

<div class="auth-container">
    <div class="auth-card">
        <h1 class="logo" style="text-align: center; margin-bottom: 20px;">Fernan<span>Pop</span></h1>

        <form action="LoginServlet" method="POST" class="auth-form">
            <h2>Iniciar Sesión</h2>

            <% if (request.getParameter("error") != null) { %>
            <div class="error-msg">Credenciales incorrectas. Inténtalo de nuevo.</div>
            <% } %>

            <input type="email" name="email" placeholder="Correo electrónico" required>
            <input type="password" name="clave" placeholder="Contraseña" required>

            <button type="submit" class="btn-primary">Entrar</button>

            <p style="text-align: center; font-size: 0.85rem; color: var(--text-muted); margin-top: 10px;">
                ¿No tienes cuenta? <a href="registro.jsp" style="color: var(--accent); text-decoration: none; font-weight: 600;">Regístrate</a>
            </p>
            <div style="text-align: center; margin-top: 15px; display: flex; flex-direction: column; gap: 10px; align-items: center;">
                <!-- Opción A: Botón estilizado completo -->
                <a href="InvitadoServlet" class="btn-invitado">Entrar como invitado</a>
            </div>
        </form>
    </div>
</div>

</body>
</html>

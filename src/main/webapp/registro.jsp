<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" errorPage="error.jsp" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>FernanPop - Registro</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/style.css?v=2.0">
</head>
<body>

<div class="auth-container">
    <div class="auth-card">
        <h1 class="logo" style="text-align: center; margin-bottom: 20px;">Fernan<span>Pop</span></h1>

        <form action="RegistroServlet" method="POST" class="app-form">
            <h2 style="margin-bottom: 10px;">Crear Cuenta</h2>

            <% if (request.getParameter("error") != null) { %>
            <div class="alert-error">
                No se pudo completar el registro. Inténtalo de nuevo.
            </div>
            <% } %>

            <% if (request.getParameter("exito") != null) { %>
            <div class="alert-success">
                ¡Registro casi completado! Te hemos enviado un enlace a tu correo electrónico para activar tu cuenta.
            </div>
            <% } %>

            <div class="form-group">
                <label for="nombre">Nombre</label>
                <input type="text" id="nombre" name="nombre" placeholder="Tu nombre" required>
            </div>

            <div class="form-group">
                <label for="apellido">Apellido</label>
                <input type="text" id="apellido" name="apellido" placeholder="Tu apellido" required>
            </div>

            <div class="form-group">
                <label for="movil">Teléfono móvil</label>
                <input type="tel" id="movil" name="movil" placeholder="600000000" required>
            </div>

            <div class="form-group">
                <label for="email">Correo electrónico</label>
                <input type="email" id="email" name="email" placeholder="correo@ejemplo.com" required>
            </div>

            <div class="form-group">
                <label for="clave">Contraseña</label>
                <input type="password" id="clave" name="clave" placeholder="••••••••" required>
            </div>

            <button type="submit" class="btn-primary" style="margin-top: 10px;">Registrarse</button>

            <div style="text-align: center; margin-top: 15px; display: flex; flex-direction: column; gap: 8px;">
                <p style="font-size: 0.85rem; color: var(--text-muted); margin: 0;">
                    ¿Ya tienes cuenta? <a href="index.jsp?vistaLogin=login" style="color: var(--primary); text-decoration: none; font-weight: 600;">Inicia Sesión</a>
                </p>
                <a href="InvitadoServlet" style="font-size: 0.85rem; color: var(--text-muted); text-decoration: underline;">
                    Continuar como invitado
                </a>
            </div>
        </form>
    </div>
</div>

</body>
</html>
<%--
  Created by IntelliJ IDEA.
  User: miran
  Date: 9/09/2026
  Time: 19:30
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" errorPage="error.jsp" %>
<%@ page import="models.Usuario" %>
<%
    Usuario usuarioLogueado = (Usuario) session.getAttribute("usuario");
    boolean estaLogueado = (usuarioLogueado != null);

    // Comprobamos si ha iniciado navegación como invitado
    Boolean esInvitadoAttr = (Boolean) session.getAttribute("esInvitado");
    boolean esInvitado = (esInvitadoAttr != null && esInvitadoAttr);

    String vistaLogin = request.getParameter("vistaLogin");
    if (vistaLogin == null) vistaLogin = "login";

    String seccion = request.getParameter("seccion");
    if (seccion == null) seccion = "inicio";

    // Seguridad: Si no está logueado ni es invitado, o intenta entrar a una sección restringida como invitado
    boolean accesoPermitido = estaLogueado || (esInvitado && (seccion.equals("inicio") || seccion.equals("ofertas")));
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>FernanPop</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>

<% if (!accesoPermitido) { %>
<!-- Si no tiene acceso (no logueado/no invitado o intento de entrar a zona privada como invitado) -->
<div class="auth-container">
    <% if (vistaLogin.equals("login")) { %>
    <jsp:include page="login.jsp" />
    <% } else { %>
    <jsp:include page="registro.jsp" />
    <% } %>
</div>
<% } else { %>
<!-- Carga Navegación y Secciones accesibles -->
<jsp:include page="navbar.jsp" />

<div class="main-container">
    <% if (seccion.equals("inicio")) { %>
    <jsp:include page="inicio.jsp" />
    <% } else if (seccion.equals("ofertas")) { %>
    <jsp:include page="ofertas.jsp" />
    <% } else if (estaLogueado && seccion.equals("publicar")) { %>
    <jsp:include page="publicar.jsp" />
    <% } else if (estaLogueado && seccion.equals("misProductos")) { %>
    <jsp:include page="misProductos.jsp" />
    <% } else if (estaLogueado && seccion.equals("tratos")) { %>
    <jsp:include page="tratos.jsp" />
    <% } else if (estaLogueado && seccion.equals("ajustes")) { %>
    <jsp:include page="ajustes.jsp" />
    <% } else { %>
    <!-- Fallback por si intenta forzar una sección privada como invitado -->
    <jsp:include page="inicio.jsp" />
    <% } %>
</div>
<% } %>

</body>
</html>
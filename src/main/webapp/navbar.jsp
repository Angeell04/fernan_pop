<%--
  Created by IntelliJ IDEA.
  User: miran
  Date: 10/09/2026
  Time: 19:09
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" errorPage="error.jsp" %>
<%@ page import="models.Usuario" %>
<%
  // Detectamos la sección activa desde la URL (?seccion=...)
  String seccionActiva = request.getParameter("seccion");
  if (seccionActiva == null) seccionActiva = "inicio";

  // Control de sesión e invitado
  Usuario usuarioNavbar = (Usuario) session.getAttribute("usuario");
  boolean estaLogueadoNavbar = (usuarioNavbar != null);
%>

<!-- Menú Superior (PC) -->
<header class="header-desktop">
  <a href="index.jsp?seccion=inicio" class="logo">Fernan<span>Pop</span></a>
  <nav class="nav-desktop">
    <a href="index.jsp?seccion=inicio" class="<%= seccionActiva.equals("inicio") ? "active" : "" %>">Inicio</a>


    <% if (estaLogueadoNavbar) { %>
    <a href="index.jsp?seccion=ofertas" class="<%= seccionActiva.equals("ofertas") ? "active" : "" %>">Ofertas</a>
    <a href="index.jsp?seccion=publicar" class="<%= seccionActiva.equals("publicar") ? "active" : "" %>">Publicar</a>
    <a href="index.jsp?seccion=misProductos" class="<%= seccionActiva.equals("misProductos") ? "active" : "" %>">Mis Productos</a>
    <a href="index.jsp?seccion=tratos" class="<%= seccionActiva.equals("tratos") ? "active" : "" %>">Tratos</a>
    <a href="index.jsp?seccion=ajustes" class="<%= seccionActiva.equals("ajustes") ? "active" : "" %>">Ajustes</a>
    <a href="LogoutServlet" class="btn-logout">Cerrar Sesión</a>
    <% } else { %>
    <a href="LogoutServlet" class="btn-login">Iniciar Sesión</a>
    <% } %>
  </nav>
</header>

<!-- Menú Inferior (Móvil) -->
<nav class="bottom-nav">
  <a href="index.jsp?seccion=inicio" class="<%= seccionActiva.equals("inicio") ? "active" : "" %>">🏠<br>Inicio</a>

  <% if (estaLogueadoNavbar) { %>
  <a href="index.jsp?seccion=ofertas" class="<%= seccionActiva.equals("ofertas") ? "active" : "" %>">🏷️<br>Ofertas</a>
  <a href="index.jsp?seccion=publicar" class="<%= seccionActiva.equals("publicar") ? "active" : "" %>">➕<br>Vender</a>
  <a href="index.jsp?seccion=misProductos" class="<%= seccionActiva.equals("misProductos") ? "active" : "" %>">📦<br>Mis Prod.</a>
  <a href="index.jsp?seccion=tratos" class="<%= seccionActiva.equals("tratos") ? "active" : "" %>">🤝<br>Tratos</a>
  <a href="index.jsp?seccion=ajustes" class="<%= seccionActiva.equals("ajustes") ? "active" : "" %>">⚙️<br>Ajustes</a>
  <% } else { %>
  <a href="LogoutServlet">🔑<br>Entrar</a>
  <% } %>
</nav>
<%--
  Created by IntelliJ IDEA.
  User: miran
  Date: 10/09/2026
  Time: 19:10
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" errorPage="error.jsp" %>
<%@ page import="java.util.*, models.Producto, models.Usuario, controller.GestionAPP" %>

<%
    // 1. Control de acceso adaptado a Invitados (evita la redirección a login.jsp)
    Usuario usuarioLogueado = (Usuario) session.getAttribute("usuario");
    Boolean esInvitadoAttr = (Boolean) session.getAttribute("esInvitado");
    boolean esInvitado = (esInvitadoAttr != null && esInvitadoAttr);

    // Si no está logueado ni es invitado, por seguridad sí redirigimos
    if (usuarioLogueado == null && !esInvitado) {
        response.sendRedirect("login.jsp");
        return;
    }

    GestionAPP gestionAPP = new GestionAPP();
    List<Producto> listaProductos = new ArrayList<>();

    String textoBusqueda = "";
    if ("GET".equalsIgnoreCase(request.getMethod())){
        textoBusqueda = request.getParameter("q");
        if (textoBusqueda != null && !textoBusqueda.trim().isEmpty()) {
            listaProductos = gestionAPP.buscaProductoTexto(textoBusqueda);
        } else {
            listaProductos = gestionAPP.getAllProductos();
        }
    }
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>FernanPop - Inicio</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/style.css">
</head>
<body>

<div class="main-container">

    <!-- BARRA DE BÚSQUEDA (Dirigida a index.jsp manteniendo la sección) -->
    <div class="search-section">
        <form action="index.jsp" method="GET" class="search-form">
            <input type="hidden" name="seccion" value="inicio">
            <input type="text"
                   name="q"
                   placeholder="Buscar productos (ej. PlayStation, Bici...)"
                   value="<%= textoBusqueda != null ? textoBusqueda : "" %>">
            <button type="submit" class="btn-primary">Buscar</button>
            <% if (textoBusqueda != null && !textoBusqueda.trim().isEmpty()) { %>
            <a href="index.jsp?seccion=inicio" class="btn-clean">Limpiar</a>
            <% } %>
        </form>
    </div>

    <h2>
        <%= (textoBusqueda != null && !textoBusqueda.trim().isEmpty())
                ? "Resultados para: \"" + textoBusqueda + "\""
                : "Productos Disponibles" %>
    </h2>

    <!-- GRID DE RESULTADOS -->
    <div class="products-grid">
        <% if (listaProductos != null && !listaProductos.isEmpty()) {
            for (Producto p : listaProductos) { %>
        <a href="producto.jsp?id=<%= p.getId() %>" class="product-card-link">
            <div class="product-card">
                <h3><%= p.getTitulo() %></h3>
                <p class="price"><%= p.getPrecio() %> €</p>
            </div>
        </a>
        <%  }
        } else { %>
        <p style="color: var(--text-muted); grid-column: 1 / -1;">
            No se encontraron productos disponibles.
        </p>
        <% } %>
    </div>
</div>

</body>
</html>
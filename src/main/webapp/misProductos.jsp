<%@ page import="models.Usuario" %>
<%@ page import="models.Producto" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: miran
  Date: 13/09/2026
  Time: 18:32
  To change this template use File | Settings | File Templates.
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" errorPage="error.jsp" %>
<%
    // 1. Obtenemos el usuario guardado en el atributo de sesión
    Usuario usuarioLogueado = (Usuario) session.getAttribute("usuario");

    // 2. Comprobamos si está logueado
    if (usuarioLogueado == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    // 3. Obtenemos la lista de sus productos en venta
    List<Producto> listaProductos = usuarioLogueado.getEnVenta();
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>FernanPop - Mis Productos</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/style.css">
</head>
<body>


<div class="main-container">
    <h2>Ver productos personales</h2>
    <p style="color: var(--text-muted); margin-top: 8px; margin-bottom: 20px;">
        Consulta y gestiona los productos que tienes a la venta.
    </p>

    <div class="products-grid">
        <% if (listaProductos != null && !listaProductos.isEmpty()) {
            for (Producto p : listaProductos) { %>
        <div class="product-card">
            <h3><%= p.getTitulo() %></h3>
            <p class="description"><%= p.getDescripcion() %></p>
            <p class="price"><%= p.getPrecio() %> €</p>
        </div>
        <%  }
        } else { %>
        <p style="color: var(--text-muted); grid-column: 1 / -1;">
            No has subido aún ningún producto.
            <a href="publicar.jsp" style="color: var(--accent); text-decoration: none; font-weight: 600;">
                ¿Quieres subir un producto?
            </a>
        </p>
        <% } %>
    </div>
</div>

</body>
</html>